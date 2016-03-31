package com.bnorm.infinite;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

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
public class InternalState<S, E, C> {

    /** The wrapped state. */
    protected final S state;

    /** The optional parent state. */
    protected Optional<InternalState<S, E, C>> parent;

    /** The children of the state. */
    protected final Set<InternalState<S, E, C>> children;

    /** The entrance actions of the state. */
    protected final Set<Action<? super S, ? super E, ? super C>> entranceActions;

    /** The exit actions of the state. */
    protected final Set<Action<? super S, ? super E, ? super C>> exitActions;

    /**
     * Constructs a new internal state form the specified state.
     *
     * @param state the state to wrap.
     */
    protected InternalState(S state) {
        this.state = state;
        this.parent = Optional.empty();
        this.children = new LinkedHashSet<>();
        this.entranceActions = new LinkedHashSet<>();
        this.exitActions = new LinkedHashSet<>();
    }

    /**
     * Constructs a new internal state form the specified state and action comparators.  The action comparators are used
     * to sort the entrance and exit states of the internal state.
     *
     * @param state the state to wrap.
     * @param entranceComparator the entrance action comparator.
     * @param exitComparator the exit action comparator.
     */
    protected InternalState(S state, Comparator<Action<? super S, ? super E, ? super C>> entranceComparator,
                            Comparator<Action<? super S, ? super E, ? super C>> exitComparator) {
        this.state = state;
        this.parent = Optional.empty();
        this.children = new LinkedHashSet<>();
        this.entranceActions = new TreeSet<>(entranceComparator);
        this.exitActions = new TreeSet<>(exitComparator);
    }

    /**
     * The real state the internal state represents.
     *
     * @return the real state.
     */
    public S getState() {
        return state;
    }

    /**
     * Sets the parent state of the internal state.
     *
     * @param parent the parent state.
     */
    public void setParentState(InternalState<S, E, C> parent) {
        this.parent = Optional.ofNullable(parent);
    }

    /**
     * Returns the parent state of the state as an internal state.
     *
     * @return the parent state.
     */
    public Optional<InternalState<S, E, C>> getParentState() {
        return parent;
    }

    /**
     * Returns if the specified state is a parent of the internal state.  This is a recursive method and will check up
     * the parent chain as far as it can.  There is no limit to this so if there is a cyclical parent relationship the
     * method will result in a heap overflow exception.
     *
     * @param state the possible parent state.
     * @return if the specified state is a parent.
     */
    public boolean isParent(S state) {
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
    public Set<InternalState<S, E, C>> getChildrenStates() {
        return Collections.unmodifiableSet(children);
    }

    /**
     * Adds the specified internal state as a child state of the internal state.
     *
     * @param state the new child state.
     */
    public void addChild(InternalState<S, E, C> state) {
        children.add(state);
    }

    /**
     * Returns if the specified state is a child of the internal state.  This is a recursive method and will check down
     * the child chain as far as it can.  There is no limit to this so if there is a cyclical child relationship the
     * method will result in a heap overflow exception.
     *
     * @param state the possible child state.
     * @return if the specified state is a child.
     */
    public boolean isChild(S state) {
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
    public Set<Action<? super S, ? super E, ? super C>> getEntranceActions() {
        return Collections.unmodifiableSet(entranceActions);
    }

    /**
     * Adds the specified action as an entrance action to the internal state.
     *
     * @param action the new entrance action.
     */
    public void addEntranceAction(Action<? super S, ? super E, ? super C> action) {
        entranceActions.add(action);
    }

    /**
     * Performs all the entrance actions on the internal state and any possible parents that have not already been
     * entered.  If the specified transition is a reentrant transition, only the internal state's entrance actions are
     * performed.  Any parent entrance actions are performed first.
     *
     * @param event the event that caused the transition.
     * @param transition the resulting state transition.
     * @param context the state machine context.
     */
    public void enter(E event, Transition<? extends S, ? extends E, ? extends C> transition, C context) {
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
    public Set<Action<? super S, ? super E, ? super C>> getExitActions() {
        return Collections.unmodifiableSet(exitActions);
    }

    /**
     * Adds the specified action as an exit action to the internal state.
     *
     * @param action the new exit action.
     */
    public void addExitAction(Action<? super S, ? super E, ? super C> action) {
        exitActions.add(action);
    }

    /**
     * Performs all the exit actions on the internal state and any possible parents that have not already been exited.
     * If the specified transition is a reentrant transition, only the internal state's exit actions are performed.  Any
     * parent exit actions are performed last.
     *
     * @param event the event that caused the transition.
     * @param transition the resulting state transition.
     * @param context the state machine context.
     */
    public void exit(E event, Transition<? extends S, ? extends E, ? extends C> transition, C context) {
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
