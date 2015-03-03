package com.bnorm.infinite.file.xml;

import java.io.IOException;
import java.nio.file.Path;
import javax.xml.bind.JAXBException;

import com.bnorm.infinite.InternalStateFactory;
import com.bnorm.infinite.StateMachineException;
import com.bnorm.infinite.StateMachineStructure;
import com.bnorm.infinite.StateMachineStructureFactory;
import com.bnorm.infinite.TransitionFactory;
import com.bnorm.infinite.file.StringStateMachineReader;

/**
 * todo A factory for creating state machine structures for a file.
 *
 * @param <S> the class type of the states.
 * @param <E> the class type of the events.
 * @param <C> the class type of the context.
 * @author Brian Norman
 * @since 1.2.0
 */
public class XMLStateMachineStructureFactory<S, E, C> implements StateMachineStructureFactory<S, E, C> {

    /** File location of the state machine text. */
    private final Path path;

    /** Reader used to interpret the state machine text. */
    private final StringStateMachineReader<S, E, C> stateMachineReader;

    /**
     * Constructors a new FileStateMachineStructureFactory with the specified parameters.
     *
     * @param path the file location of the state machine text.
     * @param stateMachineReader the reader used to load the state machine.
     */
    public XMLStateMachineStructureFactory(Path path, StringStateMachineReader<S, E, C> stateMachineReader) {
        this.path = path;
        this.stateMachineReader = stateMachineReader;
    }

    @Override
    public StateMachineStructure<S, E, C> create(InternalStateFactory<S, E, C> internalStateFactory,
                                                 TransitionFactory<S, E, C> transitionFactory) {
        try {
            return new XMLStateMachineStructure<>(internalStateFactory, transitionFactory, path, stateMachineReader);
        } catch (IOException | JAXBException e) {
            throw new StateMachineException("Unable to read specified path [" + path + "]", e);
        }
    }
}
