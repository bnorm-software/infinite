package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionListener;
import com.bnorm.infinite.TransitionStage;

/**
 * The base implementation of an asynchronous transition listener factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class DefaultAsyncTransitionListenerFactory<S, E, C> implements AsyncTransitionListenerFactory<S, E, C> {

    /** The backing executor. */
    protected static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(
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
