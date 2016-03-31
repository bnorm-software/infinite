package com.bnorm.infinite.async;

import java.util.function.Supplier;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.StateBuilder;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionGuard;

/**
 * Represents a builder of a specific state for an asynchronous state machine.  The interface provides methods that add
 * behavior to the asynchronous state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public class AsyncStateBuilder<S, E, C> extends StateBuilder<S, E, C> {

    /** The asynchronous action factory. */
    protected final AsyncActionFactory<S, E, C> asyncActionFactory;

    /**
     * Constructs a new state builder from the specified state machine structure, asynchronous action factory, and state
     * being built.
     *
     * @param structure the state machine structure.
     * @param state the state being built.
     * @param asyncActionFactory the factory used to create asynchronous actions.
     */
    protected AsyncStateBuilder(StateMachineStructure<S, E, C> structure, S state,
                                AsyncActionFactory<S, E, C> asyncActionFactory) {
        super(structure, state);
        this.asyncActionFactory = asyncActionFactory;
    }

    @Override
    public AsyncStateBuilder<S, E, C> childOf(S parent) {
        super.childOf(parent);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> onEntry(Action<? super S, ? super E, ? super C> action) {
        super.onEntry(action);
        return this;
    }

    /**
     * Adds the specified action as an asynchronous entry action to the internal state.
     *
     * @param action the new asynchronous entry action.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> onAsyncEntry(Action<? super S, ? super E, ? super C> action) {
        return onEntry(asyncActionFactory.create(action));
    }

    @Override
    public AsyncStateBuilder<S, E, C> onExit(Action<? super S, ? super E, ? super C> action) {
        super.onExit(action);
        return this;
    }

    /**
     * Adds the specified action as an asynchronous exit action to the internal state.
     *
     * @param action the new asynchronous exit action.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> onAsyncExit(Action<? super S, ? super E, ? super C> action) {
        onExit(asyncActionFactory.create(action));
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Transition<S, E, C> transition) {
        super.handle(event, transition);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event) {
        super.handle(event);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, S destination) {
        super.handle(event, destination);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination) {
        super.handle(event, destination);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, TransitionGuard<? super S, ? super E, ? super C> guard) {
        super.handle(event, guard);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Action<? super S, ? super E, ? super C> action) {
        super.handle(event, action);
        return this;
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event and asynchronous
     * transition action.
     *
     * @param event the event that will cause the transition.
     * @param action the asynchronous action to perform during the transition.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> handleAsync(E event, Action<? super S, ? super E, ? super C> action) {
        super.handle(event, asyncActionFactory.create(action));
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, S destination,
                                             TransitionGuard<? super S, ? super E, ? super C> guard) {
        super.handle(event, destination, guard);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                             TransitionGuard<? super S, ? super E, ? super C> guard) {
        super.handle(event, destination, guard);
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, S destination, Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, action);
        return this;
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination, and
     * asynchronous transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param action the asynchronous action to perform during the transition.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> handleAsync(E event, S destination,
                                                  Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, asyncActionFactory.create(action));
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                             Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, action);
        return this;
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination
     * supplier, and asynchronous transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @param action the asynchronous action to perform during the transition.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> handleAsync(E event, Supplier<? extends S> destination,
                                                  Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, asyncActionFactory.create(action));
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, S destination,
                                             TransitionGuard<? super S, ? super E, ? super C> guard,
                                             Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, guard, action);
        return this;
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination,
     * transition guard, and asynchronous transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param guard the guard for the transition.
     * @param action the asynchronous action to perform during the transition.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> handleAsync(E event, S destination,
                                                  TransitionGuard<? super S, ? super E, ? super C> guard,
                                                  Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, guard, asyncActionFactory.create(action));
        return this;
    }

    @Override
    public AsyncStateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                             TransitionGuard<? super S, ? super E, ? super C> guard,
                                             Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, guard, action);
        return this;
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination
     * supplier, transition guard, and asynchronous transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the asynchronous action to perform during the transition.
     * @return the current asynchronous state builder for chaining.
     */
    public AsyncStateBuilder<S, E, C> handleAsync(E event, Supplier<? extends S> destination,
                                                  TransitionGuard<? super S, ? super E, ? super C> guard,
                                                  Action<? super S, ? super E, ? super C> action) {
        super.handle(event, destination, guard, asyncActionFactory.create(action));
        return this;
    }
}
