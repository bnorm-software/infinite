package com.bnorm.infinite.file;

import java.util.function.Function;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.TransitionGuard;

/**
 * Base implementation for the StringStateMachineReader interface.  This is a combination of functions for converting
 * strings to states and events, along with a {@link StringStateActionFactory}, {@link StringTransitionActionFactory},
 * and [@link StringTransitionGuardFactory}.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
public class StringStateMachineReaderBase<S, E, C> implements StringStateMachineReader<S, E, C> {

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
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionActionFactory string transition action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
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
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(),
             StringTransitionActionFactory.noActionFactory(), StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringStateActionFactory<S, E, C> stateActionFactory) {
        this(stateFunction, eventFunction, stateActionFactory, StringTransitionActionFactory.noActionFactory(),
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionActionFactory string transition action factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringTransitionActionFactory<S, E, C> transitionActionFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(), transitionActionFactory,
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(),
             StringTransitionActionFactory.noActionFactory(), transitionGuardFactory);
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionActionFactory string transition action factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringStateActionFactory<S, E, C> stateActionFactory,
                                        StringTransitionActionFactory<S, E, C> transitionActionFactory) {
        this(stateFunction, eventFunction, stateActionFactory, transitionActionFactory,
             StringTransitionGuardFactory.noGuardFactory());
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param stateActionFactory string state action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringStateActionFactory<S, E, C> stateActionFactory,
                                        StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, stateActionFactory, StringTransitionActionFactory.noActionFactory(),
             transitionGuardFactory);
    }

    /**
     * Constructs a new StringStateMachineReaderBase with the specified parameters.
     *
     * @param stateFunction string state function.
     * @param eventFunction string event function.
     * @param transitionActionFactory string transition action factory.
     * @param transitionGuardFactory string transition guard factory.
     */
    public StringStateMachineReaderBase(Function<String, S> stateFunction, Function<String, E> eventFunction,
                                        StringTransitionActionFactory<S, E, C> transitionActionFactory,
                                        StringTransitionGuardFactory<S, E, C> transitionGuardFactory) {
        this(stateFunction, eventFunction, StringStateActionFactory.noActionFactory(), transitionActionFactory,
             transitionGuardFactory);
    }

    @Override
    public S readState(String state) {
        return stateFunction.apply(state);
    }

    @Override
    public E readEvent(String event) {
        return eventFunction.apply(event);
    }

    @Override
    public Action<S, E, C> readStateAction(S state, ActionType type, String action) {
        return stateActionFactory.create(state, type, action);
    }

    @Override
    public Action<S, E, C> readTransitionAction(S state, E event, S destination, String action) {
        return transitionActionFactory.create(state, event, destination, action);
    }

    @Override
    public TransitionGuard<C> readTransitionGuard(S state, E event, S destination, String guard) {
        return transitionGuardFactory.create(state, event, destination, guard);
    }
}
