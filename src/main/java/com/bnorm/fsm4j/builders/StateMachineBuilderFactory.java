package com.bnorm.fsm4j.builders;

import com.bnorm.fsm4j.Context;
import com.bnorm.fsm4j.Event;
import com.bnorm.fsm4j.InternalStateFactory;
import com.bnorm.fsm4j.State;
import com.bnorm.fsm4j.StateMachineFactory;
import com.bnorm.fsm4j.TransitionFactory;

/**
 * A factory interface for state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateMachineBuilderFactory<S extends State, E extends Event, C extends Context> {

    /**
     * Creates the default state machine builder factory.  This is the state machine builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder factory.
     */
    static <S extends State, E extends Event, C extends Context> StateMachineBuilderFactory<S, E, C> getDefault() {
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
    static <S extends State, E extends Event, C extends Context> StateMachineBuilder<S, E, C> create() {
        return StateMachineBuilderFactory.<S, E, C>getDefault()
                                         .create(InternalStateFactory.getDefault(), TransitionFactory.getDefault(),
                                                 StateMachineFactory.getDefault(), StateBuilderFactory.getDefault());
    }

    /**
     * Creates a state machine builder from the specified internal state factory, transition factory, state machine
     * factory, and state builder factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param stateMachineFactory the factory used to create the state machine.
     * @param stateBuilderFactory the factory used to create state builders.
     * @return a state machine builder.
     */
    StateMachineBuilder<S, E, C> create(InternalStateFactory internalStateFactory,
                                        TransitionFactory<S> transitionFactory,
                                        StateMachineFactory<S, E, C> stateMachineFactory,
                                        StateBuilderFactory<S, E, C> stateBuilderFactory);
}
