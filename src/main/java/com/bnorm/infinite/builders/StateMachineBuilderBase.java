package com.bnorm.infinite.builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.StateMachine;
import com.bnorm.infinite.StateMachineFactory;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;

/**
 * The base implementation of a state machine builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.0
 * @since 1.0.0
 */
public class StateMachineBuilderBase<S, E, C> implements StateMachineBuilder<S, E, C> {

    /** The state machine builder internal state factory. */
    private final InternalStateFactory<S, E, C> stateFactory;

    /** The state transition factory. */
    private final TransitionFactory<S, C> transitionFactory;

    /** The state machine builder state machine factory. */
    private final StateMachineFactory<S, E, C> stateMachineFactory;

    /** The state machine builder state builder factory. */
    private final StateBuilderFactory<S, E, C> configurationFactory;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S, C>>> transitions;


    /**
     * Constructs a new state machine builder from the specified internal state factory, transition factory, state
     * builder factory, and state machine factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param stateBuilderFactory the factory used to create state builders.
     * @param stateMachineFactory the factory used to create the state machine.
     */
    protected StateMachineBuilderBase(InternalStateFactory<S, E, C> internalStateFactory,
                                      TransitionFactory<S, C> transitionFactory,
                                      StateBuilderFactory<S, E, C> stateBuilderFactory,
                                      StateMachineFactory<S, E, C> stateMachineFactory) {
        this.stateFactory = internalStateFactory;
        this.transitionFactory = transitionFactory;
        this.stateMachineFactory = stateMachineFactory;
        this.configurationFactory = stateBuilderFactory;
        this.states = new LinkedHashMap<>();
        this.transitions = new LinkedHashMap<>();
    }

    @Override
    public StateBuilder<S, E, C> configure(S state) {
        InternalState<S, E, C> internal = states.computeIfAbsent(state, stateFactory::create);
        return configurationFactory.create(transitionFactory, states, transitions, internal);
    }

    @Override
    public StateMachine<S, E, C> build(S starting, C context) {
        return stateMachineFactory.create(states, transitions, starting, context);
    }
}
