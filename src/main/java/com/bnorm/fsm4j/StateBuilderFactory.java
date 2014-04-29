package com.bnorm.fsm4j;

import java.util.Map;
import java.util.Set;

/**
 * A factory interface for state builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <B> the class type of the state builder.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateBuilderFactory<S extends State, E extends Event, B extends StateBuilder<S, E>> {

    /**
     * Returns the default state builder factory.  This is the state builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @return default state builder factory.
     */
    static <S extends State, E extends Event> StateBuilderFactory<S, E, StateBuilder<S, E>> createDefault() {
        return StateBuilderBase::new;
    }

    /**
     * Creates a new state builder from the specified state map, transition map, and internal state being built.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     * @return a new state builder.
     */
    B create(Map<S, InternalState<S, E>> states, Map<E, Set<Transition<S>>> transitions, InternalState<S, E> state);
}
