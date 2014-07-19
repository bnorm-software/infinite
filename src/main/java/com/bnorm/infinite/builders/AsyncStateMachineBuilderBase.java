package com.bnorm.infinite.builders;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.async.AsyncActionFactory;
import com.bnorm.infinite.async.AsyncStateMachine;
import com.bnorm.infinite.async.AsyncStateMachineFactory;

/**
 * The base implementation of an asynchronous state machine builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class AsyncStateMachineBuilderBase<S, E, C> implements AsyncStateMachineBuilder<S, E, C> {

    /** The state machine builder internal state factory. */
    private final InternalStateFactory<S, E, C> stateFactory;

    /** The state transition factory. */
    private final TransitionFactory<S, C> transitionFactory;

    /** the asynchronous action factory. */
    private final AsyncActionFactory<S, E, C> asyncActionFactory;

    /** The asynchronous state machine builder state builder factory. */
    private final AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory;

    /** The asynchronous state machine builder state machine factory. */
    private final AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory;

    /** The state to internal state map. */
    private final Map<S, InternalState<S, E, C>> states;

    /** The event to transition map. */
    private final Map<E, Set<Transition<S, C>>> transitions;


    /**
     * Constructs a new state machine builder from the specified internal state factory, transition factory,
     * asynchronous action factory, asynchronous state builder factory, and state machine factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @param asyncStateBuilderFactory the factory used to create asynchronous state builders.
     * @param asyncStateMachineFactory the factory used to create the asynchronous state machine.
     */
    protected AsyncStateMachineBuilderBase(InternalStateFactory<S, E, C> internalStateFactory,
                                           TransitionFactory<S, C> transitionFactory,
                                           AsyncActionFactory<S, E, C> asyncActionFactory,
                                           AsyncStateBuilderFactory<S, E, C> asyncStateBuilderFactory,
                                           AsyncStateMachineFactory<S, E, C> asyncStateMachineFactory) {
        this.stateFactory = internalStateFactory;
        this.transitionFactory = transitionFactory;
        this.asyncActionFactory = asyncActionFactory;
        this.asyncStateBuilderFactory = asyncStateBuilderFactory;
        this.asyncStateMachineFactory = asyncStateMachineFactory;
        this.states = new LinkedHashMap<>();
        this.transitions = new LinkedHashMap<>();
    }

    @Override
    public AsyncStateBuilder<S, E, C> configure(S state) {
        InternalState<S, E, C> internal = states.computeIfAbsent(state, stateFactory::create);
        return asyncStateBuilderFactory.create(transitionFactory, asyncActionFactory, states, transitions, internal);
    }

    @Override
    public AsyncStateMachine<S, E, C> build(S starting, C context) {
        return asyncStateMachineFactory.create(transitionFactory, states, transitions, starting, context);
    }
}
