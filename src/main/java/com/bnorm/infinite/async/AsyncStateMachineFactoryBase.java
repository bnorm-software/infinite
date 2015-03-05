package com.bnorm.infinite.async;

import com.bnorm.infinite.StateMachineFactoryBase;
import com.bnorm.infinite.StateMachineStructure;

public class AsyncStateMachineFactoryBase<S, E, C> extends StateMachineFactoryBase<S, E, C>
        implements AsyncStateMachineFactory<S, E, C> {

    @Override
    public AsyncStateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting, C context) {
        return new AsyncStateMachineBase<>(structure, starting, context);
    }
}
