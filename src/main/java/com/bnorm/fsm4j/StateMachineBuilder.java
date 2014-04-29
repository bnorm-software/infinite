package com.bnorm.fsm4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * State machine builder interface.  A state machine builder provides access to configuring each state and also for
 * creating the final state machine.  Since the builder and the state machine share the same backend it is possible to
 * build the state machine before configuring any states.  This allows the state machine itself to be reference from any
 * from any entrance or exit actions.  This however, is not fully supported at this time.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <B> the class type of the state builder.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineBuilder<S extends State, E extends Event, B extends StateBuilder<S, E>> {

    /**
     * Creates the default state machine builder.  This is a combination of the default state builder factory and
     * internal state factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @return default state machine builder.
     */
    static <S extends State, E extends Event> StateMachineBuilder<S, E, StateBuilder<S, E>> createDefault() {
        return create(StateBuilderFactory.createDefault(), InternalStateFactory.createDefault());
    }

    /**
     * Creates a state machine builder from the specified state builder factory and internal state factory.
     *
     * @param configurationFactory the factory used to create state builders.
     * @param stateFactory the factory used to create internal states.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <B> the class type of the state builder.
     * @return a new state machine builder.
     */
    static <S extends State, E extends Event, B extends StateBuilder<S, E>> StateMachineBuilder<S, E, B> create(
            StateBuilderFactory<S, E, B> configurationFactory, InternalStateFactory stateFactory) {
        return new StateMachineBuilder<S, E, B>() {
            /** The state to internal state map. */
            private final Map<S, InternalState<S, E>> states = new HashMap<>();
            /** The event to transition map. */
            private final Map<E, Set<Transition<S>>> transitions = new HashMap<>();

            @Override
            public B configure(S state) {
                InternalState<S, E> internal = states.computeIfAbsent(state, stateFactory::create);
                return configurationFactory.create(states, transitions, internal);
            }

            @Override
            public StateMachine<S, E> build(S starting) {
                return new StateMachineBase<>(states, transitions, starting);
            }
        };
    }

    /**
     * Returns a state builder that can be used to configure the specified state.
     *
     * @param state the state to configure.
     * @return the state builder for the state.
     */
    B configure(S state);

    /**
     * Builds and returns the state machine the state machine builder is constructing.
     *
     * @param starting the starting state of the state machine.
     * @return the state machine.
     */
    StateMachine<S, E> build(S starting);
}
