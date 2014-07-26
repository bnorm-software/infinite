package com.bnorm.infinite;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bnorm.infinite.builders.StateMachineBuilder;
import com.bnorm.infinite.builders.StateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for {@link StateMachine}
 *
 * @author Brian Norman
 * @version 1.0
 * @since 1.0
 */
public class StateMachineTest {

    /**
     * JUnit test for {@link StateMachine#getState()}.
     */
    @Test
    public void testState() {
        StateMachineBuilder<String, String, Void> builder = StateMachineBuilderFactory.create();
        StateMachine<String, String, Void> machine;
        builder.configure("State1").handle("event1", "State2");
        builder.configure("State2").handle("event2", "State1");

        machine = builder.build("State1", null);

        Assert.assertEquals("State1", machine.getState());
        Assert.assertNotEquals("State2", machine.getState());

        machine = builder.build("State2", null);

        Assert.assertNotEquals("State1", machine.getState());
        Assert.assertEquals("State2", machine.getState());

        machine = builder.build("Fake", null);

        Assert.assertNotEquals("State1", machine.getState());
        Assert.assertNotEquals("State2", machine.getState());
    }

    /**
     * JUnit test for {@link StateMachine#getContext()}.
     */
    @Test
    public void testContext() {
        StateMachineBuilder<String, String, String> builder = StateMachineBuilderFactory.create();
        builder.configure("State1")
               .handle("event1", "State2")
               .onEntry((state, event, transition, context) -> Assert.assertEquals("Context", context))
               .onExit((state, event, transition, context) -> Assert.assertEquals("Context", context));
        builder.configure("State2")
               .handle("event2", "State1")
               .onEntry((state, event, transition, context) -> Assert.assertEquals("Context", context))
               .onExit((state, event, transition, context) -> Assert.assertEquals("Context", context));
        StateMachine<String, String, String> machine = builder.build("State1", "Context");
        machine.addTransitionListener((stage, event, transition, context) -> Assert.assertEquals("Context", context));

        Assert.assertEquals("Context", machine.getContext());

        machine.fire("event2"); // to State1

        Assert.assertEquals("Context", machine.getContext());

        machine.fire("event1"); // to State2

        Assert.assertEquals("Context", machine.getContext());

        machine.fire("event1"); // to State2

        Assert.assertEquals("Context", machine.getContext());

        machine.fire("event2"); // to State1

        Assert.assertEquals("Context", machine.getContext());
    }

    /**
     * JUnit test for {@link StateMachine#fire(Object)}.
     */
    @Test
    public void testFire() {
        // Turnstile state machine
        StateMachineBuilder<String, String, Void> turnstileBuilder = StateMachineBuilderFactory.create();
        turnstileBuilder.configure("Locked").handle("coin", "Unlocked");
        turnstileBuilder.configure("Unlocked").handle("push", "Locked");
        StateMachine<String, String, Void> turnstile = turnstileBuilder.build("Locked", null);
        Optional<Transition<String, Void>> turnstileTransition;

        turnstileTransition = turnstile.fire("coin");
        Assert.assertTrue(turnstileTransition.isPresent());
        Assert.assertEquals("Locked", turnstileTransition.get().getSource());
        Assert.assertEquals("Unlocked", turnstileTransition.get().getDestination());
        Assert.assertEquals(TransitionGuard.<Void>none(), turnstileTransition.get().getGuard());

        turnstileTransition = turnstile.fire("coin");
        Assert.assertFalse(turnstileTransition.isPresent());

        turnstileTransition = turnstile.fire("push");
        Assert.assertTrue(turnstileTransition.isPresent());
        Assert.assertEquals("Unlocked", turnstileTransition.get().getSource());
        Assert.assertEquals("Locked", turnstileTransition.get().getDestination());
        Assert.assertEquals(TransitionGuard.<Void>none(), turnstileTransition.get().getGuard());

        turnstileTransition = turnstile.fire("push");
        Assert.assertFalse(turnstileTransition.isPresent());


        // DVD Player state machine
        AtomicBoolean containsDVD = new AtomicBoolean(false);
        StateMachineBuilder<String, String, AtomicBoolean> dvdplayerBuilder = StateMachineBuilderFactory.create();
        dvdplayerBuilder.configure("Stopped").handle("play", "Playing", AtomicBoolean::get);
        dvdplayerBuilder.configure("Active").handle("stop", "Stopped");
        dvdplayerBuilder.configure("Playing").childOf("Active").handle("pause", "Paused");
        dvdplayerBuilder.configure("Paused").childOf("Active").handle("play", "Playing");
        StateMachine<String, String, AtomicBoolean> dvdplayer = dvdplayerBuilder.build("Stopped", containsDVD);
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition;

        dvdplayerTransition = dvdplayer.fire("play");
        Assert.assertFalse(dvdplayerTransition.isPresent());

        containsDVD.set(true);
        dvdplayerTransition = dvdplayer.fire("play");
        Assert.assertTrue(dvdplayerTransition.isPresent());
        Assert.assertEquals("Stopped", dvdplayerTransition.get().getSource());
        Assert.assertEquals("Playing", dvdplayerTransition.get().getDestination());

        dvdplayerTransition = dvdplayer.fire("play");
        Assert.assertFalse(dvdplayerTransition.isPresent());

        dvdplayerTransition = dvdplayer.fire("pause");
        Assert.assertTrue(dvdplayerTransition.isPresent());
        Assert.assertEquals("Playing", dvdplayerTransition.get().getSource());
        Assert.assertEquals("Paused", dvdplayerTransition.get().getDestination());

        dvdplayerTransition = dvdplayer.fire("pause");
        Assert.assertFalse(dvdplayerTransition.isPresent());

        dvdplayerTransition = dvdplayer.fire("stop");
        Assert.assertTrue(dvdplayerTransition.isPresent());
        Assert.assertEquals("Active", dvdplayerTransition.get().getSource());
        Assert.assertEquals("Stopped", dvdplayerTransition.get().getDestination());

        dvdplayerTransition = dvdplayer.fire("stop");
        Assert.assertFalse(dvdplayerTransition.isPresent());

        dvdplayerTransition = dvdplayer.fire("pause");
        Assert.assertFalse(dvdplayerTransition.isPresent());

        dvdplayerTransition = dvdplayer.fire("play");
        Assert.assertTrue(dvdplayerTransition.isPresent());
        Assert.assertEquals("Stopped", dvdplayerTransition.get().getSource());
        Assert.assertEquals("Playing", dvdplayerTransition.get().getDestination());

        dvdplayerTransition = dvdplayer.fire("stop");
        Assert.assertTrue(dvdplayerTransition.isPresent());
        Assert.assertEquals("Active", dvdplayerTransition.get().getSource());
        Assert.assertEquals("Stopped", dvdplayerTransition.get().getDestination());
    }
}
