package com.bnorm.infinite.signals;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A signal is a way of describing an event with some extra, optional, information.  A state machine designer can use a
 * signal to be able to handle when a fired event needs to contain more information then just the event itself.  This
 * can be useful in a lot of situations.  For example, when a keyboard key is press, the signal could be the event that
 * a key was pressed and the optional information could be what key.
 *
 * <p>To use this signal in a state machine, the event must be specified as being a signal of the specified event type
 * and optional parameter type.
 *
 * <pre>{@code
 * StateMachine<State, Signal<Event, Parameter>, Context> stateMachine;
 * }</pre>
 *
 * This class wraps an event type with an Optional delegation.  All signals with equal events are considered equal but
 * all optional like methods are delegated to the specified parameter.  One might think of this class like a keyed
 * optional.
 *
 * <p>This class is not designed to be fully featured.  It is only designed to give a developer an idea of how one might
 * solve the problem of parameter passing with a state machine event.  If more parameters are required, another signal
 * like class should be created and used.
 *
 * @param <E> the class type of the events.
 * @param <T> the class type of the signal value.
 * @author Brian Norman
 * @since 1.1.0
 */
public class Signal<E, T> {

    /** The signal event */
    private final E event;

    /** The signal parameter value. */
    private final Optional<T> optional;

    /**
     * Constructs a signal with the specified event.
     *
     * @param event the signal event.
     */
    protected Signal(E event) {
        this.event = event;
        this.optional = Optional.empty();
    }

    /**
     * Constructs a signal with the specified event and parameter value.
     *
     * @param event the signal event.
     * @param value the signal parameter value.
     */
    protected Signal(E event, T value) {
        this.event = event;
        this.optional = Optional.of(value);
    }

    /**
     * Creates and returns a new signal with the specified event.
     *
     * @param event the signal event.
     * @return a new event signal.
     */
    public static <E, T> Signal<E, T> of(E event) {
        return new Signal<>(event);
    }

    /**
     * Creates and returns a new signal with the specified event and parameter value.
     *
     * @param event the signal event.
     * @param value the signal parameter value.
     * @return a new event signal.
     */
    public static <E, T> Signal<E, T> of(E event, T value) {
        return new Signal<>(event, value);
    }

    /**
     * Returns the signal event.
     *
     * @return the signal event.
     */
    public E getEvent() {
        return event;
    }

    /**
     * Delegation to {@link java.util.Optional#get()}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws java.util.NoSuchElementException if there is no value present
     * @see Signal#isPresent()
     */
    public T get() {
        return optional.get();
    }

    /**
     * Delegation to {@link java.util.Optional#isPresent()}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return optional.isPresent();
    }

    /**
     * Delegation to {@link java.util.Optional#ifPresent(java.util.function.Consumer)}.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is null
     */
    public void ifPresent(Consumer<? super T> consumer) {
        optional.ifPresent(consumer);
    }

    /**
     * Delegation to {@link java.util.Optional#filter(java.util.function.Predicate)}.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an {@code Optional} describing the value of this {@code Optional} if a value is present and the value
     * matches the given predicate, otherwise an empty {@code Optional}
     * @throws NullPointerException if the predicate is null
     */
    public Optional<T> filter(Predicate<? super T> predicate) {
        return optional.filter(predicate);
    }

    /**
     * Delegation to {@link java.util.Optional#map(java.util.function.Function)}.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an {@code Optional} describing the result of applying a mapping function to the value of this {@code
     * Optional}, if a value is present, otherwise an empty {@code Optional}
     * @throws NullPointerException if the mapping function is null
     */
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return optional.map(mapper);
    }

    /**
     * Delegation to {@link java.util.Optional#flatMap(java.util.function.Function)}.
     *
     * @param <U> The type parameter to the {@code Optional} returned by
     * @param mapper a mapping function to apply to the value, if present the mapping function
     * @return the result of applying an {@code Optional}-bearing mapping function to the value of this {@code
     * Optional}, if a value is present, otherwise an empty {@code Optional}
     * @throws NullPointerException if the mapping function is null or returns a null result
     */
    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return optional.flatMap(mapper);
    }

    /**
     * Delegation to {@link java.util.Optional#orElse(Object)}.
     *
     * @param other the value to be returned if there is no value present, may be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return optional.orElse(other);
    }

    /**
     * Delegation to {@link java.util.Optional#orElseGet(java.util.function.Supplier)}.
     *
     * @param other a {@code Supplier} whose result is returned if no value is present
     * @return the value if present otherwise the result of {@code other.get()}
     * @throws NullPointerException if value is not present and {@code other} is null
     */
    public T orElseGet(Supplier<? extends T> other) {
        return optional.orElseGet(other);
    }

    /**
     * Delegation to {@link java.util.Optional#orElseThrow(java.util.function.Supplier)}.
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and {@code exceptionSupplier} is null
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return optional.orElseThrow(exceptionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Signal)) {
            return false;
        } else {
            Signal<?, ?> other = (Signal<?, ?>) obj;
            return Objects.equals(event, other.event);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(event);
    }

    @Override
    public String toString() {
        if (optional.isPresent()) {
            return "Signal[" + event + "," + optional + "]";
        } else {
            return "Signal[" + event + "]";
        }
    }
}
