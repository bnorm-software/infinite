package com.bnorm.infinite;

public class InternalStateFactoryBase<S, E, C> implements InternalStateFactory<S, E, C> {

    @Override
    public InternalState<S, E, C> create(S state) {
        return new InternalStateBase<>(state);
    }
}
