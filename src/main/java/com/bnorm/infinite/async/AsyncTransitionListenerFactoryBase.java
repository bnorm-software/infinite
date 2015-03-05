package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionListener;
import com.bnorm.infinite.TransitionStage;

public class AsyncTransitionListenerFactoryBase<S, E, C> implements AsyncTransitionListenerFactory<S, E, C> {
    /** The backing executor. */
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(
            new NamedThreadFactory("AsyncTransitionListener"));

    @Override
    public TransitionListener<S, E, C> create(TransitionListener<? super S, ? super E, ? super C> listener) {
        return new TransitionListener<S, E, C>() {
            @Override
            public void stateTransition(TransitionStage stage, E event,
                                        Transition<? extends S, ? extends E, ? extends C> transition, C context) {
                EXECUTOR.submit(() -> listener.stateTransition(stage, event, transition, context));
            }
        };
    }
}
