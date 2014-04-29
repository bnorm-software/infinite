package com.bnorm.fsm4j;

/**
 * Represents a builder of a specific state.  The interface provides methods that add behavior to the state machine.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface StateBuilder<S extends State, E extends Event> {

    /**
     * Returns the internal state that is being constructed.
     *
     * @return the internal state being built.
     */
    InternalState<S, E> getInternalState();

    /**
     * Sets the the parent of the internal state to be the specified state.  The parent state must already be partially
     * built by another state builder before it can be set as the parent of another state.
     *
     * @param state the parent state.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> childOf(S state);

    /**
     * Adds the specified action as an entry action to the internal state.
     *
     * @param action the new entry action.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> onEntry(Action<S, E> action);

    /**
     * Adds the specified action as an exit action to the internal state.
     *
     * @param action the new exit action.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> onExit(Action<S, E> action);

    /**
     * Adds the specified transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the transition.
     * @param transition the transition that will result because of the event.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> handle(E event, Transition<S> transition);

    /**
     * Adds a reentrant transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the reentrant transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> handle(E event);

    /**
     * Adds a transition to the specified state as a possible transition given the specified event.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @return the current state builder for chaining.
     */
    StateBuilder<S, E> handle(E event, S destination);
}
