package com.bnorm.infinite.builders;

import java.util.function.Supplier;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionGuard;

/**
 * Represents a builder of a specific state for an asynchronous state machine.  The interface provides methods that add
 * behavior to the asynchronous state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncStateBuilder<S, E, C> extends StateBuilder<S, E, C> {

    @Override
    AsyncStateBuilder<S, E, C> childOf(S state);

    @Override
    AsyncStateBuilder<S, E, C> onEntry(Action<? super S, ? super E, ? super C> action);

    /**
     * Adds the specified action as an asynchronous entry action to the internal state.
     *
     * @param action the new asynchronous entry action.
     * @return the current asynchronous state builder for chaining.
     */
    AsyncStateBuilder<S, E, C> onAsyncEntry(Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> onExit(Action<? super S, ? super E, ? super C> action);

    /**
     * Adds the specified action as an asynchronous exit action to the internal state.
     *
     * @param action the new asynchronous exit action.
     * @return the current asynchronous state builder for chaining.
     */
    AsyncStateBuilder<S, E, C> onAsyncExit(Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Transition<S, E, C> transition);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, TransitionGuard<C> guard);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination, TransitionGuard<C> guard);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination, Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination, Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, S destination, TransitionGuard<C> guard,
                                      Action<? super S, ? super E, ? super C> action);

    @Override
    AsyncStateBuilder<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard,
                                      Action<? super S, ? super E, ? super C> action);
}
