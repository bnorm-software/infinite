package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructures;

public class AsyncStateMachineBuilders {

    public static <S, E, C> AsyncStateMachineBuilder<S, E, C> create() {
        AsyncStateMachineBuilderFactory<S, E, C> builderFactory = new AsyncStateMachineBuilderFactoryBase<>();
        return builderFactory.create(StateMachineStructures.create());
    }

    public static <S, E, C> AsyncStateMachineBuilder<S, E, C> create(AsyncStateMachineBuilderFactory<S, E, C> builderFactory) {
        return builderFactory.create(StateMachineStructures.create());
    }
}
