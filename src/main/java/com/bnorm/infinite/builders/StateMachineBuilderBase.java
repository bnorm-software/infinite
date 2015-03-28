package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachine;
import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.StateMachineStructure;

/**
 * The base implementation of a state machine builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class StateMachineBuilderBase<S, E, C> implements StateMachineBuilder<S, E, C> {

    /** The state machine structure. */
    protected final StateMachineStructure<S, E, C> structure;

    /** The state machine builder state machine factory. */
    protected final StateMachineFactory<S, E, C> stateMachineFactory;

    /** The state machine builder state builder factory. */
    protected final StateBuilderFactory<S, E, C> stateBuilderFactory;


    /**
     * Constructs a new state machine builder from the specified state machine structure, state machine factory, and
     * state builder factory.
     *
     * @param structure the state machine structure.
     * @param stateMachineFactory the factory used to create the state machine.
     * @param stateBuilderFactory the factory used to create state builders.
     */
    protected StateMachineBuilderBase(StateMachineStructure<S, E, C> structure,
                                      StateMachineFactory<S, E, C> stateMachineFactory,
                                      StateBuilderFactory<S, E, C> stateBuilderFactory) {
        this.structure = structure;
        this.stateMachineFactory = stateMachineFactory;
        this.stateBuilderFactory = stateBuilderFactory;
    }

    @Override
    public StateBuilder<S, E, C> configure(S state) {
        return stateBuilderFactory.create(structure, state);
    }

    @Override
    public StateMachine<S, E, C> build(S starting, C context) {
        return stateMachineFactory.create(structure, starting, context);
    }
}
