package com.bnorm.infinite;

import java.util.function.Supplier;

public class TransitionFactoryBase<S, E, C> implements TransitionFactory<S, E, C> {

    @Override
    public Transition<S, E, C> create(S source, Supplier<S> destination, TransitionGuard<C> guard,
                                      Action<S, E, C> action) {
        return new TransitionBase<>(source, destination, guard, action);
    }
}
