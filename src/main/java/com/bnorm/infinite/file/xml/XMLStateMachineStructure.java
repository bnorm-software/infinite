package com.bnorm.infinite.file.xml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.StateMachineStructureBase;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.TransitionGuard;
import com.bnorm.infinite.file.StringStateMachineReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLStateMachineStructure<S, E, C> extends StateMachineStructureBase<S, E, C> {

    /** The class logger. */
    private static final Logger log = LoggerFactory.getLogger(XMLStateMachineStructure.class);

    private static JAXBContext CONTEXT = null;

    /**
     * Constructs a new FileStateMachineStructure with the specified parameters.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param path the file location of the state machine text.
     * @param stateMachineReader the reader used to load the state machine.
     * @throws java.io.IOException if there is a problem reading the state machine file.
     * @throws javax.xml.bind.JAXBException todo
     */
    public XMLStateMachineStructure(InternalStateFactory<S, E, C> internalStateFactory,
                                    TransitionFactory<S, E, C> transitionFactory, Path path,
                                    StringStateMachineReader<S, E, C> stateMachineReader) throws IOException, JAXBException {
        super(internalStateFactory, transitionFactory);

        if (CONTEXT == null) {
            synchronized (this) {
                if (CONTEXT == null) {
                    CONTEXT = JAXBContext.newInstance(StateMachine.class.getPackage().getName());
                }
            }
        }

        StateMachine machine = (StateMachine) CONTEXT.createUnmarshaller().unmarshal(Files.newInputStream(path));

        for (StateType stateType : machine.getState()) {
            processStateType(stateMachineReader, stateType);
        }
    }

    private S processStateType(StringStateMachineReader<S, E, C> stateMachineReader, StateType stateType) {
        final S state = stateMachineReader.readState(stateType.getName());
        final InternalState<S, E, C> internalState = this.getState(state);
        log.trace("Found state [{}]", stateType.getName());

        if (stateType.getParent() != null) {
            final S parent = stateMachineReader.readState(stateType.getParent());

            final InternalState<S, E, C> internalParent = this.getState(parent);
            internalParent.addChild(internalState);
            internalState.setParentState(internalParent);
            log.trace("Found parent state [{}]", stateType.getParent());
        }

        if (stateType.getEntry() != null) {
            final Action<S, E, C> entryAction = stateMachineReader.readStateAction(state, ActionType.Entrance,
                                                                                   stateType.getEntry());
            internalState.addEntranceAction(entryAction);
            log.trace("Found entry action [{}]", stateType.getEntry());
        }

        if (stateType.getExit() != null) {
            final Action<S, E, C> exitAction = stateMachineReader.readStateAction(state, ActionType.Exit,
                                                                                  stateType.getExit());
            internalState.addExitAction(exitAction);
            log.trace("Found exit action [{}]", stateType.getExit());
        }

        for (EventType eventType : stateType.getEvent()) {
            processEventType(state, stateMachineReader, eventType);
        }

        return state;
    }

    private void processEventType(S state, StringStateMachineReader<S, E, C> stateMachineReader, EventType eventType) {
        final E event = stateMachineReader.readEvent(eventType.getName());
        log.trace("Found transition event [{}]", eventType.getName());

        final S destination;
        if (eventType.getState() != null) {
            destination = stateMachineReader.readState(eventType.getState());
            log.trace("Found transition state [{}]", eventType.getState());
        } else {
            destination = state;
            log.trace("Creating re-entrant transition to [{}]", state);
        }

        final TransitionGuard<C> transitionGuard;
        if (eventType.getGuard() != null) {
            transitionGuard = stateMachineReader.readTransitionGuard(state, event, destination, eventType.getGuard());
            log.trace("Found transition guard [{}]", eventType.getGuard());
        } else {
            transitionGuard = TransitionGuard.none();
        }

        final Action<S, E, C> transitionAction;
        if (eventType.getAction() != null) {
            transitionAction = stateMachineReader.readTransitionAction(state, event, destination,
                                                                       eventType.getAction());
            log.trace("Found transition action [{}]", eventType.getAction());
        } else {
            transitionAction = Action.noAction();
        }

        log.trace("Adding state transition.  Event [{}] State [{}]", event, destination);
        this.addTransition(event,
                           this.getTransitionFactory().create(state, destination, transitionGuard, transitionAction));
    }
}
