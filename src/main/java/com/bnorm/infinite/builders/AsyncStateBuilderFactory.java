package com.bnorm.infinite.builders;

import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.async.AsyncActionFactory;

/**
 * A factory interface for asynchronous state builders.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncStateBuilderFactory<S, E, C> {

    /**
     * Returns the default asynchronous state builder factory.  This is the state builder base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state builder factory.
     */
    static <S, E, C> AsyncStateBuilderFactory<S, E, C> getDefault() {
        return AsyncStateBuilderBase::new;
    }


    /**
     * Creates a new state builder from the specified transition factory, asynchronous action factory, state map,
     * transition map, and internal state being built.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     * @return a new state builder.
     */
    AsyncStateBuilder<S, E, C> create(TransitionFactory<S, C> transitionFactory,
                                      AsyncActionFactory<S, E, C> asyncActionFactory,
                                      Map<S, InternalState<S, E, C>> states, Map<E, Set<Transition<S, C>>> transitions,
                                      InternalState<S, E, C> state);
}
