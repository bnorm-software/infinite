package com.bnorm.infinite.async;

import java.util.Optional;
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
     * Singles the asynchronous state machine to stop running when it has finished processing any outstanding events.
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
    Optional<Transition<S, C>> fire(E event);

    /**
     * Submits the specified event to the state machine to be processed.  This adds the specified event to the end of
     * the event queue.
     *
     * @param event the event submitted.
     * @return the future of the resulting transition.
     */
    Future<Optional<Transition<S, C>>> submit(E event);

    /**
     * Injects the specified event to the state machine for immediate processing.  This adds the specified event at the
     * beginning of the event queue.  Multiple injects have no guarantee in what order they will be processed.
     *
     * @param event the event injected.
     * @return the future of the resulting transition.
     */
    Future<Optional<Transition<S, C>>> inject(E event);
}
