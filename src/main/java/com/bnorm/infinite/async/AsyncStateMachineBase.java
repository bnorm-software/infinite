package com.bnorm.infinite.async;

import java.util.Optional;
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

/**
 * The base implementation of an asynchronous state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class AsyncStateMachineBase<S, E, C> extends StateMachineBase<S, E, C> implements AsyncStateMachine<S, E, C> {

    /** The state machine lock used to make the asynchronous state machine thread safe. */
    private final ReentrantLock stateMachineLock;

    /** The priority blocking event queue used to order submitted events. */
    private final PriorityBlockingQueue<AsyncEventTask<E, Optional<Transition<S, C>>>> eventQueue;

    /** The incrementing priority of the next event. */
    private final AtomicLong priority;

    /** If the asynchronous state machine is currently running. */
    private final AtomicBoolean running;

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
        if (isRunning()) {
            synchronized (eventQueue) {
                // Submit a poisonous pill to the event queue that will cause it to process an event and stop running.
                running.set(false);
                eventQueue.add(new AsyncEventTask<>(null, priority.getAndIncrement(), () -> null));
            }
        }
    }

    @Override
    public void run() {
        running.set(true);
        try {
            while (isRunning()) {
                try {
                    AsyncEventTask<?, ?> asyncEventTask = eventQueue.take();
                    if (asyncEventTask != null && asyncEventTask.getEvent() != null) {
                        asyncEventTask.run();
                        asyncEventTask.get();
                    }
                } catch (InterruptedException | CancellationException e) {
                    /* There are 3 types of exceptions being caught here:
                     * 1. Interrupt from BlockQueue#poll(long, TimeUnit)
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
            running.set(false);
        }
    }

    @Override
    public Optional<Transition<S, C>> fire(E event) {
        if (stateMachineLock.isHeldByCurrentThread()) {
            throw new StateMachineException("StateMachine#fire(E) was called from within a synchronous Action or " +
                                                    "synchronous TransitionListener.\n" +
                                                    "Please use AsyncStateMachine#sumbit(E), " +
                                                    "AsyncStateMachine#inject(E), or an asynchronous Action or " +
                                                    "asynchronous TransitionListener.");
        }
        try {
            return submit(event, priority.getAndIncrement()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new StateMachineException(e);
        }
    }

    @Override
    public Future<Optional<Transition<S, C>>> submit(E event) {
        return submit(event, priority.getAndIncrement());
    }

    @Override
    public Future<Optional<Transition<S, C>>> inject(E event) {
        return submit(event, Long.MIN_VALUE);
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
     * @param priority the priority of the submitted event.
     * @return the resulting transition Future.
     */
    private Future<Optional<Transition<S, C>>> submit(E event, long priority) {
        synchronized (eventQueue) {
            final AsyncEventTask<E, Optional<Transition<S, C>>> asyncEventTask;
            asyncEventTask = new AsyncEventTask<>(event, priority, () -> safeFire(event));
            eventQueue.add(asyncEventTask);
            return asyncEventTask;
        }
    }

    /**
     * Safely fires the event to the state machine.  This uses the StateMachineBase implementation to handle the actual
     * processing of the event but wraps it with a Lock.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    private Optional<Transition<S, C>> safeFire(E event) {
        stateMachineLock.lock();
        try {
            return super.fire(event);
        } finally {
            stateMachineLock.unlock();
        }
    }
}
