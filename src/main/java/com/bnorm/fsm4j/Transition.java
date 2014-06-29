package com.bnorm.fsm4j;

/**
 * Simple interface that represents a transition between states.
 *
 * @param <S> the class type of the states.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public interface Transition<S extends State> {

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
     * If the transition is allowed.  This value could change given circumstances outside of the state machine so it is
     * checked every time this is a possible transition.
     *
     * @return if the transition is currently allowed.
     */
    default boolean allowed() {
        return true;
    }
}
