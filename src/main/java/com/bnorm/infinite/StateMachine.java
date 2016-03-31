package com.bnorm.infinite;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.bnorm.infinite.builders.StateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * todo
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.0.0
 */
public class StateMachine<S, E, C> {

    /** The class logger. */
    private static final Logger log = LoggerFactory.getLogger(StateMachine.class);

    /** The state machine structure. */
    protected final StateMachineStructure<S, E, C> structure;

    /** The context of the state machine. */
    protected final C context;

    /** The current state of the state machine. */
    protected S state;

    /** The state machine transition listeners. */
    protected final Set<TransitionListener<? super S, ? super E, ? super C>> listeners;

    /**
     * Constructs a new state machine from the specified state machine structure, starting state, and context.
     *
     * @param structure the state machine structure.
     * @param starting the starting state of the state machine.
     * @param context the state machine context.
     */
    public StateMachine(StateMachineStructure<S, E, C> structure, S starting, C context) {
        this.structure = structure;
        this.context = context;
        this.state = starting;
        this.listeners = new LinkedHashSet<>();
    }

    /**
     * Returns the context of the state machine.
     *
     * @return the context.
     */
    public C getContext() {
        return context;
    }

    /**
     * Returns the current state of the state machine.
     *
     * @return the current state.
     */
    public S getState() {
        return state;
    }

    /**
     * Adds the specified transition listener to the state machine.
     *
     * @param listener the new transition listener.
     */
    public void addTransitionListener(TransitionListener<? super S, ? super E, ? super C> listener) {
        listeners.add(listener);
    }

    /**
     * Fires the specified event.  This is how states are transitioned.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    public Optional<Transition<S, E, C>> fire(E event) {
        log.trace("Event fired [{}]", event);
        final Set<Transition<S, E, C>> eventTransitions = structure.getTransitions(event);
        if (eventTransitions.isEmpty()) {
            log.trace("No transitions for event [{}]", event);
            return Optional.empty();
        }


        // ===== Find The Possible Transitions ===== //
        /*
         * To build the possible transition list, we will start with the current internal state and see if it handles
         * the specified event.  If it does not, we iterate one level up the parent chain and repeat.
         */

        List<Transition<S, E, C>> possible = Collections.emptyList();
        Optional<InternalState<S, E, C>> optional = Optional.of(structure.getState(state));
        while (possible.isEmpty() && optional.isPresent()) {
            final InternalState<S, E, C> state = optional.get();
            log.trace("Looking for allowed transitions from state [{}]", state.getState());
            possible = eventTransitions.stream()
                                       .filter(t -> Objects.equals(t.getSource(), state.getState()))
                                       .filter(t -> t.getGuard().allowed(getState(), event, getContext()))
                                       .collect(Collectors.toList());
            optional = state.getParentState();
            log.trace("Moving up the parent chain to state [{}]",
                      optional.isPresent() ? optional.get().getState() : null);
        }

        if (possible.isEmpty()) {
            log.trace("No transitions possible for event [{}]", event);
            return Optional.empty();
        } else if (possible.size() > 1) {
            log.warn("Multiple [{}] transitions possible for event [{}]", possible.size(), event);
            throw new StateMachineException(
                    String.format("Multiple [%d] transitions possible for event [%s]", possible.size(), event));
        }

        // ===== Gather Transition Information ===== //
        /*
         * Create a snapshot clone of the transition so the destination does not change each time we ask.  With dynamic
         * transitions this guarantees that the getDestination() method is only called once for each transition.  This
         * guardless copy of the transition is then passed to the to all consumers of the transition and eventually
         * returned by the method.
         */

        Transition<S, E, C> transition = possible.get(0).copy();
        final S destination = transition.getDestination();

        Optional<InternalState<S, E, C>> commonAncestor;
        commonAncestor = InternalState.getCommonAncestor(structure.getState(state), structure.getState(destination));
        final S commonAncestorState = commonAncestor.isPresent() ? commonAncestor.get().getState() : null;
        log.trace("Common ancestor of states [{}] and [{}] is [{}]", state, destination, commonAncestorState);


        // ===== Perform Transition ===== //
        /*
         * There are many steps to the actual transition but there are only 3 basic stages: exit, between, enter.
         *
         * To start the exiting stage, we have to notify transition listeners of the beginning of the transition.  Then
         * perform all exit actions of the current state.  At this point we are at the common ancestor state or the
         * transition so update the current state to be accurate.
         *
         * The between stage is the smallest stage.  During the between stage we notify transition listeners of the
         * current stage.  Then the transition action is performed to complete the between stage.
         *
         * Finally, the enter stage is begun by setting the current state to the destination state.  Then perform all
         * enter actions of the destination state.  Since the transition is about to complete, notify the transition
         * listeners.
         */

        log.trace("Starting transition from [{}] to [{}]", state, commonAncestorState);

        // exit

        log.trace("Notifying listeners before transition from [{}] to [{}]", state, destination);
        listeners.forEach(l -> l.stateTransition(TransitionStage.Before, event, transition, context));

        log.trace("Performing exit actions of [{}]", state);
        structure.getState(state).exit(event, transition, context);

        log.trace("Finished transition from [{}] to [{}]", state, commonAncestorState);
        state = commonAncestorState;

        // between

        log.trace("Notifying listeners between transition from [{}] to [{}]", state, destination);
        listeners.forEach(l -> l.stateTransition(TransitionStage.Between, event, transition, context));

        log.trace("Performing transition action while in state [{}]", state);
        transition.getAction().perform(state, event, transition, context);

        // enter

        log.trace("Starting transition from [{}] to [{}]", state, destination);
        state = destination;

        log.trace("Performing entrance actions of [{}]", state);
        structure.getState(state).enter(event, transition, context);

        log.trace("Notifying listeners after transition from [{}] to [{}]", state, destination);
        listeners.forEach(l -> l.stateTransition(TransitionStage.After, event, transition, context));

        // done

        log.trace("Finished transition from [{}] to [{}]", state, destination);

        return Optional.of(transition);
    }

    /**
     * State machine builder interface.  A state machine builder provides access to configuring each state and also for
     * creating the final state machine.  Since the builder and the state machine share the same backend it is possible
     * to build the state machine before configuring any states.  This allows the state machine itself to be reference
     * from any entrance or exit actions.  This however, is not fully supported at this time.
     *
     * @param <S> the class type of the states.
     * @param <E> the class type of the events.
     * @param <C> the class type of the context.
     * @author Brian Norman
     * @since 1.0.0
     */
    public static class Builder<S, E, C> {

        /** The state machine structure. */
        protected final StateMachineStructure<S, E, C> structure;


        /**
         * Constructs a new state machine builder from the specified state machine structure, state machine factory, and
         * state builder factory.
         */
        public Builder() {
            this.structure = new StateMachineStructure<>();
        }

        /**
         * Returns a state builder that can be used to configure the specified state.
         *
         * @param state the state to configure.
         * @return the state builder for the state.
         */
        public StateBuilder<S, E, C> configure(S state) {
            return new StateBuilder<>(structure, state);
        }

        /**
         * Builds and returns the state machine the state machine builder is constructing.
         *
         * @param starting the starting state of the state machine.
         * @param context the state machine context.
         * @return the state machine.
         */
        public StateMachine<S, E, C> build(S starting, C context) {
            return new StateMachine<>(structure, starting, context);
        }
    }
}
