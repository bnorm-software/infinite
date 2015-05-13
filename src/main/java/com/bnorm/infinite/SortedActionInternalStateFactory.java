package com.bnorm.infinite;

import java.util.Comparator;

/**
 * The implementation of an internal state factory that creates internal states with sorted sets of entrance and exit
 * actions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class SortedActionInternalStateFactory<S, E, C> implements InternalStateFactory<S, E, C> {

    /** The entrance action comparator. */
    private final Comparator<Action<? super S, ? super E, ? super C>> entranceComparator;

    /** The exit action comparator. */
    private final Comparator<Action<? super S, ? super E, ? super C>> exitComparator;

    /**
     * Constructs an internal state factory with the specified action comparator.
     *
     * @param actionComparator the action comparator.
     */
    public SortedActionInternalStateFactory(Comparator<Action<? super S, ? super E, ? super C>> actionComparator) {
        this(actionComparator, actionComparator);
    }

    /**
     * Constructs an internal state factory with the specified action comparators.
     *
     * @param entranceComparator the entrance action comparator.
     * @param exitComparator the exit action comparator.
     */
    public SortedActionInternalStateFactory(Comparator<Action<? super S, ? super E, ? super C>> entranceComparator,
                                            Comparator<Action<? super S, ? super E, ? super C>> exitComparator) {
        this.entranceComparator = entranceComparator;
        this.exitComparator = exitComparator;
    }

    @Override
    public InternalState<S, E, C> create(S state) {
        return new InternalStateBase<>(state, entranceComparator, exitComparator);
    }
}
