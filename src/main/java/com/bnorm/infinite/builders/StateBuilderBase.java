package com.bnorm.infinite.builders;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.StateMachineException;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.TransitionGuard;

/**
 * The base implementation of a state builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.0
 * @since 1.0.0
 */
public class StateBuilderBase<S, E, C> implements StateBuilder<S, E, C> {

    /** The state machine state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The state machine event to transition map. */
    private final Map<E, Set<Transition<S, C>>> transitions;

    /** The internal state being built. */
    private final InternalState<S, E, C> state;

    /** The state transition factory. */
    private final TransitionFactory transitionFactory;

    /**
     * Constructs a new state builder from the specified transition factory, state map, transition map, and internal
     * state being built.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     */
    protected StateBuilderBase(TransitionFactory transitionFactory, Map<S, InternalState<S, E, C>> states,
                               Map<E, Set<Transition<S, C>>> transitions, InternalState<S, E, C> state) {
        this.states = states;
        this.transitions = transitions;
        this.state = state;
        this.transitionFactory = transitionFactory;
    }

    @Override
    public InternalState<S, E, C> getInternalState() {
        return state;
    }

    @Override
    public StateBuilderBase<S, E, C> childOf(S parent) {
        InternalState<S, E, C> internalParent = states.get(parent);
        if (internalParent != null) {
            internalParent.addChild(getInternalState());
            getInternalState().setParentState(internalParent);
            return this;
        } else {
            throw new StateMachineException(
                    "Requested parent state [" + parent + "] does not exist in the state machine." +
                            "  Configure parent states before configuring children states.");
        }
    }

    @Override
    public StateBuilderBase<S, E, C> onEntry(Action<S, E, C> action) {
        StateBuilder.super.onEntry(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> onExit(Action<S, E, C> action) {
        StateBuilder.super.onExit(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Transition<S, C> transition) {
        if (!transition.getSource().equals(getInternalState().getState())) {
            throw new StateMachineException(
                    "Illegal transition source.  Should be [" + getInternalState().getState() + "] Is [" + transition.getSource() + "]");
        }
        Set<Transition<S, C>> handlers = transitions.computeIfAbsent(event, e -> new LinkedHashSet<>());
        handlers.add(transition);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event) {
        return handle(event, transitionFactory.create(getInternalState().getState(), getInternalState().getState()));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination) {
        return handle(event, transitionFactory.create(getInternalState().getState(), destination));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, TransitionGuard<C> guard) {
        return handle(event, transitionFactory.create(getInternalState().getState(), getInternalState().getState(),
                                                      guard));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination, TransitionGuard<C> guard) {
        return handle(event, transitionFactory.create(getInternalState().getState(), destination, guard));
    }
}
