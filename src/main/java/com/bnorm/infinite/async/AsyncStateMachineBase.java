package com.bnorm.infinite.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import com.bnorm.infinite.StateMachineBase;
import com.bnorm.infinite.StateMachineException;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base implementation of an asynchronous state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public class AsyncStateMachineBase<S, E, C> extends StateMachineBase<S, E, C> implements AsyncStateMachine<S, E, C> {

    /** The class logger. */
    private static final Logger log = LoggerFactory.getLogger(AsyncStateMachineBase.class);

    /** The state machine lock used to make the asynchronous state machine thread safe. */
    protected final ReentrantLock stateMachineLock;

    /** The priority blocking event queue used to order submitted events. */
    protected final BlockingQueue<AsyncEventTask<E, Optional<Transition<S, E, C>>>> eventQueue;

    /** The incrementing priority of the next event. */
    protected final AtomicLong priority;

    /** If the asynchronous state machine is currently running. */
    protected final AtomicBoolean running;

    /**
     * Constructs a new state machine from the specified state machine structure, starting state, and context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     */
    public AsyncStateMachineBase(StateMachineStructure<S, E, C> structure, S starting, C context) {
        super(structure, starting, context);
        this.stateMachineLock = new ReentrantLock();
        this.eventQueue = new PriorityBlockingQueue<>();
        this.priority = new AtomicLong(Long.MIN_VALUE + 1);
        this.running = new AtomicBoolean(false);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void stop() {
        if (running.getAndSet(false)) {
            // Submit a poisonous pill to the event queue that will cause it to process an event and stop running.
            log.trace("Submitting poisonous pill into event queue");
            eventQueue.add(new Poison<>(priority.getAndIncrement()));
        }
    }

    @Override
    public void run() {
        running.set(true);
        try {
            while (running.get()) {
                try {
                    AsyncEventTask<?, ?> asyncEventTask = eventQueue.take();
                    log.trace("Running next event [{}] taken from the task queue.", asyncEventTask.getEvent());
                    asyncEventTask.run();
                    asyncEventTask.get();
                } catch (InterruptedException | CancellationException e) {
                    log.warn("Exception was thrown while trying to process an event", e);
                    /* There are 3 types of exceptions being caught here:
                     * 1. Interrupt from BlockQueue#take()
                     *     - This case can be safely ignored because we will loop and wait again.
                     * 2. Interrupt from AsyncEventTask#get()
                     *     - This should be almost impossible unless something really terrible happened.
                     * 3. Cancel from AsyncEventTask#get()
                     *     - Someone outside the state machine cancelled the submit or inject.
                     */
                } catch (ExecutionException e) {
                    // This is the exception we want to rethrow.  There was an issue performing a transition.
                    throw new StateMachineException(e);
                }
            }
        } finally {
            // If we ever stop running for whatever reason, make sure the state machine is marked as such.
            if (running.getAndSet(false)) {
                log.warn("The state machine thread exited without being properly shutdown.");
            }
        }
    }

    @Override
    public Optional<Transition<S, E, C>> fire(E event) {
        if (stateMachineLock.isHeldByCurrentThread()) {
            throw new StateMachineException("StateMachine#fire(E) was called from within a synchronous Action or " +
                                                    "synchronous TransitionListener.\n" +
                                                    "Please use AsyncStateMachine#sumbit(E), " +
                                                    "AsyncStateMachine#inject(E), an asynchronous Action, or " +
                                                    "asynchronous TransitionListener.");
        }
        try {
            long pValue = priority.getAndIncrement();
            log.trace("Firing [{}] with priority [{}].", event, pValue);
            return submit(event, pValue).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new StateMachineException(e);
        }
    }

    @Override
    public Future<Optional<Transition<S, E, C>>> submit(E event) {
        long pValue = priority.getAndIncrement();
        log.trace("Submitting [{}] to the event queue with priority [{}].", event, pValue);
        return submit(event, pValue);
    }

    @Override
    public Future<Optional<Transition<S, E, C>>> inject(E event) {
        log.trace("Injecting [{}] into the event queue with priority [{}].", event, Long.MIN_VALUE);
        return submit(event, Long.MIN_VALUE);
    }

    @Override
    public void clear() {
        if (!eventQueue.isEmpty()) {
            List<AsyncEventTask<E, Optional<Transition<S, E, C>>>> asyncEventTasks = new ArrayList<>();
            eventQueue.drainTo(asyncEventTasks);
            for (AsyncEventTask<E, Optional<Transition<S, E, C>>> asyncEventTask : asyncEventTasks) {
                if (asyncEventTask instanceof Poison) {
                    // Resubmit any poisonous pills so stop commands are still processed.
                    eventQueue.add(asyncEventTask);
                } else {
                    log.trace("Canceling next event [{}] taken from the task queue.", asyncEventTask.getEvent());
                    if (!asyncEventTask.cancel(false)) {
                        throw new AssertionError("There was a problem canceling task in the event queue.");
                    }
                }
            }
        }
    }

    @Override
    public Queue<AsyncEventTask<E, Optional<Transition<S, E, C>>>> getQueue() {
        Queue<AsyncEventTask<E, Optional<Transition<S, E, C>>>> copy = new PriorityQueue<>(eventQueue);
        copy.removeIf(t -> t instanceof Poison);
        return copy;
    }

    /**
     * Submits specified event to the specified priority to the event queue.  All events processed by the asynchronous
     * state machine are technically submitted, they are just submitted differently.
     *
     * <ol> <li>Submitted events are submitted to the priority queue with the next priority and the resulting transition
     * Future returned to the caller.</li> <li>Injected events are submitted to the priority queue with the highest
     * possible priority and the resulting transition Future returned to the caller.</li> <li>Fired events are submitted
     * to the priority queue with the next priority and the resulting transition Future is waited on before the
     * transition is returned to the caller.</li> </ol>
     *
     * @param event the event to submit.
     * @param pValue the priority of the submitted event.
     * @return the resulting transition Future.
     */
    private Future<Optional<Transition<S, E, C>>> submit(E event, long pValue) {
        final AsyncEventTask<E, Optional<Transition<S, E, C>>> asyncEventTask;
        asyncEventTask = new AsyncEventTask<>(event, pValue, () -> safeFire(event));
        eventQueue.add(asyncEventTask);
        return asyncEventTask;
    }

    /**
     * Safely fires the event to the state machine.  This uses the StateMachineBase implementation to handle the actual
     * processing of the event but wraps it with a Lock.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    private Optional<Transition<S, E, C>> safeFire(E event) {
        log.trace("Acquiring state machine lock for event [{}].", event);
        stateMachineLock.lock();
        try {
            return super.fire(event);
        } finally {
            log.trace("Releasing state machine lock for event [{}].", event);
            stateMachineLock.unlock();
        }
    }

    /**
     * A poison pill asynchronous event task.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     */
    private static class Poison<S, E, C> extends AsyncEventTask<E, Optional<Transition<S, E, C>>> {

        /**
         * Constructor for a poison pill asynchronous event task.
         *
         * @param priority the poison pill priority.
         */
        protected Poison(long priority) {
            super(null, priority, () -> null);
        }
    }
}
