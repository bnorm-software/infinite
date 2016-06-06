package com.bnorm.infinite.async;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Future;

import com.bnorm.infinite.StateMachine;
import com.bnorm.infinite.Transition;

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
public interface AsyncStateMachine<S, E, C> extends StateMachine<S, E, C>, Runnable {

    /**
     * Returns {@code true} if the asynchronous state machine is currently running, {@code false} otherwise.
     *
     * @return if the asynchronous state machine is running.
     */
    boolean isRunning();

    /**
     * Signals the asynchronous state machine to stop running when it has finished processing any outstanding events.
     */
    void stop();

    /**
     * Submits the specified event to the state machine and wait for it to be processed.  This adds the specified event
     * to the end of the event queue.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    @Override
    Optional<Transition<S, E, C>> fire(E event);

    /**
     * Submits the specified event to the state machine to be processed.  This adds the specified event to the end of
     * the event queue.
     *
     * @param event the event submitted.
     * @return the future of the resulting transition.
     */
    Future<Optional<Transition<S, E, C>>> submit(E event);

    /**
     * Injects the specified event to the state machine for immediate processing.  This adds the specified event at the
     * beginning of the event queue.  Multiple injects have no guarantee in what order they will be processed.
     *
     * @param event the event injected.
     * @return the future of the resulting transition.
     */
    Future<Optional<Transition<S, E, C>>> inject(E event);

    /**
     * Signals the asynchronous state machine to clear any outstanding events.  This will cancel any Future objects that
     * have been returned by submit or injection calls that have not be executed yet.
     */
    void clear();

    /**
     * Returns a copy of the internal event queue.  Every separate call to this method will produce a new copy instance.
     * While manipulations of this queue will not affect the internal queue of the state machine, operations performed
     * upon the event task will.  This would allow the caller to cancel only specific events from the queue.
     *
     * @return a copy of the internal event queue.
     */
    Queue<AsyncEventTask<E, Optional<Transition<S, E, C>>>> getQueue();
}
