package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;

/**
 * A factory interface for state builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
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
     * Creates a new state builder from the specified state machine structure and the state being built.
     *
     * @param structure the state machine structure.
     * @param state the state being built.
     * @return a new state builder.
     */
    StateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state);
}
