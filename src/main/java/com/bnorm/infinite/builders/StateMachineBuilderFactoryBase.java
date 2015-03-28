package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.StateMachineFactoryBase;
import com.bnorm.infinite.StateMachineStructure;

/**
 * The base implementation of an state machine builder factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class StateMachineBuilderFactoryBase<S, E, C> implements StateMachineBuilderFactory<S, E, C> {

    /** The state machine factory. */
    protected final StateMachineFactory<S, E, C> stateMachineFactory;

    /** The state builder factory. */
    protected final StateBuilderFactory<S, E, C> stateBuilderFactory;

    /**
     * Constructs a new state machine builder factory with the specified state machine factory and state builder
     * factory.
     *
     * @param stateMachineFactory the factory used to create state machines.
     * @param stateBuilderFactory the factory used to create state builders.
     */
    public StateMachineBuilderFactoryBase(StateMachineFactory<S, E, C> stateMachineFactory,
                                          StateBuilderFactory<S, E, C> stateBuilderFactory) {
        this.stateMachineFactory = stateMachineFactory;
        this.stateBuilderFactory = stateBuilderFactory;
    }

    /** Constructs a new state machine builder with the default state machine factory and state builder factory. */
    public StateMachineBuilderFactoryBase() {
        this(new StateMachineFactoryBase<>(), new StateBuilderFactoryBase<>());
    }

    @Override
    public StateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure) {
        return new StateMachineBuilderBase<>(structure, stateMachineFactory, stateBuilderFactory);
    }
}
