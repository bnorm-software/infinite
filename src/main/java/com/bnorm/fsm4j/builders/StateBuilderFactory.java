package com.bnorm.fsm4j.builders;

import java.util.Map;
import java.util.Set;

import com.bnorm.fsm4j.InternalState;
import com.bnorm.fsm4j.Transition;
import com.bnorm.fsm4j.TransitionFactory;

/**
 * A factory interface for state builders.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateBuilderFactory {

    /**
     * Returns the default state builder factory.  This is the state builder base constructor.
     *
     * @return default state builder factory.
     */
    static StateBuilderFactory getDefault() {
        return StateBuilderBase::new;
    }

    /**
     * Creates a new state builder from the specified transition factory, state map, transition map, and internal state
     * being built.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new state builder.
     */
    <S, E, C> StateBuilder<S, E, C> create(TransitionFactory transitionFactory, Map<S, InternalState<S, E, C>> states,
                                           Map<E, Set<Transition<S>>> transitions, InternalState<S, E, C> state);
}
