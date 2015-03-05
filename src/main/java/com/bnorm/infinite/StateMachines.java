package com.bnorm.infinite;

public class StateMachines {

    public static <S, E, C> StateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting,
                                                         C context) {
        return new StateMachineBase<>(structure, starting, context);
    }
}
