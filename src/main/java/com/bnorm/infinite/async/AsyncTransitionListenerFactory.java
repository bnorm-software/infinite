package com.bnorm.infinite.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionListener;
import com.bnorm.infinite.TransitionStage;

/**
 * A factory interface for asynchronous transition listeners.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.1.0
 */
public interface AsyncTransitionListenerFactory<S, E, C> {

    /**
     * Returns the default asynchronous transition listener factory.  This asynchronous transition listener factory has
     * the default cached thread pool as a backing with a named thread factory set to name all threads as {@code
     * AsyncTransitionListener[index]}.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default asynchronous transition listener factory.
     */
    static <S, E, C> AsyncTransitionListenerFactory<S, E, C> getDefault() {
        return new AsyncTransitionListenerFactory<S, E, C>() {
            /** The backing executor. */
            final ExecutorService executor = Executors.newCachedThreadPool(
                    new NamedThreadFactory("AsyncTransitionListener"));

            @Override
            public TransitionListener<S, E, C> create(TransitionListener<? super S, ? super E, ? super C> listener) {
                return new TransitionListener<S, E, C>() {
                    @Override
                    public void stateTransition(TransitionStage stage, E event,
                                                Transition<? extends S, ? extends E, ? extends C> transition,
                                                C context) {
                        executor.submit(() -> listener.stateTransition(stage, event, transition, context));
                    }
                };
            }
        };
    }


    /**
     * Creates a new transition listener that is an asynchronous version of the specified transition listener.  The new
     * asynchronous transition listener uses the factory completion executor to execute the specified transition
     * listener.
     *
     * @param listener the transition listener to perform asynchronously.
     * @return a new asynchronous transition listener.
     */
    TransitionListener<S, E, C> create(TransitionListener<? super S, ? super E, ? super C> listener);
}
