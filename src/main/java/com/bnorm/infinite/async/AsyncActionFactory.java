package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
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
 * @since 1.1.0
 */
public interface AsyncActionFactory<S, E, C> {

    /**
     * Returns the default asynchronous action factory.  This asynchronous action factory has the default cached thread
     * pool as a backing with a named thread factory set to name all threads as {@code AsyncAction[index]}.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default asynchronous action factory.
     */
    static <S, E, C> AsyncActionFactory<S, E, C> getDefault() {
        return new AsyncActionFactory<S, E, C>() {
            /** The backing executor. */
            final ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("AsyncAction"));

            @Override
            public Action<S, E, C> create(Action<S, E, C> action) {
                return new Action<S, E, C>() {
                    @Override
                    public void perform(S state, E event, Transition<? extends S, ? extends E, ? extends C> transition,
                                        C context) {
                        executor.submit(() -> action.perform(state, event, transition, context));
                    }
                };
            }
        };
    }


    /**
     * Creates a new action that is an asynchronous version of the specified action.  The new asynchronous action uses
     * the factory completion executor to execute the specified action.
     *
     * @param action the action to perform asynchronously.
     * @return a new asynchronous action.
     */
    Action<S, E, C> create(Action<S, E, C> action);
}
