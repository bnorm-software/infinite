package com.bnorm.infinite.builders;

import java.util.Objects;
import java.util.function.Supplier;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.StateMachineException;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionGuard;

/**
 * The base implementation of a state builder.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class StateBuilderBase<S, E, C> implements StateBuilder<S, E, C> {

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
    protected StateBuilderBase(StateMachineStructure<S, E, C> structure, S state) {
        this.structure = structure;
        this.state = state;
    }

    @Override
    public StateBuilderBase<S, E, C> childOf(S parent) {
        final InternalState<S, E, C> internalState = structure.getState(state);
        final InternalState<S, E, C> internalParent = structure.getState(parent);
        internalParent.addChild(internalState);
        internalState.setParentState(internalParent);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> onEntry(Action<? super S, ? super E, ? super C> action) {
        structure.getState(state).addEntranceAction(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> onExit(Action<? super S, ? super E, ? super C> action) {
        structure.getState(state).addExitAction(action);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Transition<S, E, C> transition) {
        if (!Objects.equals(transition.getSource(), state)) {
            throw new StateMachineException(
                    "Illegal transition source.  Should be [" + state + "] Is [" + transition.getSource() + "]");
        }
        structure.addTransition(event, transition);
        return this;
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event) {
        return handle(event, structure.getTransitionFactory().create(state, state));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination) {
        return handle(event, structure.getTransitionFactory().create(state, destination));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Supplier<S> destination) {
        return handle(event, structure.getTransitionFactory().create(state, destination));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, TransitionGuard<C> guard) {
        return handle(event, structure.getTransitionFactory().create(state, state, guard));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Action<? super S, ? super E, ? super C> action) {
        return handle(event, structure.getTransitionFactory().create(state, state, action));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination, TransitionGuard<C> guard) {
        return handle(event, structure.getTransitionFactory().create(state, destination, guard));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard) {
        return handle(event, structure.getTransitionFactory().create(state, destination, guard));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination, Action<? super S, ? super E, ? super C> action) {
        return handle(event, structure.getTransitionFactory().create(state, destination, action));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Supplier<S> destination,
                                            Action<? super S, ? super E, ? super C> action) {
        return handle(event, structure.getTransitionFactory().create(state, destination, action));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, S destination, TransitionGuard<C> guard,
                                            Action<? super S, ? super E, ? super C> action) {
        return handle(event, structure.getTransitionFactory().create(state, destination, guard, action));
    }

    @Override
    public StateBuilderBase<S, E, C> handle(E event, Supplier<S> destination, TransitionGuard<C> guard,
                                            Action<? super S, ? super E, ? super C> action) {
        return handle(event, structure.getTransitionFactory().create(state, destination, guard, action));
    }
}
