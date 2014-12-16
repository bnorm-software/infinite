package com.bnorm.infinite;

/**
 * A functional interface that is used to represent a listener for state transition events.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public interface TransitionListener<S, E, C> {

    /**
     * When the listener is added to a state machine, this method is called during all the different stages of a state
     * transition.
     *
     * @param stage the state transition stage.
     * @param event the event that caused the transition.
     * @param transition the state transition that took place.
     * @param context the state machine context.
     */
    void stateTransition(TransitionStage stage, E event, Transition<? extends S, ? extends E, ? extends C> transition,
                         C context);
}
