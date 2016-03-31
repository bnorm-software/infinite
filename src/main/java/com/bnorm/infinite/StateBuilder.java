package com.bnorm.infinite;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a builder of a specific state.  The interface provides methods that add behavior to the state machine.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class StateBuilder<S, E, C> {

    /** The state machine structure. */
    protected final StateMachineStructure<S, E, C> structure;

    /** The state being built. */
    protected final S state;

    /**
     * Constructs a new state builder from the specified state machine structure and the state being built.
     *
     * @param structure the state machine structure
     * @param state the state being built.
     */
    protected StateBuilder(StateMachineStructure<S, E, C> structure, S state) {
        this.structure = structure;
        this.state = state;
    }

    /**
     * Sets the the parent of the internal state to be the specified state.  The parent state must already be partially
     * built by another state builder before it can be set as the parent of another state.
     *
     * @param parent the parent state.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> childOf(S parent) {
        final InternalState<S, E, C> internalState = structure.getState(state);
        final InternalState<S, E, C> internalParent = structure.getState(parent);
        internalParent.addChild(internalState);
        internalState.setParentState(internalParent);
        return this;
    }

    /**
     * Adds the specified action as an entry action to the internal state.
     *
     * @param action the new entry action.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> onEntry(Action<? super S, ? super E, ? super C> action) {
        structure.getState(state).addEntranceAction(action);
        return this;
    }

    /**
     * Adds the specified action as an exit action to the internal state.
     *
     * @param action the new exit action.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> onExit(Action<? super S, ? super E, ? super C> action) {
        structure.getState(state).addExitAction(action);
        return this;
    }

    /**
     * Adds the specified transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the transition.
     * @param transition the transition that will result because of the event.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Transition<S, E, C> transition) {
        if (!Objects.equals(transition.getSource(), state)) {
            throw new StateMachineException(
                    "Illegal transition source.  Should be [" + state + "] Is [" + transition.getSource() + "]");
        }
        structure.addTransition(event, transition);
        return this;
    }

    /**
     * Adds a reentrant transition as a possible transition given the specified event.
     *
     * @param event the event that will cause the reentrant transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event) {
        return handle(event, new Transition<>(state, () -> state, TransitionGuard.none(), Action.noAction()));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event and destination.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, S destination) {
        return handle(event, new Transition<>(state, () -> destination, TransitionGuard.none(), Action.noAction()));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event and destination
     * supplier.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination) {
        return handle(event, new Transition<>(state, destination, TransitionGuard.none(), Action.noAction()));
    }

    /**
     * Adds a reentrant transition as a possible transition given the specified event and transition guard.
     *
     * @param event the event that will cause the reentrant transition.
     * @param guard the guard for the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, TransitionGuard<? super S, ? super E, ? super C> guard) {
        return handle(event, new Transition<>(state, () -> state, guard, Action.noAction()));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event and transition
     * action.
     *
     * @param event the event that will cause the transition.
     * @param action the action to perform during the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Action<? super S, ? super E, ? super C> action) {
        return handle(event, new Transition<>(state, () -> state, TransitionGuard.none(), action));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination, and the
     * transition guard.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param guard the guard for the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, S destination,
                                        TransitionGuard<? super S, ? super E, ? super C> guard) {
        return handle(event, new Transition<>(state, () -> destination, guard, Action.noAction()));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination
     * supplier, and transition guard.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @param guard the guard for the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                        TransitionGuard<? super S, ? super E, ? super C> guard) {
        return handle(event, new Transition<>(state, destination, guard, Action.noAction()));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination, and
     * transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param action the action to perform during the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, S destination, Action<? super S, ? super E, ? super C> action) {
        return handle(event, new Transition<>(state, () -> destination, TransitionGuard.none(), action));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination
     * supplier, and transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @param action the action to perform during the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                        Action<? super S, ? super E, ? super C> action) {
        return handle(event, new Transition<>(state, destination, TransitionGuard.none(), action));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination,
     * transition guard, and transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, S destination, TransitionGuard<? super S, ? super E, ? super C> guard,
                                        Action<? super S, ? super E, ? super C> action) {
        return handle(event, new Transition<>(state, () -> destination, guard, action));
    }

    /**
     * Adds a transition to the specified state as a possible transition given the specified event, destination
     * supplier, transition guard, and transition action.
     *
     * @param event the event that will cause the transition.
     * @param destination the destination supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     * @return the current state builder for chaining.
     */
    public StateBuilder<S, E, C> handle(E event, Supplier<? extends S> destination,
                                        TransitionGuard<? super S, ? super E, ? super C> guard,
                                        Action<? super S, ? super E, ? super C> action) {
        return handle(event, new Transition<>(state, destination, guard, action));
    }
}
