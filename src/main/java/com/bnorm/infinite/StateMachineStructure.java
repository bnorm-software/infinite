package com.bnorm.infinite;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * A structure class representing states and transitions between them.  This structure does not have any state machine
 * like behavior, just a representation of a state machine.  This structure can be safely used between multiple state
 * machines which reduces the memory requirements for having many state machines of the same structure.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public class StateMachineStructure<S, E, C> {

    /** The state to internal state map. */
    protected final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    protected final Map<E, Set<Transition<S, E, C>>> transitions;

    /** todo */
    protected final Function<S, InternalState<S, E, C>> internalStateFunction;

    /**
     * Constructs a new state machine structure base from the specified internal state factory and transition factory.
     */
    public StateMachineStructure() {
        this.states = Collections.synchronizedMap(new HashMap<>());
        this.transitions = Collections.synchronizedMap(new HashMap<>());
        this.internalStateFunction = InternalState::new;
    }

    /**
     * Constructs a new state machine structure base from the specified internal state factory and transition factory.
     *
     * @param actionComparator
     */
    public StateMachineStructure(Comparator<Action<? super S, ? super E, ? super C>> actionComparator) {
        this(actionComparator, actionComparator);
    }

    /**
     * Constructs a new state machine structure base from the specified internal state factory and transition factory.
     *
     * @param entranceComparator
     * @param exitComparator
     */
    public StateMachineStructure(Comparator<Action<? super S, ? super E, ? super C>> entranceComparator,
                                 Comparator<Action<? super S, ? super E, ? super C>> exitComparator) {
        this.states = Collections.synchronizedMap(new HashMap<>());
        this.transitions = Collections.synchronizedMap(new HashMap<>());
        this.internalStateFunction = state -> new InternalState<>(state, entranceComparator, exitComparator);
    }

    /**
     * Returns the internal state associated with the specified state.  This method should never return a {@code null}
     * internal state.  If needed, the structure should use the internal state factory and create a new internal state.
     *
     * @param state the state machine state.
     * @return the associated internal state.
     */
    public InternalState<S, E, C> getState(S state) {
        return states.computeIfAbsent(state, internalStateFunction);
    }

    /**
     * Returns the transitions associated with the specified event.  This method should never return a {@code null} set
     * of transitions.  If needed, the structure should create a new, empty set.
     *
     * @param event the state machine event.
     * @return the associated transitions.
     */
    public Set<Transition<S, E, C>> getTransitions(E event) {
        return Collections.unmodifiableSet(getTransitionsUnsafe(event));
    }

    /**
     * Adds the specified transition as a possible transition for the specified event.
     *
     * @param event the state machine event.
     * @param transition the event transition.
     */
    public void addTransition(E event, Transition<S, E, C> transition) {
        getTransitionsUnsafe(event).add(transition);
    }

    /**
     * Returns a modifiable set of transitions associated with the specified event.
     *
     * @param event the state machine event.
     * @return the associated transitions.
     */
    private Set<Transition<S, E, C>> getTransitionsUnsafe(E event) {
        return transitions.computeIfAbsent(event, e -> Collections.synchronizedSet(new HashSet<>()));
    }
}
