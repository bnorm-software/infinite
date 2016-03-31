package com.bnorm.infinite.async;

import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import com.bnorm.infinite.StateMachine;
import com.bnorm.infinite.StateMachineException;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The operation asynchronous state machine interface.  The asynchronous state machine is different from the base state
 * machine interface in that it must be running on a thread and processes events asynchronously via a priority queue.
 * Events can be {@link #fire(Object) fired}, {@link #submit(Object) submitted}, and {@link #inject(Object) injected}.
 *
 * <p>If the asynchronous state machine is not running on a thread, any events added to the queue will not be processed
 * until the asynchronous state machine is running.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public class AsyncStateMachine<S, E, C> extends StateMachine<S, E, C> implements Runnable {

    /** The class logger. */
    private static final Logger log = LoggerFactory.getLogger(AsyncStateMachine.class);

    /** The state machine lock used to make the asynchronous state machine thread safe. */
    protected final ReentrantLock stateMachineLock;

    /** The priority blocking event queue used to order submitted events. */
    protected final PriorityBlockingQueue<AsyncEventTask<E, Optional<Transition<S, E, C>>>> eventQueue;

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
    public AsyncStateMachine(StateMachineStructure<S, E, C> structure, S starting, C context) {
        super(structure, starting, context);
        this.stateMachineLock = new ReentrantLock();
        this.eventQueue = new PriorityBlockingQueue<>();
        this.priority = new AtomicLong(Long.MIN_VALUE + 1);
        this.running = new AtomicBoolean(false);
    }

    /**
     * Returns {@code true} if the asynchronous state machine is currently running, {@code false} otherwise.
     *
     * @return if the asynchronous state machine is running.
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Singles the asynchronous state machine to stop running when it has finished processing any outstanding events.
     */
    public void stop() {
        if (isRunning()) {
            synchronized (eventQueue) {
                // Submit a poisonous pill to the event queue that will cause it to process an event and stop running.
                log.trace("Submitting poisonous pill into event queue");
                running.set(false);
                eventQueue.add(new AsyncEventTask<>(null, priority.getAndIncrement(), () -> null));
            }
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
            if (running.get()) {
                log.warn("The state machine thread exited without being properly shutdown.");
                running.set(false);
            }
        }
    }

    /**
     * Submits the specified event to the state machine and wait for it to be processed.  This adds the specified event
     * to the end of the event queue.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
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

    /**
     * Submits the specified event to the state machine to be processed.  This adds the specified event to the end of
     * the event queue.
     *
     * @param event the event submitted.
     * @return the future of the resulting transition.
     */
    public Future<Optional<Transition<S, E, C>>> submit(E event) {
        long pValue = priority.getAndIncrement();
        log.trace("Submitting [{}] to the event queue with priority [{}].", event, pValue);
        return submit(event, pValue);
    }

    /**
     * Injects the specified event to the state machine for immediate processing.  This adds the specified event at the
     * beginning of the event queue.  Multiple injects have no guarantee in what order they will be processed.
     *
     * @param event the event injected.
     * @return the future of the resulting transition.
     */
    public Future<Optional<Transition<S, E, C>>> inject(E event) {
        log.trace("Injecting [{}] into the event queue with priority [{}].", event, Long.MIN_VALUE);
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
    private Future<Optional<Transition<S, E, C>>> submit(E event, long priority) {
        if (!isRunning()) {
            log.warn("Submitting [{}] to the event queue while it is not running!", event);
        }
        synchronized (eventQueue) {
            final AsyncEventTask<E, Optional<Transition<S, E, C>>> asyncEventTask;
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
     * Asynchronous state machine builder interface.  An asynchronous state machine builder provides access to
     * configuring each state and also for creating the final asynchronous state machine.  Since the builder and the
     * state machine share the same backend it is possible to build the asynchronous state machine before configuring
     * any states.  This allows the state machine itself to be reference from any entrance or exit actions.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @author Brian Norman
     * @since 1.1.0
     */
    public static class Builder<S, E, C> extends StateMachine.Builder<S, E, C> {

        private final AsyncActionFactory<S, E, C> asyncActionFactory;

        /**
         * Constructs a new state machine builder from the specified state machine structure, asynchronous state machine
         * factory, asynchronous state builder factory, and asynchronous action factory.
         *
         * @param asyncActionFactory
         */
        public Builder(AsyncActionFactory<S, E, C> asyncActionFactory) {
            super();

            this.asyncActionFactory = asyncActionFactory;
        }

        /**
         * Returns an asynchronous state builder that can be used to configure the specified state.
         *
         * @param state the state to configure.
         * @return the state builder for the state.
         */
        @Override
        public AsyncStateBuilder<S, E, C> configure(S state) {
            return new AsyncStateBuilder<>(structure, state, asyncActionFactory);
        }

        /**
         * Builds and returns the asynchronous state machine the asynchronous state machine builder is constructing.
         *
         * @param starting the starting state of the state machine.
         * @param context the state machine context.
         * @return the state machine.
         */
        @Override
        public AsyncStateMachine<S, E, C> build(S starting, C context) {
            return new AsyncStateMachine<>(structure, starting, context);
        }
    }
}
