package com.bnorm.infinite;

/**
 * A factory interface for internal states.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface InternalStateFactory<S, E, C> {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default internal state factory.
     */
    static <S, E, C> InternalStateFactory<S, E, C> getDefault() {
        return InternalStateBase::new;
    }

    /**
     * Creates a new internal state from specified state.
     *
     * @param state the state to wrap.
     * @return a new internal state.
     */
    InternalState<S, E, C> create(S state);
}
