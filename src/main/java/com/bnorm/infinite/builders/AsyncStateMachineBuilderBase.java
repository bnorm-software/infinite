package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncStateMachine;
import com.bnorm.infinite.async.AsyncStateMachineFactory;

/**
 * The base implementation of an asynchronous state machine builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public class AsyncStateMachineBuilderBase<S, E, C> implements AsyncStateMachineBuilder<S, E, C> {

    /** The state machine structure. */
    private final StateMachineStructure<S, E, C> structure;

    /** The asynchronous state machine builder state machine factory. */
    private final AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory;

    /** The asynchronous state machine builder state builder factory. */
    private final AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory;

    /** the asynchronous action factory. */
    private final AsyncActionFactory<S, E, C> asyncActionFactory;


    /**
     * Constructs a new state machine builder from the specified state machine structure, asynchronous state machine
     * factory, asynchronous state builder factory, and asynchronous action factory.
     *
     * @param structure the state machine structure.
     * @param asyncStateMachineFactory the factory used to create the asynchronous state machine.
     * @param asyncStateBuilderFactory the factory used to create asynchronous state builders.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     */
    protected AsyncStateMachineBuilderBase(StateMachineStructure<S, E, C> structure,
                                           AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory,
                                           AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory,
                                           AsyncActionFactory<S, E, C> asyncActionFactory) {
        this.structure = structure;
        this.asyncStateMachineFactory = asyncStateMachineFactory;
        this.asyncStateBuilderFactory = asyncStateBuilderFactory;
        this.asyncActionFactory = asyncActionFactory;
    }

    @Override
    public AsyncStateBuilder<S, E, C> configure(S state) {
        return asyncStateBuilderFactory.create(structure, state, asyncActionFactory);
    }

    @Override
    public AsyncStateMachine<S, E, C> build(S starting, C context) {
        return asyncStateMachineFactory.create(structure, starting, context);
    }
}
