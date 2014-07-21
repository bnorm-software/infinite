package com.bnorm.infinite.builders;

import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncStateMachineFactory;

/**
 * A factory interface for asynchronous state machine builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncStateMachineBuilderFactory<S, E, C> {

    /**
     * Creates the default asynchronous state machine builder factory.  This is the asynchronous state machine builder
     * base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder factory.
     */
    static <S, E, C> AsyncStateMachineBuilderFactory<S, E, C> getDefault() {
        return AsyncStateMachineBuilderBase::new;
    }

    /**
     * Creates the default asynchronous state machine builder.  This is a combination of the default internal state
     * factory, transition factory, asynchronous action factory, asynchronous state builder factory, and asynchronous
     * state machine factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine builder.
     */
    static <S, E, C> AsyncStateMachineBuilder<S, E, C> create() {
        return AsyncStateMachineBuilderFactory.<S, E, C>getDefault()
                                              .create(InternalStateFactory.getDefault(), TransitionFactory.getDefault(),
                                                      AsyncActionFactory.getDefault(),
                                                      AsyncStateBuilderFactory.getDefault(),
                                                      AsyncStateMachineFactory.getDefault());
    }

    /**
     * Creates a state machine builder from the specified internal state factory, transition factory, asynchronous
     * action factory, asynchronous state builder factory, and asynchronous state machine factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @param asyncStateBuilderFactory the factory used to create asynchronous state builders.
     * @param asyncStateMachineFactory the factory used to create the asynchronous state machine.
     * @return a asynchronous state machine builder.
     */
    AsyncStateMachineBuilder<S, E, C> create(InternalStateFactory<S, E, C> internalStateFactory,
                                             TransitionFactory<S, C> transitionFactory,
                                             AsyncActionFactory<S, E, C> asyncActionFactory,
                                             AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory,
                                             AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory);
}
