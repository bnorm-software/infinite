package com.bnorm.fsm4j;

/**
 * A functional interface that is used to represent a listener for state transition events.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
@FunctionalInterface
public interface TransitionListener<S, E, C> {

    /**
     * When the listener is added to a state machine, this method is called whenever there is a state transition.
     *
     * @param event the event that caused the transition.
     * @param transition the state transition that took place.
     * @param context the state machine context.
     */
    void stateTransition(E event, Transition<? extends S> transition, C context);
}
