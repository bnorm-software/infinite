package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncActionFactoryBase;

public class AsyncStateBuilderFactoryBase<S, E, C> extends StateBuilderFactoryBase<S, E, C>
        implements AsyncStateBuilderFactory<S, E, C> {

    protected final AsyncActionFactory<S, E, C> asyncActionFactory;

    public AsyncStateBuilderFactoryBase(AsyncActionFactory<S, E, C> asyncActionFactory) {
        this.asyncActionFactory = asyncActionFactory;
    }

    public AsyncStateBuilderFactoryBase() {
        this(new AsyncActionFactoryBase<>());
    }

    @Override
    public AsyncStateBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure, S state) {
        return new AsyncStateBuilderBase<>(structure, state, asyncActionFactory);
    }
}
