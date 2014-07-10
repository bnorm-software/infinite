package com.bnorm.infinite;

/**
 * A factory interface for internal states.
 *
 * @author Brian Norman
 * @version 1.0.0
 * @since 1.0.0
 */
public interface InternalStateFactory {

    /**
     * Returns the default internal state factory.  This is the internal state base constructor.
     *
     * @return default internal state factory.
     */
    static InternalStateFactory getDefault() {
        return InternalStateBase::new;
    }

    /**
     * Creates a new internal state from specified state.
     *
     * @param state the state to wrap.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new internal state.
     */
    <S, E, C> InternalState<S, E, C> create(S state);
}
