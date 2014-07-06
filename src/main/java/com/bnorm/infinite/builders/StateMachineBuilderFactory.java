package com.bnorm.infinite.builders;

import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.TransitionFactory;

/**
 * A factory interface for state machine builders.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineBuilderFactory {

    /**
     * Creates the default state machine builder factory.  This is the state machine builder base constructor.
     *
     * @return default state machine builder factory.
     */
    static StateMachineBuilderFactory getDefault() {
        return StateMachineBuilderBase::new;
    }

    /**
     * Creates the default state machine builder.  This is a combination of the default state builder factory and
     * internal state factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder.
     */
    static <S, E, C> StateMachineBuilder<S, E, C> create() {
        return StateMachineBuilderFactory.<S, E, C>getDefault()
                                         .create(InternalStateFactory.getDefault(), TransitionFactory.getDefault(),
                                                 StateBuilderFactory.getDefault(), StateMachineFactory.getDefault());
    }

    /**
     * Creates a state machine builder from the specified internal state factory, transition factory, state builder
     * factory, and state machine factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param stateBuilderFactory the factory used to create state builders.
     * @param stateMachineFactory the factory used to create the state machine.
     * @return a state machine builder.
     */
    <S, E, C> StateMachineBuilder<S, E, C> create(InternalStateFactory internalStateFactory,
                                                  TransitionFactory transitionFactory,
                                                  StateBuilderFactory stateBuilderFactory,
                                                  StateMachineFactory stateMachineFactory);
}
