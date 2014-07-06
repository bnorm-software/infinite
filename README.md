## Infinite [![Build Status](https://travis-ci.org/bnorm-software/infinite.svg?branch=master)](https://travis-ci.org/bnorm-software/infinite) ##
Infinite is a hierarchical finite state machine for Java.  The library is designed to be lightweight yet full featured.

## Maven ##
Infinite is not yet part of Maven Central.

## Examples ##

### Turnstile ###
This example will introduce a very basic state machine and how to create it with just a few lines of code.  This is the
turnstile example provided on [Wikipedia](http://en.wikipedia.org/wiki/Finite-state_machine#Example:_a_turnstile).  It
consists of two states, 'Locked' and 'Unlocked', two events, 'coin' and 'push', and has no context.

```java
// State type is String, event type is String, and there is no context
StateMachineBuilder<String, String, Void> builder = StateMachineBuilderFactory.create();
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
StateMachineBuilder<String, String, Void> builder = StateMachineBuilderFactory.create();
builder.configure("Stopped")
       .handle("play", "Playing");
builder.configure("Active")
       .handle("stop", "Stopped");
// Playing and Paused are both children of Active
builder.configure("Playing")
       .childOf("Active")
       .handle("pause", "Paused");
builder.configure("Paused")
       .childOf("Active")
       .handle("play", "Playing");
StateMachine<String, String, Void> dvdplayer = builder.build("Stopped", null);
// Add a transition listener to print out each event and transition
dvdplayer.addTransitionListener((event, transition, context) -> {
    System.out.println(event + " generated " + transition);
});
dvdplayer.fire("play");
dvdplayer.fire("pause");
dvdplayer.fire("stop");
```

## Features ##

### Version 1.0.0 ###
**Initial release**
 - Supports hierarchical state machine designs
 - State machine context
 - Entrance and exit actions for states 
 - Guarded state transitions
 - State machine transition listeners
 - Flexible typing for states, events, and context
 - Easy to use state machine builder classes

## Future ##

### Version 1.1.0 ###
**Asynchronous release**
 - Thread safe state machine
 - Asynchronous entrance and exit actions
 - Submit events to be processed when other events have finished
