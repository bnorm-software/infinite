package com.bnorm.infinite.async;

import com.bnorm.infinite.TransitionListener;

/**
 * A factory interface for asynchronous transition listeners.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncTransitionListenerFactory<S, E, C> {

    /**
     * Creates a new transition listener that is an asynchronous version of the specified transition listener.  The new
     * asynchronous transition listener uses the factory completion executor to execute the specified transition
     * listener.
     *
     * @param listener the transition listener to perform asynchronously.
     * @return a new asynchronous transition listener.
     */
    TransitionListener<S, E, C> create(TransitionListener<? super S, ? super E, ? super C> listener);
}
