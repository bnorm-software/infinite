package com.bnorm.infinite;

/**
 * Simple interface that represents a transition between states.
 *
 * @param <S> the class type of the states.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface Transition<S, C> {

    /**
     * The state the transition originates from.
     *
     * @return the source state.
     */
    S getSource();

    /**
     * The state the transition completes at.
     *
     * @return the destination state.
     */
    S getDestination();

    /**
     * Returns {@code true} if the transition is a reentry into the originating state.  If a transition is a reentry
     * transition, any parent state's entry/exit actions are not called.
     *
     * @return if the source state is equal to the destination state.
     */
    default boolean isReentrant() {
        return getSource().equals(getDestination());
    }

    /**
     * Returns the guard for the transition.  If the transition is not guarded, a transition guard that always allows
     * the transition should be returned.
     *
     * @return the transition guard.
     */
    default TransitionGuard<C> getGuard() {
        return TransitionGuard.none();
    }
}
