package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncActionFactoryBase;

/**
 * The base implementation of an asynchronous state builder factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class AsyncStateBuilderFactoryBase<S, E, C> extends StateBuilderFactoryBase<S, E, C>
        implements AsyncStateBuilderFactory<S, E, C> {

    /** The asynchronous action factory. */
    protected final AsyncActionFactory<S, E, C> asyncActionFactory;

    /**
     * Constructs a new asynchronous state builder factory from the specified asynchronous action factory.
     *
     * @param asyncActionFactory the factory used to create asynchronous actions.
     */
    public AsyncStateBuilderFactoryBase(AsyncActionFactory<S, E, C> asyncActionFactory) {
        this.asyncActionFactory = asyncActionFactory;
    }

    /** Constructs a new asynchronous state builder factory with a default asynchronous action factory. */
    public AsyncStateBuilderFactoryBase() {
        this(new AsyncActionFactoryBase<>());
    }

    @Override
    public AsyncStateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state) {
        return new AsyncStateBuilderBase<>(structure, state, asyncActionFactory);
    }
}
