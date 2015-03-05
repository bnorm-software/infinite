package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.async.AsyncStateMachineFactory;
import com.bnorm.infinite.async.AsyncStateMachineFactoryBase;

public class AsyncStateMachineBuilderFactoryBase<S, E, C> extends StateMachineBuilderFactoryBase<S, E, C>
        implements AsyncStateMachineBuilderFactory<S, E, C> {

    private final AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory;
    private final AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory;

    public AsyncStateMachineBuilderFactoryBase(AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory,
                                               AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory) {
        super(asyncStateMachineFactory, asyncStateBuilderFactory);

        this.asyncStateMachineFactory = asyncStateMachineFactory;
        this.asyncStateBuilderFactory = asyncStateBuilderFactory;
    }

    public AsyncStateMachineBuilderFactoryBase() {
        this(new AsyncStateMachineFactoryBase<>(), new AsyncStateBuilderFactoryBase<>());
    }

    @Override
    public AsyncStateMachineBuilder<S, E, C> create(StateMachineStructure<S, E, C> structure) {
        return new AsyncStateMachineBuilderBase<>(structure, asyncStateMachineFactory, asyncStateBuilderFactory);
    }
}
