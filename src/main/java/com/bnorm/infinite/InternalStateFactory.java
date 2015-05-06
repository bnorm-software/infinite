package com.bnorm.infinite;

/**
 * A factory interface for internal states.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public interface InternalStateFactory<S, E, C> {

    /**
     * Creates a new internal state from specified state.
     *
     * @param state the state to wrap.
     * @return a new internal state.
     */
    InternalState<S, E, C> create(S state);
}
