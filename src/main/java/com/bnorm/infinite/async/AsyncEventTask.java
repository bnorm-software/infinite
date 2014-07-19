package com.bnorm.infinite.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The asynchronous event task is how events are processed in an asynchronous state machine.  This is how events or
 * sorted in the priority queue that defines the order by which events are processed.  This task can be treated in
 * exactly the same way as a RunnableFuture thanks to the delegation to a FutureTask.
 *
 * @param <E> the class type of the events.
 * @param <R> the class type of the result of the event.
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class AsyncEventTask<E, R> implements RunnableFuture<R>, Comparable<AsyncEventTask<E, R>> {

    /** The asynchronous state machine event to process. */
    private final E event;

    /** The priority of the asynchronous state machine event. */
    private final long priority;

    /** The FutureTask used to process the asynchronous state machine event. */
    private final FutureTask<R> task;

    /**
     * Constructs a new asynchronous event task with the specified event, priority, and process definition.
     *
     * @param event the event to process.
     * @param priority the priority of the event.
     * @param callable how to process the event.
     */
    protected AsyncEventTask(E event, long priority, Callable<R> callable) {
        this.event = event;
        this.priority = priority;
        this.task = new FutureTask<>(callable);
    }

    /**
     * Returns the event of the asynchronous event task.
     *
     * @return the event.
     */
    public E getEvent() {
        return event;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return task.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public boolean isDone() {
        return task.isDone();
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        return task.get();
    }

    @Override
    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return task.get(timeout, unit);
    }

    @Override
    public int compareTo(AsyncEventTask<E, R> task) {
        return Long.compare(priority, task.priority);
    }

    @Override
    public String toString() {
        return Long.toString(priority);
    }
}
