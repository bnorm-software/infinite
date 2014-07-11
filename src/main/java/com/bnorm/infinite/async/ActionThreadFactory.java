package com.bnorm.infinite.async;

import java.util.concurrent.ThreadFactory;

/**
 * A factory interface for action threads.
 *
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class ActionThreadFactory implements ThreadFactory {

    /** The name used for all threads. */
    private final String name;

    /** The current count of the thread number. */
    private int counter;

    /**
     * Constructs a new action thread factory with the give thread name.
     *
     * @param name the name to use for all threads.
     */
    protected ActionThreadFactory(String name) {
        this.name = name;
        this.counter = -1;
    }

    public Thread newThread(Runnable r) {
        counter++;
        return new Thread(r, name + "[" + counter + "]");
    }
}
