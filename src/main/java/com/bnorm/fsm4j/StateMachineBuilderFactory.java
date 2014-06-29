package com.bnorm.fsm4j;

/**
 * A factory interface for state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineBuilderFactory<S extends State, E extends Event> {

    /**
     * Creates the default state machine builder.  This is a combination of the default state builder factory and
     * internal state factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @return default state machine builder.
     */
    static <S extends State, E extends Event> StateMachineBuilderFactory<S, E> getDefault() {
        return StateMachineBuilderBase::new;
    }

    /**
     * Creates a state machine builder from the specified state builder factory and internal state factory.
     *
     * @param stateFactory the factory used to create internal states.
     * @param configurationFactory the factory used to create state builders.
     * @return a state machine builder.
     */
    StateMachineBuilder<S, E> create(InternalStateFactory stateFactory, StateBuilderFactory<S, E> configurationFactory);
}
