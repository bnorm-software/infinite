## Infinite [![Build Status](https://travis-ci.org/bnorm-software/infinite.svg?branch=master)](https://travis-ci.org/bnorm-software/infinite) [![Coverage Status](https://coveralls.io/repos/bnorm-software/infinite/badge.png?branch=master)](https://coveralls.io/r/bnorm-software/infinite?branch=master) ##

Infinite is a quantum hierarchical finite state machine for Java.  The library is designed to be lightweight yet full featured.

## Maven ##
Infinite can be found in Maven Central thanks to Sonatype OSSRH.

```xml
<dependency>
    <groupId>com.bnorm</groupId>
    <artifactId>infinite</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Examples ##

### Turnstile ###
This example will introduce a very basic state machine and how to create it with just a few lines of code.  This is the
turnstile example provided on [Wikipedia](http://en.wikipedia.org/wiki/Finite-state_machine#Example:_a_turnstile).  It
consists of two states, 'Locked' and 'Unlocked', two events, 'coin' and 'push', and has no context.

```java
// State type is String, event type is String, and there is no context
StateMachineBuilder<String, String, Void> builder = StateMachineBuilderFactory.createDefault();
// Push events are not handled and will simply be ignored
builder.configure("Locked")
       .handle("coin", "Unlocked");
// Coin events are not handled and will simply be ignored
builder.configure("Unlocked")
       .handle("push", "Locked");
// Build state machine with a starting state of locked and a null context
StateMachine<String, String, Void> turnstile = builder.build("Locked", null);
turnstile.fire("coin");
turnstile.fire("push");
```

### DVD Player ###
This example shows some of the power in a hierarchical state machine.  There are two main states and two children states
in this state machine.  The 'Active' state, which is the parent of 'Playing' and 'Paused', handles the 'stop' event so
it doesn't need to be handled for each state.  For this specific example it would probably be easier to have both
'Playing' and 'Paused' handle the 'stop' event but that doesn't provide a very good example, now does it?

```java
AtomicBoolean containsDVD = new AtomicBoolean(false);
StateMachineBuilder<String, String, AtomicBoolean> builder = StateMachineBuilderFactory.createDefault();
builder.configure("Stopped")
       .handle("play", "Playing", AtomicBoolean::get);
builder.configure("Active")
       .handle("stop", "Stopped");
// Playing and Paused are both children of Active
builder.configure("Playing")
       .childOf("Active")
       .handle("pause", "Paused");
builder.configure("Paused")
       .childOf("Active")
       .handle("play", "Playing");
StateMachine<String, String, AtomicBoolean> dvdplayer = builder.build("Stopped", null);
// Add a transition listener to print out each event and transition
dvdplayer.addTransitionListener((stage, event, transition, context) -> {
    System.out.println(event + " generated " + transition);
});
dvdplayer.fire("play"); // Does nothing
containsDVD.set(true);
dvdplayer.fire("play");
dvdplayer.fire("pause");
dvdplayer.fire("stop");
```

## Releases ##

### Version 1.0.0 ###
 - Supports hierarchical state machine designs
 - State machine context
 - Entrance and exit actions for states 
 - Guarded state transitions
 - State machine transition listeners
 - Flexible typing for states, events, and context
 - Easy to use state machine builder classes

### Version 1.0.1/2 ###
 - (#45) Factories not defining type at class level does not allow custom type factories

### Version 1.1.0 ###
 - Asynchronous, thread safe, state machine
 - Submit events to be processed when other events have finished
 - Asynchronous entrance and exit actions
 - Asynchronous transition listener factory
 - State machine structure to back all state machines

## Future ##

### Version 1.2.0 ###
 - Load a state machine structure from a text file
 - Save a state machine structure to a text file
 - Your suggestion here!
