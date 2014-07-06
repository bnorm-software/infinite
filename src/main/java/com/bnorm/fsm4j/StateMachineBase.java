package com.bnorm.fsm4j;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * The base implementation of a state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public class StateMachineBase<S, E, C> implements StateMachine<S, E, C> {

    /** The state machine transition listeners. */
    private final Set<TransitionListener<? super S, ? super E, ? super C>> listeners;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S>>> transitions;

    /** The current state of the state machine. */
    private S state;

    /** The context of the state machine. */
    private final C context;

    /**
     * Constructs a new state machine from the specified state map, transition map, starting state, and context.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     */
    protected StateMachineBase(Map<S, InternalState<S, E, C>> states, Map<E, Set<Transition<S>>> transitions,
                               S starting, C context) {
        this.listeners = new LinkedHashSet<>();
        this.states = states;
        this.transitions = transitions;
        setState(starting);
        this.context = context;
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
    public C getContext() {
        return context;
    }

    @Override
    public InternalState<S, E, C> getInternalState(S state) {
        return states.get(state);
    }

    @Override
    public Set<Transition<S>> getTransitions(E event) {
        return Collections.unmodifiableSet(transitions.getOrDefault(event, Collections.emptySet()));
    }

    @Override
    public Set<TransitionListener<? super S, ? super E, ? super C>> getTransitionListeners() {
        return Collections.unmodifiableSet(listeners);
    }

    @Override
    public void addTransitionListener(TransitionListener<? super S, ? super E, ? super C> listener) {
        listeners.add(listener);
    }
}
