package com.bnorm.infinite;

/**
 * The base implementation of a state machine structure factory.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.3.0
 */
public class StateMachineStructureFactoryBase<S, E, C> implements StateMachineStructureFactory<S, E, C> {

    /** The internal state factory. */
    protected final InternalStateFactory<S, E, C> internalStateFactory;

    /** The transition factory. */
    protected final TransitionFactory<S, E, C> transitionFactory;

    /**
     * Constructs a new state machine structure factory from the specified internal state factory and transition
     * factory.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     */
    public StateMachineStructureFactoryBase(InternalStateFactory<S, E, C> internalStateFactory,
                                            TransitionFactory<S, E, C> transitionFactory) {
        this.internalStateFactory = internalStateFactory;
        this.transitionFactory = transitionFactory;
    }

    /**
     * Constructs a new state machine structure factory from the default internal state factory and transition factory.
     */
    public StateMachineStructureFactoryBase() {
        this(new InternalStateFactoryBase<>(), new TransitionFactoryBase<>());
    }

    @Override
    public StateMachineStructure<S, E, C> create() {
        return new StateMachineStructureBase<>(internalStateFactory, transitionFactory);
    }
}
