package com.bnorm.infinite;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The base implementation of a state machine structure.
 *
 * @param <S>
 * @param <E>
 * @param <C>
 * @author Brian Norman
 * @since 1.1.0
 */
public class StateMachineStructureBase<S, E, C> implements StateMachineStructure<S, E, C> {

    /** The state machine internal state factory. */
    private final InternalStateFactory<S, E, C> internalStateFactory;

    /** The state transition factory. */
    private final TransitionFactory<S, C> transitionFactory;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S, C>>> transitions;

    /**
     * Constructs a new state machine structure base from the specified internal state factory and transition factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     */
    protected StateMachineStructureBase(InternalStateFactory<S, E, C> internalStateFactory,
                                        TransitionFactory<S, C> transitionFactory) {
        this.internalStateFactory = internalStateFactory;
        this.transitionFactory = transitionFactory;
        this.states = Collections.synchronizedMap(new HashMap<>());
        this.transitions = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public InternalStateFactory<S, E, C> getInternalStateFactory() {
        return internalStateFactory;
    }

    @Override
    public InternalState<S, E, C> getState(S state) {
        return states.computeIfAbsent(state, internalStateFactory::create);
    }

    @Override
    public TransitionFactory<S, C> getTransitionFactory() {
        return transitionFactory;
    }

    @Override
    public Set<Transition<S, C>> getTransitions(E event) {
        return Collections.unmodifiableSet(getTransitionsUnsafe(event));
    }

    @Override
    public void addTransition(E event, Transition<S, C> transition) {
        getTransitionsUnsafe(event).add(transition);
    }

    /**
     * Returns a modifiable set of transitions associated with the specified event.
     *
     * @param event the state machine event.
     * @return the associated transitions.
     */
    private Set<Transition<S, C>> getTransitionsUnsafe(E event) {
        return transitions.computeIfAbsent(event, e -> Collections.synchronizedSet(new HashSet<>()));
    }
}
