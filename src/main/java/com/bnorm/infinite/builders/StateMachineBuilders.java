package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructures;

public class StateMachineBuilders {

    public static <S, E, C> StateMachineBuilder<S, E, C> create() {
        StateMachineBuilderFactory<S, E, C> builderFactory = new StateMachineBuilderFactoryBase<>();
        return builderFactory.create(StateMachineStructures.create());
    }

    public static <S, E, C> StateMachineBuilder<S, E, C> create(StateMachineBuilderFactory<S, E, C> builderFactory) {
        return builderFactory.create(StateMachineStructures.create());
    }
}
