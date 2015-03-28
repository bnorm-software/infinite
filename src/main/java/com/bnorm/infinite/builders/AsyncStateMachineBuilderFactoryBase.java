package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncStateMachineFactory;
import com.bnorm.infinite.async.AsyncStateMachineFactoryBase;

/**
 * The base implementation of an asynchronous state machine builder factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class AsyncStateMachineBuilderFactoryBase<S, E, C> extends StateMachineBuilderFactoryBase<S, E, C>
        implements AsyncStateMachineBuilderFactory<S, E, C> {

    /** The asynchronous state machine factory. */
    protected final AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory;

    /** The asynchronous state builder factory. */
    protected final AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory;

    /**
     * Constructs a new asynchronous state machine builder factory with the specified asynchronous state machine factory
     * and asynchronous state builder factory.
     *
     * @param asyncStateMachineFactory the factory used to create asynchronous state machines.
     * @param asyncStateBuilderFactory the factory used to create asynchronous state builders.
     */
    public AsyncStateMachineBuilderFactoryBase(AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory,
                                               AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory) {
        super(asyncStateMachineFactory, asyncStateBuilderFactory);

        this.asyncStateMachineFactory = asyncStateMachineFactory;
        this.asyncStateBuilderFactory = asyncStateBuilderFactory;
    }

    /**
     * Constructs a new asynchronous state machine builder with the default asynchronous state machine factory and
     * asynchronous state builder factory.
     */
    public AsyncStateMachineBuilderFactoryBase() {
        this(new AsyncStateMachineFactoryBase<>(), new AsyncStateBuilderFactoryBase<>());
    }

    @Override
    public AsyncStateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure) {
        return new AsyncStateMachineBuilderBase<>(structure, asyncStateMachineFactory, asyncStateBuilderFactory);
    }
}
