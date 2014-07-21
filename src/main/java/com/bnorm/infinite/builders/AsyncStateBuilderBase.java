package com.bnorm.infinite.builders;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.TransitionGuard;
import com.bnorm.infinite.async.AsyncActionFactory;

/**
 * The base implementation of an asynchronous state builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class AsyncStateBuilderBase<S, E, C> extends StateBuilderBase<S, E, C> implements AsyncStateBuilder<S, E, C> {

    /** The asynchronous action factory. */
    private final AsyncActionFactory<S, E, C> asyncActionFactory;

    /**
     * Constructs a new state builder from the specified transition factory, asynchronous action factory, state map,
     * transition map, and internal state being built.
     *
     * @param transitionFactory the factory used to create transitions.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     * @param states the states of the state machine.
     * @param transitions the transitions of the state machine.
     * @param state the internal state being built.
     */
    protected AsyncStateBuilderBase(TransitionFactory<S, C> transitionFactory,
                                    AsyncActionFactory<S, E, C> asyncActionFactory,
                                    Map<S, InternalState<S, E, C>> states, Map<E, Set<Transition<S, C>>> transitions,
                                    InternalState<S, E, C> state) {
        super(transitionFactory, states, transitions, state);
        this.asyncActionFactory = asyncActionFactory;
    }

    @Override
    public AsyncActionFactory<S, E, C> getAsyncActionFactory() {
        return asyncActionFactory;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> childOf(S parent) {
        super.childOf(parent);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> onEntry(Action<S, E, C> action) {
        super.onEntry(action);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> onExit(Action<S, E, C> action) {
        super.onExit(action);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> onAsyncEntry(Action<S, E, C> action) {
        AsyncStateBuilder.super.onAsyncEntry(action);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> onAsyncExit(Action<S, E, C> action) {
        AsyncStateBuilder.super.onAsyncExit(action);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, Transition<S, C> transition) {
        super.handle(event, transition);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event) {
        super.handle(event);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, S destination) {
        super.handle(event, destination);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, Supplier<S> destination) {
        super.handle(event, destination);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, TransitionGuard<C> guard) {
        super.handle(event, guard);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, S destination, TransitionGuard<C> guard) {
        super.handle(event, destination, guard);
        return this;
    }

    @Override
    public AsyncStateBuilderBase<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard) {
        super.handle(event, destination, guard);
        return this;
    }
}
