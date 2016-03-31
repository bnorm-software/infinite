package com.bnorm.infinite.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Scanner;

import com.bnorm.infinite.Action;
import com.bnorm.infinite.ActionType;
import com.bnorm.infinite.InternalState;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.Transition;
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
public class FileStateMachineStructure<S, E, C> extends StateMachineStructure<S, E, C> {

    /** The class logger. */
    private static final Logger log = LoggerFactory.getLogger(FileStateMachineStructure.class);

    /**
     * Constructs a new FileStateMachineStructure with the specified parameters.
     *
     * @param path the file location of the state machine text.
     * @param reader the reader used to load the state machine.
     * @throws IOException if there is a problem reading the state machine file.
     */
    public FileStateMachineStructure(Path path, StringStateMachineReader<S, E, C> reader) throws IOException {
        parseFile(path, reader);
    }

    /**
     * Constructs a new FileStateMachineStructure with the specified parameters.
     *
     * @param path the file location of the state machine text.
     * @param reader the reader used to load the state machine.
     * @param actionComparator todo
     * @throws IOException if there is a problem reading the state machine file.
     */
    public FileStateMachineStructure(Path path, StringStateMachineReader<S, E, C> reader,
                                     Comparator<Action<? super S, ? super E, ? super C>> actionComparator) throws IOException {
        super(actionComparator);
        parseFile(path, reader);
    }

    /**
     * Constructs a new FileStateMachineStructure with the specified parameters.
     *
     * @param path the file location of the state machine text.
     * @param reader the reader used to load the state machine.
     * @param entranceComparator todo
     * @param exitComparator todo
     * @throws IOException if there is a problem reading the state machine file.
     */
    public FileStateMachineStructure(Path path, StringStateMachineReader<S, E, C> reader,
                                     Comparator<Action<? super S, ? super E, ? super C>> entranceComparator,
                                     Comparator<Action<? super S, ? super E, ? super C>> exitComparator) throws IOException {
        super(entranceComparator, exitComparator);
        parseFile(path, reader);
    }

    private void parseFile(Path path, StringStateMachineReader<S, E, C> reader) throws IOException {
        try (Scanner scanner = new Scanner(path)) {
            boolean validState = false;
            S state = null;
            while (scanner.hasNextLine()) {
                // Remove comments from the end of lines
                String fullLine = scanner.nextLine();
                String[] split = fullLine.split("#", 2);
                String line = split[0];

                if (!line.trim().isEmpty()) {
                    // The line contains state machine structure information
                    if (!line.startsWith(" ") && !line.startsWith("\t")) {
                        // A line that is not indented in anyway is a state
                        log.trace("Parsing line as state.  Line[{}]", fullLine);
                        state = parseState(reader, line);
                        validState = true;
                    } else if (validState) {
                        // An indented line, if it comes directly after a state, is treated as a transition
                        log.trace("Parsing line as transition.  Line[{}]", fullLine);
                        parseTransition(state, reader, line);
                    } else {
                        // Indented line that is at beginning of the file or did not come directly after a state
                        log.trace("Ignoring line.  Line[{}]", fullLine);
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
        String[] split = line.split(":|/");
        int index = 0;

        String stateString = split[index].trim();
        S state = stateMachineReader.readState(stateString);
        InternalState<S, E, C> internalState = this.getState(state);
        index++;
        log.trace("Found state [{}]", stateString);

        if (line.contains(":")) {
            String parentString = split[index].trim();
            S parent = stateMachineReader.readState(parentString);

            InternalState<S, E, C> internalParent = this.getState(parent);
            internalParent.addChild(internalState);
            internalState.setParentState(internalParent);

            index++;
            log.trace("Found parent state [{}]", parentString);
        }

        if (line.contains("/")) {
            String entryActionString = split[index].trim();
            if (!entryActionString.isEmpty()) {
                Action<S, E, C> entryAction = stateMachineReader.readStateAction(state, ActionType.Entrance,
                                                                                 entryActionString);
                internalState.addEntranceAction(entryAction);
                log.trace("Found entry action [{}]", entryActionString);
            }
            index++;
        }

        if (index < split.length) {
            String exitActionString = split[index].trim();
            Action<S, E, C> exitAction = stateMachineReader.readStateAction(state, ActionType.Exit, exitActionString);
            internalState.addExitAction(exitAction);
            index++;
            log.trace("Found exit action [{}]", exitActionString);
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
        String[] split = line.split("->|\\[|/");
        int index = 0;

        String eventString = split[index].trim();
        E event = stateMachineReader.readEvent(eventString);
        index++;
        log.trace("Found transition event [{}]", eventString);

        S destination;
        if (line.contains("->")) {
            String stateString = split[index].trim();
            destination = stateMachineReader.readState(stateString);
            index++;
            log.trace("Found transition state [{}]", stateString);
        } else {
            destination = state;
            log.trace("Creating re-entrant transition to [{}]", state);
        }

        TransitionGuard<S, E, C> transitionGuard;
        if (line.contains("[")) {
            String guard = split[index];
            int guardEnd = guard.lastIndexOf(']');
            if (guardEnd > 0) {
                guard = guard.substring(0, guardEnd);
            } // else - no guard ending - that's fine
            guard = guard.trim();
            transitionGuard = stateMachineReader.readTransitionGuard(state, event, destination, guard);
            index++;
            log.trace("Found transition guard [{}]", guard);
        } else {
            transitionGuard = TransitionGuard.none();
        }

        final Action<S, E, C> transitionAction;
        if (line.contains("/")) {
            String transitionActionString = split[index].trim();
            transitionAction = stateMachineReader.readTransitionAction(state, event, destination,
                                                                       transitionActionString);
            index++;
            log.trace("Found transition action [{}]", transitionActionString);
        } else {
            transitionAction = Action.noAction();
        }

        log.trace("Adding state transition.  Event [{}] State [{}]", event, destination);
        this.addTransition(event, new Transition<>(state, () -> destination, transitionGuard, transitionAction));
    }
}
