package com.bnorm.fsm4j;

/**
 * A factory interface for state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <B> the class type of the state builder.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineBuilderFactory<S extends State, E extends Event, B extends StateBuilder<S, E>> {

    /**
     * Creates the default state machine builder.  This is a combination of the default state builder factory and
     * internal state factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <B> the class type of the state builder.
     * @return default state machine builder.
     */
    static <S extends State, E extends Event, B extends StateBuilder<S, E>> StateMachineBuilderFactory<S, E, B> createDefault() {
        return StateMachineBuilderBase::new;
    }

    /**
     * Creates a state machine builder from the specified state builder factory and internal state factory.
     *
     * @param stateFactory the factory used to create internal states.
     * @param configurationFactory the factory used to create state builders.
     * @return a state machine builder.
     */
    StateMachineBuilder<S, E, B> create(InternalStateFactory stateFactory,
                                        StateBuilderFactory<S, E, B> configurationFactory);
}
