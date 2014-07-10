package com.bnorm.infinite;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The operational state machine interface.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @version 1.0.0
 * @since 1.0.0
 */
public interface StateMachine<S, E, C> {

    /**
     * Sets the current state of the state machine to the specified state.  This will skip any entrance or exit
     * actions.
     *
     * @param state the new current state.
     */
    void setState(S state);

    /**
     * Returns the current state of the state machine.
     *
     * @return the current state.
     */
    S getState();

    /**
     * Returns the context of the state machine.
     *
     * @return the context.
     */
    C getContext();

    /**
     * Returns the internal state from the state machine corresponding to the specified state.
     *
     * @param state the state to find the internal state.
     * @return the corresponding internal state.
     */
    InternalState<S, E, C> getInternalState(S state);

    /**
     * Returns a set of transitions corresponding to the specified event.
     *
     * @param event the event to find the set of transitions.
     * @return the corresponding set of transitions.
     */
    Set<Transition<S, C>> getTransitions(E event);

    /**
     * Returns all the transition listeners for the state machine.
     *
     * @return all transition listeners.
     */
    Set<TransitionListener<? super S, ? super E, ? super C>> getTransitionListeners();

    /**
     * Adds the specified transition listener to the state machine.
     *
     * @param listener the new transition listener.
     */
    void addTransitionListener(TransitionListener<? super S, ? super E, ? super C> listener);

    /**
     * Fires the specified event.  This is how states are transitioned.
     *
     * @param event the event fired.
     * @return the resulting transition.
     */
    default Optional<Transition<S, C>> fire(E event) {
        List<Transition<S, C>> possible = Collections.emptyList();

        // To build the possible transition list, we will start with the current internal state and see if it
        // handles the specified event.  If it does not, we iterate one level up the parent chain and repeat.
        Optional<InternalState<S, E, C>> optional = Optional.of(getInternalState(getState()));
        final Set<Transition<S, C>> transitions = getTransitions(event);
        while (possible.isEmpty() && optional.isPresent()) {
            final InternalState<S, E, C> state = optional.get();
            possible = transitions.stream()
                                  .filter(t -> t.getSource().equals(state.getState()))
                                  .filter(t -> t.getGuard().allowed(getContext()))
                                  .collect(Collectors.toList());
            optional = state.getParentState();
        }

        if (possible.isEmpty()) {
            return Optional.empty();
        } else if (possible.size() > 1) {
            throw new StateMachineException("Too many possible transitions");
        }

        Transition<S, C> transition = possible.get(0);
        if (getInternalState(transition.getDestination()) == null) {
            throw new StateMachineException("No internal state found for destination state in state machine");
        }

        getInternalState(getState()).exit(event, transition, getContext());
        setState(transition.getDestination());
        getInternalState(getState()).enter(event, transition, getContext());

        getTransitionListeners().forEach(l -> l.stateTransition(event, transition, getContext()));
        return Optional.of(transition);
    }
}
