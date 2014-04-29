package com.bnorm.fsm4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The base implementation of a state builder.
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
class StateBuilderBase<S extends State, E extends Event> implements StateBuilder<S, E> {

    /** The state machine state to internal state map. */
    private final Map<S, InternalState<S, E>> states;

    /** The state machine event to transition map. */
    private final Map<E, Set<Transition<S>>> transitions;

    /** The internal state being built. */
    private final InternalState<S, E> state;

    /**
     * Constructs a new state builder from the specified state map, transition map, and internal state being built.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     */
    public StateBuilderBase(Map<S, InternalState<S, E>> states, Map<E, Set<Transition<S>>> transitions,
                            InternalState<S, E> state) {
        this.states = states;
        this.transitions = transitions;
        this.state = state;
    }

    @Override
    public InternalState<S, E> getInternalState() {
        return state;
    }

    @Override
    public StateBuilderBase<S, E> childOf(S parent) {
        InternalState<S, E> internalParent = states.get(parent);
        internalParent.addChild(getInternalState());
        getInternalState().setParentState(internalParent);
        return this;
    }

    @Override
    public StateBuilderBase<S, E> onEntry(Action<S, E> action) {
        StateBuilder.super.onEntry(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E> onExit(Action<S, E> action) {
        StateBuilder.super.onExit(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E> handle(E event, Transition<S> transition) {
        if (!transition.getSource().equals(getInternalState().getState())) {
            throw new RuntimeException("Illegal transition source");
        }
        Set<Transition<S>> handlers = transitions.get(event);
        if (handlers == null) {
            handlers = new HashSet<>();
            transitions.put(event, handlers);
        }
        handlers.add(transition);
        return this;
    }

    @Override
    public StateBuilderBase<S, E> handle(E event) {
        return handle(event, new TransitionBase<>(getInternalState().getState(), getInternalState().getState()));
    }

    @Override
    public StateBuilderBase<S, E> handle(E event, S destination) {
        return handle(event, new TransitionBase<>(getInternalState().getState(), destination));
    }
}
