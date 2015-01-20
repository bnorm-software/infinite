package com.bnorm.infinite;

import java.util.Optional;

/**
 * The operational state machine interface.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public interface StateMachine<S, E, C> {

    /**
     * Returns the context of the state machine.
     *
     * @return the context.
     */
    C getContext();

    /**
     * Returns the current state of the state machine.
     *
     * @return the current state.
     */
    S getState();

    /**
     * Adds the specified transition listener to the state machine.
     *
     * @param listener the new transition listener.
     */
    void addTransitionListener(TransitionListener<? super S, ? super E, ? super C> listener);

    /**
     * Fires the specified event.  This is how states are transitioned.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    Optional<Transition<S, E, C>> fire(E event);
}
