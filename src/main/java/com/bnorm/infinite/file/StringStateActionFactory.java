package com.bnorm.infinite.file;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;

/**
 * Functional interface that represents how to convert a string into a state entrance or exit Action.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
@FunctionalInterface
public interface StringStateActionFactory<S, E, C> {

    /**
     * A string state action factory that always returns a no action.  Since this string state action factory always
     * returns the type safe {@link Action#noAction()}, it can be cast to any required state machine types.
     */
    static StringStateActionFactory<?, ?, ?> NO_ACTION_FACTORY = (s, t, a) -> Action.noAction();

    /**
     * Returns the {@link StringStateActionFactory#NO_ACTION_FACTORY} factory cast to the required parameter types.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a type safe {@link StringStateActionFactory#NO_ACTION_FACTORY} factory.
     */
    static <S, E, C> StringStateActionFactory<S, E, C> noActionFactory() {
        @SuppressWarnings("unchecked")
        StringStateActionFactory<S, E, C> noActionFactory = (StringStateActionFactory<S, E, C>) NO_ACTION_FACTORY;
        return noActionFactory;
    }

    /**
     * Creates a new action given the specified action string, action type, and state to which it should be added.  How
     * to interpret these parameters is completely up to the implementing class.
     *
     * @param state the state to which the action should be added.
     * @param type the action type.
     * @param action the string that should be converted to an action.
     * @return a new action converted from the action string.
     */
    Action<S, E, C> create(S state, ActionType type, String action);
}
