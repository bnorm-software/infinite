package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.Transition;

public class AsyncActionFactoryBase<S, E, C> implements AsyncActionFactory<S, E, C> {

    /** The backing executor. */
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(
            new NamedThreadFactory("AsyncAction"));

    @Override
    public Action<S, E, C> create(Action<S, E, C> action) {
        return new Action<S, E, C>() {
            @Override
            public void perform(S state, E event, Transition<? extends S, ? extends E, ? extends C> transition,
                                C context) {
                EXECUTOR.submit(() -> action.perform(state, event, transition, context));
            }
        };
    }
}
