package com.bnorm.fsm4j;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * The base implementation of a state builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public class StateBuilderBase<S extends State, E extends Event, C extends Context> implements StateBuilder<S, E, C> {

    /** The state machine state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The state machine event to transition map. */
    private final Map<E, Set<Transition<S>>> transitions;

    /** The internal state being built. */
    private final InternalState<S, E, C> state;

    /**
     * Constructs a new state builder from the specified state map, transition map, and internal state being built.
     *
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     */
    protected StateBuilderBase(Map<S, InternalState<S, E, C>> states, Map<E, Set<Transition<S>>> transitions,
                               InternalState<S, E, C> state) {
        this.states = states;
        this.transitions = transitions;
        this.state = state;
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
    public StateBuilderBase<S, E, C> handle(E event, Transition<S> transition) {
        if (!transition.getSource().equals(getInternalState().getState())) {
            throw new StateMachineException(
                    "Illegal transition source.  Should be [" + getInternalState().getState() + "] Is [" + transition.getSource() + "]");
        }
        Set<Transition<S>> handlers = transitions.computeIfAbsent(event, e -> new LinkedHashSet<>());
        handlers.add(transition);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event) {
        return handle(event, new TransitionBase<>(getInternalState().getState(), getInternalState().getState()));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination) {
        return handle(event, new TransitionBase<>(getInternalState().getState(), destination));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, BooleanSupplier conditional) {
        return handle(event,
                      new TransitionBase<>(getInternalState().getState(), getInternalState().getState(), conditional));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination, BooleanSupplier conditional) {
        return handle(event, new TransitionBase<>(getInternalState().getState(), destination, conditional));
    }
}
