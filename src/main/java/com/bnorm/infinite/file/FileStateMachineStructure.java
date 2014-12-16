package com.bnorm.infinite.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.StateMachineStructureBase;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.TransitionGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State machine structure read from a text file.  This text file is interpreted by a string state machine reader.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
public class FileStateMachineStructure<S, E, C> extends StateMachineStructureBase<S, E, C> {

    /** Class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(FileStateMachineStructure.class);

    /**
     * Constructs a new FileStateMachineStructure with the specified parameters.
     *
     * @param internalStateFactory the factory used to create internal states.
     * @param transitionFactory the factory used to create transitions.
     * @param path the file location of the state machine text.
     * @param stateMachineReader the reader used to load the state machine.
     * @throws IOException if there is a problem reading the state machine file.
     */
    public FileStateMachineStructure(InternalStateFactory<S, E, C> internalStateFactory,
                                     TransitionFactory<S, E, C> transitionFactory, Path path,
                                     StringStateMachineReader<S, E, C> stateMachineReader) throws IOException {
        super(internalStateFactory, transitionFactory);

        try (Scanner scanner = new Scanner(path)) {
            boolean validState = false;
            S state = null;
            while (scanner.hasNextLine()) {
                // Remove comments from the end of lines
                final String fullLine = scanner.nextLine();
                final String[] split = fullLine.split("#", 2);
                String line = split[0];

                if (!line.trim().isEmpty()) {
                    // The line contains state machine structure information
                    if (!line.startsWith(" ") && !line.startsWith("\t")) {
                        // A line that is not indented in anyway is a state
                        LOG.trace("Parsing line as state.  Line[{}]", fullLine);
                        state = parseState(stateMachineReader, line);
                        validState = true;
                    } else if (validState) {
                        // An indented line, if it comes directly after a state, is treated as a transition
                        LOG.trace("Parsing line as transition.  Line[{}]", fullLine);
                        parseTransition(state, stateMachineReader, line);
                    } else {
                        // Indented line that is at beginning of the file or did not come directly after a state
                        LOG.trace("Ignoring line.  Line[{}]", fullLine);
                    }
                }
            }
        }
    }

    /**
     * Parses the specified line string as a state using the given reader.
     *
     * @param stateMachineReader the state machine reader.
     * @param line the state machine text string.
     * @return the resulting state from the string.
     */
    private S parseState(StringStateMachineReader<S, E, C> stateMachineReader, String line) {
        final String[] split = line.split(":|/");
        int index = 0;

        final String stateString = split[index].trim();
        final S state = stateMachineReader.readState(stateString);
        final InternalState<S, E, C> internalState = this.getState(state);
        index++;
        LOG.trace("Found state [{}]", stateString);

        if (line.contains(":")) {
            final String parentString = split[index].trim();
            final S parent = stateMachineReader.readState(parentString);

            final InternalState<S, E, C> internalParent = this.getState(parent);
            internalParent.addChild(internalState);
            internalState.setParentState(internalParent);

            index++;
            LOG.trace("Found parent state [{}]", parentString);
        }

        if (line.contains("/")) {
            final String entryActionString = split[index].trim();
            if (!entryActionString.isEmpty()) {
                final Action<S, E, C> entryAction = stateMachineReader.readStateAction(state, ActionType.Entrance,
                                                                                       entryActionString);
                internalState.addEntranceAction(entryAction);
                LOG.trace("Found entry action [{}]", entryActionString);
            }
            index++;
        }

        if (index < split.length) {
            final String exitActionString = split[index].trim();
            final Action<S, E, C> exitAction = stateMachineReader.readStateAction(state, ActionType.Exit,
                                                                                  exitActionString);
            internalState.addExitAction(exitAction);
            index++;
            LOG.trace("Found exit action [{}]", exitActionString);
        }

        return state;
    }

    /**
     * Parses the specified line string as a transition using the given reader and source state.
     *
     * @param state the transition source state.
     * @param stateMachineReader the state machine reader.
     * @param line the state machine text string.
     */
    private void parseTransition(S state, StringStateMachineReader<S, E, C> stateMachineReader, String line) {
        final String[] split = line.split("->|\\[|/");
        int index = 0;

        final String eventString = split[index].trim();
        final E event = stateMachineReader.readEvent(eventString);
        index++;
        LOG.trace("Found transition event [{}]", eventString);

        final S destination;
        if (line.contains("->")) {
            final String stateString = split[index].trim();
            destination = stateMachineReader.readState(stateString);
            index++;
            LOG.trace("Found transition state [{}]", stateString);
        } else {
            destination = state;
            LOG.trace("Creating re-entrant transition to [{}]", state);
        }

        final TransitionGuard<C> transitionGuard;
        if (line.contains("[")) {
            String guard = split[index];
            final int guardEnd = guard.lastIndexOf(']');
            if (guardEnd > 0) {
                guard = guard.substring(0, guardEnd);
            } // else - no guard ending - that's fine
            guard = guard.trim();
            transitionGuard = stateMachineReader.readTransitionGuard(state, event, destination, guard);
            index++;
            LOG.trace("Found transition guard [{}]", guard);
        } else {
            transitionGuard = TransitionGuard.none();
        }

        final Action<S, E, C> transitionAction;
        if (line.contains("/")) {
            final String transitionActionString = split[index].trim();
            transitionAction = stateMachineReader.readTransitionAction(state, event, destination,
                                                                       transitionActionString);
            index++;
            LOG.trace("Found transition action [{}]", transitionActionString);
        } else {
            transitionAction = Action.noAction();
        }

        LOG.trace("Adding state transition.  Event [{}] State [{}]", event, destination);
        this.addTransition(event,
                           this.getTransitionFactory().create(state, destination, transitionGuard, transitionAction));
    }
}
