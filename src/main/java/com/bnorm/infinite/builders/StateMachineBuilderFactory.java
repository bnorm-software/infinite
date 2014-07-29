package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.StateMachineStructureFactory;

/**
 * A factory interface for state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.1
 * @since 1.0.0
 */
public interface StateMachineBuilderFactory<S, E, C> {

    /**
     * Creates the default state machine builder factory.  This is the state machine builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder factory.
     */
    static <S, E, C> StateMachineBuilderFactory<S, E, C> getDefault() {
        return StateMachineBuilderBase::new;
    }

    /**
     * Creates the default state machine builder.  This is a combination of the default state machine structure, state
     * machine factory, and state builder factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder.
     */
    static <S, E, C> StateMachineBuilder<S, E, C> createDefault() {
        return StateMachineBuilderFactory.<S, E, C>getDefault()
                                         .create(StateMachineStructureFactory.createDefault(),
                                                 StateMachineFactory.getDefault(), StateBuilderFactory.getDefault());
    }

    /**
     * Creates a state machine builder from the specified state machine structure, state machine factory, and state
     * builder factory.
     *
     * @param structure the state machine structure.
     * @param stateMachineFactory the factory used to create the state machine.
     * @param stateBuilderFactory the factory used to create state builders.
     * @return a state machine builder.
     */
    StateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure,
                                        StateMachineFactory<S, E, C> stateMachineFactory,
                                        StateBuilderFactory<S, E, C> stateBuilderFactory);
}
