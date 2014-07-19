package com.bnorm.infinite.builders;

import com.bnorm.infinite.async.AsyncStateMachine;

/**
 * Asynchronous state machine builder interface.  An asynchronous state machine builder provides access to configuring
 * each state and also for creating the final asynchronous state machine.  Since the builder and the state machine share
 * the same backend it is possible to build the asynchronous state machine before configuring any states.  This allows
 * the state machine itself to be reference from any entrance or exit actions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncStateMachineBuilder<S, E, C> extends StateMachineBuilder<S, E, C> {

    /**
     * Returns an asynchronous state builder that can be used to configure the specified state.
     *
     * @param state the state to configure.
     * @return the state builder for the state.
     */
    AsyncStateBuilder<S, E, C> configure(S state);

    /**
     * Builds and returns the asynchronous state machine the asynchronous state machine builder is constructing.
     *
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     * @return the state machine.
     */
    AsyncStateMachine<S, E, C> build(S starting, C context);
}
