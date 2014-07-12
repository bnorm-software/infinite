package com.bnorm.infinite;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
 * @version 1.0.0
 * @since 1.0.0
 */
public class StateMachineBase<S, E, C> implements StateMachine<S, E, C> {

    /** The factory used to create transitions. */
    private final TransitionFactory<S, C> transitionFactory;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S, C>>> transitions;

    /** The context of the state machine. */
    private final C context;

    /** The current state of the state machine. */
    private S state;

    /** The state machine transition listeners. */
    private final Set<TransitionListener<? super S, ? super E, ? super C>> listeners;

    /**
     * Constructs a new state machine from the specified state map, transition map, starting state, and context.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param context the state machine context.
     * @param starting the starting state of the state machine.
     */
    protected StateMachineBase(TransitionFactory<S, C> transitionFactory, Map<S, InternalState<S, E, C>> states,
                               Map<E, Set<Transition<S, C>>> transitions, C context, S starting) {
        this.transitionFactory = transitionFactory;
        this.states = states;
        this.transitions = transitions;
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
        List<Transition<S, C>> possible = Collections.emptyList();

        // To build the possible transition list, we will start with the current internal state and see if it
        // handles the specified event.  If it does not, we iterate one level up the parent chain and repeat.
        Optional<InternalState<S, E, C>> optional = Optional.of(states.get(state));
        final Set<Transition<S, C>> eventTransitions = transitions.getOrDefault(event, Collections.emptySet());
        while (possible.isEmpty() && optional.isPresent()) {
            final InternalState<S, E, C> state = optional.get();
            possible = eventTransitions.stream()
                                       .filter(t -> t.getSource().equals(state.getState()))
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
        Transition<S, C> transition = transitionFactory.create(possible.get(0));
        if (states.get(transition.getDestination()) == null) {
            throw new StateMachineException("No internal state found for destination state in state machine");
        }

        states.get(state).exit(event, transition, context);
        state = transition.getDestination();
        states.get(state).enter(event, transition, context);

        listeners.forEach(l -> l.stateTransition(event, transition, context));
        return Optional.of(transition);
    }
}
