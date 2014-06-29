package com.bnorm.fsm4j;

import java.util.Optional;
import java.util.Set;

/**
 * Represents the internal state of a state machine.  This interface contains the parent-child relationship between
 * states and also the entrance and exit actions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface InternalState<S extends State, E extends Event, C extends Context> {

    /**
     * The real state the internal state represents.
     *
     * @return the real state.
     */
    S getState();

    /**
     * Sets the parent state of the internal state.
     *
     * @param parent the parent state.
     */
    void setParentState(InternalState<S, E, C> parent);

    /**
     * Returns the parent state of the state as an internal state.
     *
     * @return the parent state.
     */
    Optional<InternalState<S, E, C>> getParentState();

    /**
     * Returns if the specified state is a parent of the internal state.  This is a recursive method and will check up
     * the parent chain as far as it can.  There is no limit to this so if there is a cyclical parent relationship the
     * method will result in a heap overflow exception.
     *
     * @param state the possible parent state.
     * @return if the specified state is a parent.
     */
    default boolean isParent(S state) {
        if (getParentState().isPresent()) {
            InternalState<S, E, C> parent = getParentState().get();
            return parent.getState().equals(state) || parent.isParent(state);
        } else {
            return false;
        }
    }

    /**
     * Returns all the child internal states of the internal state.
     *
     * @return all child states.
     */
    Set<InternalState<S, E, C>> getChildrenStates();

    /**
     * Adds the specified internal state as a child state of the internal state.
     *
     * @param state the new child state.
     */
    void addChild(InternalState<S, E, C> state);

    /**
     * Returns if the specified state is a child of the internal state.  This is a recursive method and will check down
     * the child chain as far as it can.  There is no limit to this so if there is a cyclical child relationship the
     * method will result in a heap overflow exception.
     *
     * @param state the possible child state.
     * @return if the specified state is a child.
     */
    default boolean isChild(S state) {
        return getChildrenStates().stream().anyMatch(c -> c.getState().equals(state) || c.isChild(state));
    }

    /**
     * Returns all the entrance actions that are performed when the internal state is entered.
     *
     * @return all entrance actions.
     */
    Set<Action<? super S, ? super E, ? super C>> getEntranceActions();

    /**
     * Adds the specified action as an entrance action to the internal state.
     *
     * @param action the new entrance action.
     */
    void addEntranceAction(Action<? super S, ? super E, ? super C> action);

    /**
     * Performs all the entrance actions on the internal state and any possible parents that have not already been
     * entered.  If the specified transition is a reentrant transition, only the internal state's entrance actions are
     * performed.  Any parent entrance actions are performed first.
     *
     * @param event the event that caused the transition.
     * @param transition the resulting state transition.
     * @param context the state machine context.
     */
    default void enter(E event, Transition<S> transition, C context) {
        if (transition.isReentrant()) {
            getEntranceActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
        } else if (!isChild(transition.getSource()) && !getState().equals(transition.getSource())) {
            getParentState().ifPresent(s -> s.enter(event, transition, context));
            getEntranceActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
        }
    }

    /**
     * Returns all the exit actions that are performed when the internal state is exited.
     *
     * @return all exit actions.
     */
    Set<Action<? super S, ? super E, ? super C>> getExitActions();

    /**
     * Adds the specified action as an exit action to the internal state.
     *
     * @param action the new exit action.
     */
    void addExitAction(Action<? super S, ? super E, ? super C> action);

    /**
     * Performs all the exit actions on the internal state and any possible parents that have not already been exited.
     * If the specified transition is a reentrant transition, only the internal state's exit actions are performed.  Any
     * parent exit actions are performed last.
     *
     * @param event the event that caused the transition.
     * @param transition the resulting state transition.
     * @param context the state machine context.
     */
    default void exit(E event, Transition<S> transition, C context) {
        if (transition.isReentrant()) {
            getExitActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
        } else if (!isChild(transition.getDestination()) && !getState().equals(transition.getDestination())) {
            getExitActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
            getParentState().ifPresent(s -> s.exit(event, transition, context));
        }
    }
}
