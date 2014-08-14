package com.bnorm.infinite;

import java.util.Objects;
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
 * @since 1.0.0
 */
public interface InternalState<S, E, C> {

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
            return Objects.equals(parent.getState(), state) || parent.isParent(state);
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
        // Check all children...
        return getChildrenStates().stream().anyMatch(c -> Objects.equals(c.getState(), state))
                // ... before checking recursion to check children's children.
                || getChildrenStates().parallelStream().anyMatch(c -> c.isChild(state));
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
    default void enter(E event, Transition<S, C> transition, C context) {
        if (transition.isReentrant()) {
            getEntranceActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
        } else if (!isChild(transition.getSource()) && !Objects.equals(getState(), transition.getSource())) {
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
    default void exit(E event, Transition<S, C> transition, C context) {
        if (transition.isReentrant()) {
            getExitActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
        } else if (!isChild(transition.getDestination()) && !Objects.equals(getState(), transition.getDestination())) {
            getExitActions().stream().forEach(a -> a.perform(getState(), event, transition, context));
            getParentState().ifPresent(s -> s.exit(event, transition, context));
        }
    }


    // =========================== //
    // **** Static Utilities ***** //
    // =========================== //

    /**
     * Returns the first common ancestor of the two specified internal statues.  If one of the internal states is the
     * parent of the other, that internal state is returned.
     *
     * @param state1 the first state to find the common ancestor.
     * @param state2 the second state to find the common ancestor.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return the first common ancestor.
     */
    static <S, E, C> Optional<InternalState<S, E, C>> getCommonAncestor(InternalState<S, E, C> state1,
                                                                        InternalState<S, E, C> state2) {
        InternalState<S, E, C> commonParent = null;
        if (state1.isParent(state2.getState())) {
            commonParent = state2;
        } else if (state2.isParent(state1.getState())) {
            commonParent = state1;
        } else if (state2.getParentState().isPresent()) {
            InternalState<S, E, C> parentOfNext = state2.getParentState().get();
            while (!state1.isParent(parentOfNext.getState()) && parentOfNext.getParentState().isPresent()) {
                parentOfNext = parentOfNext.getParentState().get();
            }
            if (state1.isParent(parentOfNext.getState())) {
                commonParent = parentOfNext;
            }
        }
        return Optional.ofNullable(commonParent);
    }
}
