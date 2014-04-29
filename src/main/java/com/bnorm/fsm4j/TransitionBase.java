package com.bnorm.fsm4j;

/**
 * The base implementation of a transition.
 *
 * @param <S> the class type of the states.
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
class TransitionBase<S extends State> implements Transition<S> {

    /** The source state of the transition. */
    private final S source;

    /** The destination state of the transition. */
    private S destination;

    /**
     * Constructs a new transition from the specified source and destination states.
     *
     * @param source the source state of the transition.
     * @param destination the destination state of the transition.
     */
    TransitionBase(S source, S destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public S getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "TransitionBase[" + source + "->" + destination + "]";
    }
}
