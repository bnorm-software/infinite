package com.bnorm.fsm4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The base implementation of a state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
class StateMachineBase<S extends State, E extends Event> implements StateMachine<S, E> {

    /** The state machine transition listeners. */
    private final Set<TransitionListener<S, E>> listeners;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S>>> transitions;

    /** The current state of the state machine. */
    private S state;

    /**
     * Constructs a new state machine from the specified state map, transition map, and starting state.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param starting the starting state of the state machine.
     */
    StateMachineBase(Map<S, InternalState<S, E>> states, Map<E, Set<Transition<S>>> transitions, S starting) {
        this.listeners = new HashSet<>();
        this.states = states;
        this.transitions = transitions;
        setState(starting);
    }

    @Override
    public void setState(S state) {
        this.state = state;
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public InternalState<S, E> getInternalState(S state) {
        return states.get(state);
    }

    @Override
    public Set<Transition<S>> getTransitions(E event) {
        return transitions.computeIfAbsent(event, e -> new HashSet<>());
    }

    @Override
    public Set<TransitionListener<S, E>> getTransitionListeners() {
        return listeners;
    }

    @Override
    public void addTransitionListener(TransitionListener<S, E> listener) {
        listeners.add(listener);
    }
}
