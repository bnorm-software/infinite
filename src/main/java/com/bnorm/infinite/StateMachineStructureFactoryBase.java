package com.bnorm.infinite;

public class StateMachineStructureFactoryBase<S, E, C> implements StateMachineStructureFactory<S, E, C> {

    protected final InternalStateFactory<S, E, C> internalStateFactory;
    protected final TransitionFactory<S, E, C> transitionFactory;

    public StateMachineStructureFactoryBase(InternalStateFactory<S, E, C> internalStateFactory,
                                            TransitionFactory<S, E, C> transitionFactory) {
        this.internalStateFactory = internalStateFactory;
        this.transitionFactory = transitionFactory;
    }

    public StateMachineStructureFactoryBase() {
        this(new InternalStateFactoryBase<>(), new TransitionFactoryBase<>());
    }

    @Override
    public StateMachineStructure<S, E, C> create() {
        return new StateMachineStructureBase<>(internalStateFactory, transitionFactory);
    }
}
