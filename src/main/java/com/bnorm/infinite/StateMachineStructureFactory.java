package com.bnorm.infinite;

/**
 * A factory interface for state machine structures.
 *
 * @param <S>
 * @param <E>
 * @param <C>
 * @author Brian Norman
 * @since 1.1.0
 */
public interface StateMachineStructureFactory<S, E, C> {

    /**
     * Returns the default state machine structure factory.  This is the state machine structure base constructor.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine structure factory.
     */
    static <S, E, C> StateMachineStructureFactory<S, E, C> getDefault() {
        return StateMachineStructureBase::new;
    }

    /**
     * Create the default state machine structure.  This is a combination of the default internal state factory and
     * transition factory.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @return default state machine structure.
     */
    static <S, E, C> StateMachineStructure<S, E, C> createDefault() {
        return StateMachineStructureFactory.<S, E, C>getDefault()
                                           .create(InternalStateFactory.getDefault(), TransitionFactory.getDefault());
    }

    /**
     * Creates a new state machine structure from the specified internal state factory and transition factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @return a new state machine structure.
     */
    StateMachineStructure<S, E, C> create(InternalStateFactory<S, E, C> internalStateFactory,
                                          TransitionFactory<S, C> transitionFactory);
}
