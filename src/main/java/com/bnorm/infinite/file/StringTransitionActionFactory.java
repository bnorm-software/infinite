package com.bnorm.infinite.file;

import com.bnorm.infinite.Action;

/**
 * Functional interface that represents how to convert a string into a transition Action.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
@FunctionalInterface
public interface StringTransitionActionFactory<S, E, C> {

    /**
     * A string transition action factory that always returns a no action.  Since this string transition action factory
     * always returns the type safe {@link Action#noAction()}, it can be cast to any required state machine types.
     */
    static StringTransitionActionFactory<?, ?, ?> NO_ACTION_FACTORY = (s, e, d, a) -> Action.noAction();

    /**
     * Returns the {@link StringTransitionActionFactory#NO_ACTION_FACTORY} factory cast to the required parameter
     * types.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a type safe {@link StringTransitionActionFactory#NO_ACTION_FACTORY} factory.
     */
    static <S, E, C> StringTransitionActionFactory<S, E, C> noActionFactory() {
        @SuppressWarnings("unchecked")
        StringTransitionActionFactory<S, E, C> noActionFactory = (StringTransitionActionFactory<S, E, C>) NO_ACTION_FACTORY;
        return noActionFactory;
    }

    /**
     * Creates a new action given the specified action string, transition event, and source and destination states.  How
     * to interpret the parameters is completely up to the implementing class.
     *
     * @param state the source state of the transition.
     * @param event the event which will generate the transition.
     * @param destination the destination state of the transition.
     * @param action the string that should be converted to an action.
     * @return a new action converted from the action string.
     */
    Action<S, E, C> create(S state, E event, S destination, String action);
}
