package com.bnorm.fsm4j;

/**
 * Simple interface that represents a transition between states.
 *
 * @author Brian Norman
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
}
