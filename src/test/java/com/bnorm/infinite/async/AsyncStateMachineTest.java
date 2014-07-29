package com.bnorm.infinite.async;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.bnorm.infinite.StateMachineStructureFactory;
import com.bnorm.infinite.Transition;
import com.bnorm.infinite.TransitionGuard;
import com.bnorm.infinite.builders.AsyncStateMachineBuilder;
import com.bnorm.infinite.builders.AsyncStateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for {@link AsyncStateMachine}
 *
 * @author Brian Norman
 * @version 1.1.0
 * @since 1.1.0
 */
public class AsyncStateMachineTest {

    /**
     * JUnit test for {@link AsyncStateMachine#isRunning()}, {@link AsyncStateMachine#run()}, and {@link
     * AsyncStateMachine#stop()}.
     *
     * @throws InterruptedException if thread sleeping is interrupted.
     */
    @Test
    public void testRunning() throws InterruptedException {
        AsyncStateMachine<?, ?, ?> stateMachine = new AsyncStateMachineBase<>(
                StateMachineStructureFactory.createDefault(), null, null);

        Assert.assertFalse(stateMachine.isRunning());

        Thread thread = new Thread(stateMachine);
        thread.start();
        Thread.sleep(5);

        Assert.assertTrue(stateMachine.isRunning());

        stateMachine.stop();
        Thread.sleep(5);

        Assert.assertFalse(stateMachine.isRunning());

        thread = new Thread(stateMachine);
        thread.start();
        Thread.sleep(5);

        Assert.assertTrue(stateMachine.isRunning());

        stateMachine.stop();
        Thread.sleep(5);

        Assert.assertFalse(stateMachine.isRunning());
    }

    /**
     * JUnit test for {@link AsyncStateMachine#fire(Object)}, {@link AsyncStateMachine#submit(Object)}, and {@link
     * AsyncStateMachine#inject(Object)}.
     *
     * @throws InterruptedException on Future get.
     * @throws ExecutionException on Future get.
     */
    @Test
    public void testSubmit() throws ExecutionException, InterruptedException {
        // Turnstile state machine
        AsyncStateMachineBuilder<String, String, Void> turnstileBuilder = AsyncStateMachineBuilderFactory.createDefault();
        turnstileBuilder.configure("Locked").handle("coin", "Unlocked");
        turnstileBuilder.configure("Unlocked").handle("push", "Locked");
        AsyncStateMachine<String, String, Void> turnstile = turnstileBuilder.build("Locked", null);

        Future<Optional<Transition<String, Void>>> futureTurnstileTransition1 = turnstile.submit("coin");
        Future<Optional<Transition<String, Void>>> futureTurnstileTransition2 = turnstile.submit("coin");
        Future<Optional<Transition<String, Void>>> futureTurnstileTransition3 = turnstile.submit("push");
        Future<Optional<Transition<String, Void>>> futureTurnstileTransition4 = turnstile.submit("push");

        Thread turnstileThread = new Thread(turnstile);
        turnstileThread.start();

        Optional<Transition<String, Void>> turnstileTransition1 = futureTurnstileTransition1.get();
        Optional<Transition<String, Void>> turnstileTransition2 = futureTurnstileTransition2.get();
        Optional<Transition<String, Void>> turnstileTransition3 = futureTurnstileTransition3.get();
        Optional<Transition<String, Void>> turnstileTransition4 = futureTurnstileTransition4.get();


        Assert.assertTrue(turnstileTransition1.isPresent());
        Assert.assertEquals("Locked", turnstileTransition1.get().getSource());
        Assert.assertEquals("Unlocked", turnstileTransition1.get().getDestination());
        Assert.assertEquals(TransitionGuard.<Void>none(), turnstileTransition1.get().getGuard());

        Assert.assertFalse(turnstileTransition2.isPresent());

        Assert.assertTrue(turnstileTransition3.isPresent());
        Assert.assertEquals("Unlocked", turnstileTransition3.get().getSource());
        Assert.assertEquals("Locked", turnstileTransition3.get().getDestination());
        Assert.assertEquals(TransitionGuard.<Void>none(), turnstileTransition3.get().getGuard());

        Assert.assertFalse(turnstileTransition4.isPresent());

        turnstile.stop();


        // DVD Player state machine
        AtomicBoolean containsDVD = new AtomicBoolean(false);
        AsyncStateMachineBuilder<String, String, AtomicBoolean> dvdplayerBuilder = AsyncStateMachineBuilderFactory.createDefault();
        dvdplayerBuilder.configure("Stopped").handle("play", "Playing", AtomicBoolean::get);
        dvdplayerBuilder.configure("Active").handle("stop", "Stopped");
        dvdplayerBuilder.configure("Playing").childOf("Active").handle("pause", "Paused");
        dvdplayerBuilder.configure("Paused").childOf("Active").handle("play", "Playing");
        AsyncStateMachine<String, String, AtomicBoolean> dvdplayer = dvdplayerBuilder.build("Stopped", containsDVD);

        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition1 = dvdplayer.submit("play");
        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition2 = dvdplayer.submit("pause");

        Thread dvdplayerThread = new Thread(dvdplayer);
        dvdplayerThread.start();

        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition1 = futureDVDPlayerTransition1.get();
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition2 = futureDVDPlayerTransition2.get();
        Assert.assertFalse(dvdplayerTransition1.isPresent());
        Assert.assertFalse(dvdplayerTransition2.isPresent());

        dvdplayer.stop();
        containsDVD.set(true);

        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition3 = dvdplayer.submit("play");
        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition4 = dvdplayer.submit("pause");
        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition5 = dvdplayer.inject("stop");

        dvdplayerThread = new Thread(dvdplayer);
        dvdplayerThread.start();

        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition3 = futureDVDPlayerTransition3.get();
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition4 = futureDVDPlayerTransition4.get();
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition5 = futureDVDPlayerTransition5.get();
        Assert.assertTrue(dvdplayerTransition3.isPresent());
        Assert.assertEquals("Stopped", dvdplayerTransition3.get().getSource());
        Assert.assertEquals("Playing", dvdplayerTransition3.get().getDestination());
        Assert.assertTrue(dvdplayerTransition4.isPresent());
        Assert.assertEquals("Playing", dvdplayerTransition4.get().getSource());
        Assert.assertEquals("Paused", dvdplayerTransition4.get().getDestination());
        Assert.assertFalse(dvdplayerTransition5.isPresent());

        dvdplayer.stop();
        dvdplayerThread = new Thread(dvdplayer);
        dvdplayerThread.start();

        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition6 = dvdplayer.submit("play");
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition7 = dvdplayer.fire("pause");

        dvdplayer.stop();
        dvdplayerThread = new Thread(dvdplayer);

        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition8 = dvdplayer.submit("play");
        Future<Optional<Transition<String, AtomicBoolean>>> futureDVDPlayerTransition9 = dvdplayer.inject("stop");

        dvdplayerThread.start();

        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition6 = futureDVDPlayerTransition6.get();
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition8 = futureDVDPlayerTransition8.get();
        Optional<Transition<String, AtomicBoolean>> dvdplayerTransition9 = futureDVDPlayerTransition9.get();

        Assert.assertTrue(dvdplayerTransition6.isPresent());
        Assert.assertEquals("Paused", dvdplayerTransition6.get().getSource());
        Assert.assertEquals("Playing", dvdplayerTransition6.get().getDestination());
        Assert.assertTrue(dvdplayerTransition7.isPresent());
        Assert.assertEquals("Playing", dvdplayerTransition7.get().getSource());
        Assert.assertEquals("Paused", dvdplayerTransition7.get().getDestination());
        Assert.assertTrue(dvdplayerTransition8.isPresent());
        Assert.assertEquals("Stopped", dvdplayerTransition8.get().getSource());
        Assert.assertEquals("Playing", dvdplayerTransition8.get().getDestination());
        Assert.assertTrue(dvdplayerTransition9.isPresent());
        Assert.assertEquals("Active", dvdplayerTransition9.get().getSource());
        Assert.assertEquals("Stopped", dvdplayerTransition9.get().getDestination());
    }
}
