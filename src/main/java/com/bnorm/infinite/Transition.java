package com.bnorm.infinite;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Simple interface that represents a transition between states.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class Transition<S, E, C> {

    /** The source state of the transition. */
    protected final S source;

    /** The destination state supplier of the transition. */
    protected final Supplier<? extends S> destination;

    /** The conditional nature of the transition. */
    protected final TransitionGuard<? super S, ? super E, ? super C> guard;

    /** The action to perform during the transition. */
    protected final Action<? super S, ? super E, ? super C> action;

    /**
     * Constructs a new transition from the specified source and destination states and the transition guard.
     *
     * @param source the source state of the transition.
     * @param destination the destination state supplier of the transition.
     * @param guard the guard for the transition.
     * @param action the action to perform during the transition.
     */
    public Transition(S source, Supplier<? extends S> destination,
                      TransitionGuard<? super S, ? super E, ? super C> guard,
                      Action<? super S, ? super E, ? super C> action) {
        this.source = source;
        this.destination = destination;
        this.guard = guard;
        this.action = action;
    }

    /**
     * The state the transition originates from.
     *
     * @return the source state.
     */
    public S getSource() {
        return source;
    }

    /**
     * The state the transition completes at.
     *
     * @return the destination state.
     */
    public S getDestination() {
        return destination.get();
    }

    /**
     * Returns {@code true} if the transition is a reentry into the originating state.  If a transition is a reentry
     * transition, any parent state's entry/exit actions are not called.
     *
     * @return if the source state is equal to the destination state.
     */
    public boolean isReentrant() {
        return Objects.equals(getSource(), getDestination());
    }

    /**
     * Returns the guard for the transition.  If the transition is not guarded, a transition guard that always allows
     * the transition should be returned.
     *
     * @return the transition guard.
     */
    public TransitionGuard<? super S, ? super E, ? super C> getGuard() {
        return guard;
    }

    /**
     * Returns the action to perform during the transition.  If the transition does not of an action, a transition
     * action that performs no action should be returned.
     *
     * @return the transition action.
     */
    public Action<? super S, ? super E, ? super C> getAction() {
        return action;
    }

    /**
     * Copy method following the prototype design pattern.  This is required to create a constant version of the
     * transition for use outside of the state machine structure.
     *
     * @return a constant copy of this transition.
     */
    public Transition<S, E, C> copy() {
        S constantDestination = destination.get();
        return new Transition<>(source, () -> constantDestination, guard, action);
    }

    @Override
    public String toString() {
        return "TransitionBase[" + getSource() + "->" + getDestination() + "]";
    }
}
