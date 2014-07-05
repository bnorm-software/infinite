package com.bnorm.fsm4j.builders;

import java.util.function.BooleanSupplier;

import com.bnorm.fsm4j.Action;
import com.bnorm.fsm4j.InternalState;
import com.bnorm.fsm4j.Transition;

/**
 * Represents a builder of a specific state.  The interface provides methods that add behavior to the state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateBuilder<S, E, C> {

    /**
     * Returns the internal state that is being constructed.
     *
     * @return the internal state being built.
     */
    InternalState<S, E, C> getInternalState();

    /**
     * Sets the the parent of the internal state to be the specified state.  The parent state must already be partially
     * built by another state builder before it can be set as the parent of another state.
     *
     * @param state the parent state.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> childOf(S state);

    /**
     * Adds the specified action as an entry action to the internal state.
     *
     * @param action the new entry action.
     * @return the current state builder for chaining.
     */
    default StateBuilder<S, E, C> onEntry(Action<S, E, C> action) {
        getInternalState().addEntranceAction(action);
        return this;
    }

    /**
     * Adds the specified action as an exit action to the internal state.
     *
     * @param action the new exit action.
     * @return the current state builder for chaining.
     */
    default StateBuilder<S, E, C> onExit(Action<S, E, C> action) {
        getInternalState().addExitAction(action);
        return this;
    }

    /**
     * Adds the specified transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the transition.
     * @param transition the transition that will result because of the event.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> handle(E event, Transition<S> transition);

    /**
     * Adds a reentrant transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the reentrant transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> handle(E event);

    /**
     * Adds a transition to the specified state as a possible transition given the specified event.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> handle(E event, S destination);

    /**
     * Adds a reentrant transition as a possible transition given the specified event and the specified boolean supplier
     * conditional.
     *
     * @param event the event that will cause the reentrant transition.
     * @param conditional the conditional nature of the transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> handle(E event, BooleanSupplier conditional);

    /**
     * Adds a transition to the specified state as a possible transition given the specified event and the specified
     * boolean supplier conditional.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param conditional the conditional nature of the transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E, C> handle(E event, S destination, BooleanSupplier conditional);
}
