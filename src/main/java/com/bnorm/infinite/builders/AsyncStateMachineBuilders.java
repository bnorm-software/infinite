package com.bnorm.infinite.builders;

import com.bnorm.infinite.StateMachineStructures;

/**
 * Utility class for asynchronous state machine builders.
 *
 * @author Brian Norman
 * @since 1.3.0
 */
public final class AsyncStateMachineBuilders {

    /**
     * Creates a default asynchronous state machine builder.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new asynchronous state machine builder.
     */
    public static <S, E, C> AsyncStateMachineBuilder<S, E, C> create() {
        AsyncStateMachineBuilderFactory<S, E, C> asyncStateMachineBuilderFactory;
        asyncStateMachineBuilderFactory = new AsyncStateMachineBuilderFactoryBase<>();
        return asyncStateMachineBuilderFactory.create(StateMachineStructures.create());
    }

    /**
     * Creates an asynchronous state machine builder from the specified factory.
     *
     * @param asyncStateMachineBuilderFactory the factory used to create a asynchronous state machine builder.
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return a new asynchronous state machine builder.
     */
    public static <S, E, C> AsyncStateMachineBuilder<S, E, C> create(
            AsyncStateMachineBuilderFactory<S, E, C> asyncStateMachineBuilderFactory) {
        return asyncStateMachineBuilderFactory.create(StateMachineStructures.create());
    }
}
