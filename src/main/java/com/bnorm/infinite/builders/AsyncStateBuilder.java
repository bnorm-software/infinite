package com.bnorm.infinite.builders;

import java.util.function.Supplier;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionGuard;
import com.bnorm.infinite.async.AsyncActionFactory;

/**
 * Represents a builder of a specific state for an asynchronous state machine.  The interface provides methods that add
 * behavior to the asynchronous state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncStateBuilder<S, E, C> extends StateBuilder<S, E, C> {

    /**
     * Returns the factory used to create asynchronous actions.
     *
     * @return the asynchronous action factory.
     */
    AsyncActionFactory<S, E, C> getAsyncActionFactory();

    @Override
    AsyncStateBuilder<S, E, C> childOf(S state);

    @Override
    default AsyncStateBuilder<S, E, C> onEntry(Action<S, E, C> action) {
        StateBuilder.super.onEntry(action);
        return this;
    }

    /**
     * Adds the specified action as an asynchronous entry action to the internal state.
     *
     * @param action the new asynchronous entry action.
     * @return the current asynchronous state builder for chaining.
     */
    default AsyncStateBuilder<S, E, C> onAsyncEntry(Action<S, E, C> action) {
        return onEntry(getAsyncActionFactory().create(action));
    }

    @Override
    default AsyncStateBuilder<S, E, C> onExit(Action<S, E, C> action) {
        StateBuilder.super.onExit(action);
        return this;
    }

    /**
     * Adds the specified action as an asynchronous exit action to the internal state.
     *
     * @param action the new asynchronous exit action.
     * @return the current asynchronous state builder for chaining.
     */
    default AsyncStateBuilder<S, E, C> onAsyncExit(Action<S, E, C> action) {
        return onExit(getAsyncActionFactory().create(action));
    }

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Transition<S, C> transition);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, TransitionGuard<C> guard);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination, TransitionGuard<C> guard);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard);
}
