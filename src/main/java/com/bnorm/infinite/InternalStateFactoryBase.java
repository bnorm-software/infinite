package com.bnorm.infinite;

/**
 * The base implementation of an internal state factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class InternalStateFactoryBase<S, E, C> implements InternalStateFactory<S, E, C> {

    @Override
    public InternalState<S, E, C> create(S state) {
        return new InternalStateBase<>(state);
    }
}
