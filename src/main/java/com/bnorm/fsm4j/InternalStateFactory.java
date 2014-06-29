package com.bnorm.fsm4j;

/**
 * A factory interface for internal states.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
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
     * @return a new internal state.
     */
    <S extends State, E extends Event> InternalState<S, E> create(S state);
}
