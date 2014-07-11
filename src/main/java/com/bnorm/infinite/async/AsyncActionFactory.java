package com.bnorm.infinite.async;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.Transition;

/**
 * A factory interface for asynchronous actions.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public interface AsyncActionFactory<S, E, C> {

    /**
     * Returns the default async action factory.  This async action factory has the default cached thread pool as a
     * backing with an action thread factory set to name all threads as {@code AsyncAction}.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default async action factory.
     */
    static <S, E, C> AsyncActionFactory<S, E, C> getDefault() {
        return new AsyncActionFactory<S, E, C>() {
            /** The backing executor. */
            private final Executor executor = Executors.newCachedThreadPool(new ActionThreadFactory("AsyncAction"));

            @Override
            public Executor getExecutor() {
                return executor;
            }
        };
    }


    /**
     * Creates a new action that is an asynchronous version of the specified action.  The new asynchronous action uses
     * the factory executor to execute the specified action.
     *
     * @param action the action to perform asynchronously.
     * @return a new asynchronous action.
     */
    default Action<S, E, C> create(Action<S, E, C> action) {
        return new Action<S, E, C>() {
            @Override
            public void perform(S state, E event, Transition<? extends S, ? extends C> transition, C context) {
                getExecutor().execute(() -> action.perform(state, event, transition, context));
            }
        };
    }

    /**
     * Returns the executor that backs this async action factory.  This is the executor used to perform all asynchronous
     * actions.
     *
     * @return the asynchronous action executor.
     */
    Executor getExecutor();
}
