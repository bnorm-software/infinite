package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.Transition;

/**
 * The base implementation of an asynchronous action factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class DefaultAsyncActionFactory<S, E, C> implements AsyncActionFactory<S, E, C> {

    /** The backing executor. */
    protected static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(
            new NamedThreadFactory("AsyncAction"));

    @Override
    public Action<S, E, C> create(Action<? super S, ? super E, ? super C> action) {
        return new Action<S, E, C>() {
            @Override
            public void perform(S state, E event, Transition<? extends S, ? extends E, ? extends C> transition,
                                C context) {
                EXECUTOR.submit(() -> action.perform(state, event, transition, context));
            }
        };
    }
}
