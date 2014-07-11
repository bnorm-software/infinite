package com.bnorm.infinite.builders;

import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;

/**
 * A factory interface for state builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.1
 * @since 1.0.0
 */
public interface StateBuilderFactory<S, E, C> {

    /**
     * Returns the default state builder factory.  This is the state builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state builder factory.
     */
    static <S, E, C> StateBuilderFactory<S, E, C> getDefault() {
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
     * @return a new state builder.
     */
    StateBuilder<S, E, C> create(TransitionFactory<S, C> transitionFactory, Map<S, InternalState<S, E, C>> states,
                                 Map<E, Set<Transition<S, C>>> transitions, InternalState<S, E, C> state);
}
