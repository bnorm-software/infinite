package com.bnorm.infinite.async;

import com.bnorm.infinite.Action;

/**
 * A factory interface for asynchronous actions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncActionFactory<S, E, C> {

    /**
     * Creates a new action that is an asynchronous version of the specified action.  The new asynchronous action uses
     * the factory completion executor to execute the specified action.
     *
     * @param action the action to perform asynchronously.
     * @return a new asynchronous action.
     */
    Action<S, E, C> create(Action<? super S, ? super E, ? super C> action);
}
