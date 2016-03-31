package com.bnorm.infinite.file;

import java.util.function.Function;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.TransitionGuard;

/**
 * Interface that represents how to convert strings into state machine actions and transition guards.  This means that
 * it is usually a combination of a {@link StringStateActionFactory}, {@link StringTransitionActionFactory}, and [@link
 * StringTransitionGuardFactory}.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
public class StringStateMachineReader<S, E, C> {

    /** Function to convert a string into a state. */
    protected final Function<String, S> stateFunction;

    /** Function to convert a string into an event. */
    protected final Function<String, E> eventFunction;

    /** Factory to create entrance or exit actions for states. */
    protected final StringStateActionFactory<S, E, C> stateActionFactory;

    /** Factory to create actions for transitions. */
    protected final StringTransitionActionFactory<S, E, C> transitionActionFactory;

    /** Factory to create guards for transitions. */
    protected final StringTransitionGuardFactory<S, E, C> transitionGuardFactory;

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionActionFactory string transition action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringStateActionFactory<S, E, C> stateActionFactory,
                                    StringTransitionActionFactory<S, E, C> transitionActionFactory,
                                    StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this.stateFunction = stateFunction;
        this.eventFunction = eventFunction;
        this.stateActionFactory = stateActionFactory;
        this.transitionActionFactory = transitionActionFactory;
        this.transitionGuardFactory = transitionGuardFactory;
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(),
             StringTransitionActionFactory.noActionFactory(), StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringStateActionFactory<S, E, C> stateActionFactory) {
        this(stateFunction, eventFunction, stateActionFactory, StringTransitionActionFactory.noActionFactory(),
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionActionFactory string transition action factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringTransitionActionFactory<S, E, C> transitionActionFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(), transitionActionFactory,
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(),
             StringTransitionActionFactory.noActionFactory(), transitionGuardFactory);
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionActionFactory string transition action factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringStateActionFactory<S, E, C> stateActionFactory,
                                    StringTransitionActionFactory<S, E, C> transitionActionFactory) {
        this(stateFunction, eventFunction, stateActionFactory, transitionActionFactory,
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringStateActionFactory<S, E, C> stateActionFactory,
                                    StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, stateActionFactory, StringTransitionActionFactory.noActionFactory(),
             transitionGuardFactory);
    }

    /**
     * Constructs a new StringStateMachineReader with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionActionFactory string transition action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReader(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                    StringTransitionActionFactory<S, E, C> transitionActionFactory,
                                    StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(), transitionActionFactory,
             transitionGuardFactory);
    }

    /**
     * Reads the specified state string and returns a typed state machine state.
     *
     * @param state the state string.
     * @return a typed state machine state.
     */
    public S readState(String state) {
        return stateFunction.apply(state);
    }

    /**
     * Reads the specified event string and returns a typed state machine event.
     *
     * @param event the event string.
     * @return a typed state machine event.
     */
    public E readEvent(String event) {
        return eventFunction.apply(event);
    }

    /**
     * Reads the specified state action string and returns a typed state machine action.
     *
     * @param state the action state.
     * @param type the action type.
     * @param action the state action string.
     * @return a typed state action.
     */
    public Action<S, E, C> readStateAction(S state, ActionType type, String action) {
        return stateActionFactory.create(state, type, action);
    }

    /**
     * Reads the specified transition action string and returns a typed state machine transition action.
     *
     * @param state the transition source state.
     * @param event the transition event.
     * @param destination the transition destination state.
     * @param action the transition action string.
     * @return a typed transition action.
     */
    public Action<S, E, C> readTransitionAction(S state, E event, S destination, String action) {
        return transitionActionFactory.create(state, event, destination, action);
    }

    /**
     * Reads the specified transition guard string and returns a typed state machine transition guard.
     *
     * @param state the transition source state.
     * @param event the transition event.
     * @param destination the transition destination state.
     * @param guard the transition guard string.
     * @return a typed transition guard.
     */
    public TransitionGuard<S, E, C> readTransitionGuard(S state, E event, S destination, String guard) {
        return transitionGuardFactory.create(state, event, destination, guard);
    }
}
