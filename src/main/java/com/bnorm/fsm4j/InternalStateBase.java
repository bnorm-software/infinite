package com.bnorm.fsm4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The base implementation of an internal state.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public class InternalStateBase<S extends State, E extends Event> implements InternalState<S, E> {

    /** The wrapped state. */
    private final S state;

    /** The optional parent state. */
    private Optional<InternalState<S, E>> parent;

    /** The children of the state. */
    private final Set<InternalState<S, E>> children;

    /** The entrance actions of the state. */
    private final Set<Action<? super S, ? super E>> entranceActions;

    /** The exit actions of the state. */
    private final Set<Action<? super S, ? super E>> exitActions;

    /**
     * Constructs a new internal state form the specified state.
     *
     * @param state the state to wrap.
     */
    protected InternalStateBase(S state) {
        this.state = state;
        this.parent = Optional.empty();
        this.children = new HashSet<>();
        this.entranceActions = new HashSet<>();
        this.exitActions = new HashSet<>();
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public void setParentState(InternalState<S, E> parent) {
        this.parent = Optional.ofNullable(parent);
    }

    @Override
    public Optional<InternalState<S, E>> getParentState() {
        return parent;
    }

    @Override
    public Set<InternalState<S, E>> getChildrenStates() {
        return Collections.unmodifiableSet(children);
    }

    @Override
    public void addChild(InternalState<S, E> state) {
        children.add(state);
    }

    @Override
    public Set<Action<? super S, ? super E>> getEntranceActions() {
        return Collections.unmodifiableSet(entranceActions);
    }

    @Override
    public void addEntranceAction(Action<? super S, ? super E> action) {
        entranceActions.add(action);
    }

    @Override
    public Set<Action<? super S, ? super E>> getExitActions() {
        return Collections.unmodifiableSet(exitActions);
    }

    @Override
    public void addExitAction(Action<? super S, ? super E> action) {
        exitActions.add(action);
    }
}
