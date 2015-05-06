package com.bnorm.infinite;

import java.util.function.Supplier;

/**
 * The base implementation of a transition factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class TransitionFactoryBase<S, E, C> implements TransitionFactory<S, E, C> {

    @Override
    public Transition<S, E, C> create(S source, Supplier<S> destination, TransitionGuard<C> guard,
                                      Action<S, E, C> action) {
        return new TransitionBase<>(source, destination, guard, action);
    }
}
