package com.bnorm.infinite;

/**
 * The base implementation of an state machine factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class StateMachineFactoryBase<S, E, C> implements StateMachineFactory<S, E, C> {

    @Override
    public StateMachine<S, E, C> create(StateMachineStructure<S, E, C> structure, S starting, C context) {
        return new StateMachineBase<>(structure, starting, context);
    }
}
