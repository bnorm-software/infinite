package com.bnorm.infinite;

public class StateMachineStructures {

    public static <S, E, C> StateMachineStructure<S, E, C> create() {
        StateMachineStructureFactory<S, E, C> structureFactory = new StateMachineStructureFactoryBase<>();
        return structureFactory.create();
    }
}
