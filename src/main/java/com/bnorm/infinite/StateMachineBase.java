package com.bnorm.infinite;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The base implementation of a state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class StateMachineBase<S, E, C> implements StateMachine<S, E, C> {

    /** The state machine structure. */
    private final StateMachineStructure<S, E, C> structure;

    /** The context of the state machine. */
    private final C context;

    /** The current state of the state machine. */
    private S state;

    /** The state machine transition listeners. */
    private final Set<TransitionListener<? super S, ? super E, ? super C>> listeners;

    /**
     * Constructs a new state machine from the specified state machine structure, starting state, and context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     */
    protected StateMachineBase(StateMachineStructure<S, E, C> structure, S starting, C context) {
        this.structure = structure;
        this.context = context;
        this.state = starting;
        this.listeners = new LinkedHashSet<>();
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public S getState() {
        return state;
    }

    @Override
    public void addTransitionListener(TransitionListener<? super S, ? super E, ? super C> listener) {
        listeners.add(listener);
    }

    @Override
    public Optional<Transition<S, C>> fire(E event) {
        final Set<Transition<S, C>> eventTransitions = structure.getTransitions(event);
        if (eventTransitions.isEmpty()) {
            return Optional.empty();
        }

        // To build the possible transition list, we will start with the current internal state and see if it
        // handles the specified event.  If it does not, we iterate one level up the parent chain and repeat.
        List<Transition<S, C>> possible = Collections.emptyList();
        Optional<InternalState<S, E, C>> optional = Optional.of(structure.getState(state));
        while (possible.isEmpty() && optional.isPresent()) {
            final InternalState<S, E, C> state = optional.get();
            possible = eventTransitions.stream()
                                       .filter(t -> Objects.equals(t.getSource(), state.getState()))
                                       .filter(t -> t.getGuard().allowed(getContext()))
                                       .collect(Collectors.toList());
            optional = state.getParentState();
        }

        if (possible.isEmpty()) {
            return Optional.empty();
        } else if (possible.size() > 1) {
            throw new StateMachineException("Too many possible transitions");
        }

        // Create a snapshot clone of the transition so the destination does not change each time we ask.
        Transition<S, C> transition = structure.getTransitionFactory().create(possible.get(0));
        if (structure.getState(transition.getDestination()) == null) {
            throw new StateMachineException("No internal state found for destination state in state machine");
        }

        Optional<InternalState<S, E, C>> commonAncestor;
        commonAncestor = InternalState.getCommonAncestor(structure.getState(state),
                                                         structure.getState(transition.getDestination()));

        listeners.forEach(l -> l.stateTransition(TransitionStage.Before, event, transition, context));
        structure.getState(state).exit(event, transition, context);
        state = commonAncestor.isPresent() ? commonAncestor.get().getState() : null;
        listeners.forEach(l -> l.stateTransition(TransitionStage.Between, event, transition, context));
        state = transition.getDestination();
        structure.getState(state).enter(event, transition, context);
        listeners.forEach(l -> l.stateTransition(TransitionStage.After, event, transition, context));

        return Optional.of(transition);
    }
}
