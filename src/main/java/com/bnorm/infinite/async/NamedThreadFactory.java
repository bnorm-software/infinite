package com.bnorm.infinite.async;

import java.util.concurrent.ThreadFactory;

/**
 * A factory interface for named threads.
 *
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class NamedThreadFactory implements ThreadFactory {

    /** The name used for all threads. */
    private final String name;

    /** The current count of the thread number. */
    private int counter;

    /**
     * Constructs a new named thread factory with the give thread name.
     *
     * @param name the name to use for all threads.
     */
    protected NamedThreadFactory(String name) {
        this.name = name;
        this.counter = -1;
    }

    @Override
    public Thread newThread(Runnable r) {
        counter++;
        return new Thread(r, name + "[" + counter + "]");
    }
}
