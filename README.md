## FSM4J ##
FSM4J is a hierarchical finite state machine for Java.  The library is designed to be lightweight yet full featured.

Build status: [![Build Status](https://travis-ci.org/bnorm-software/fsm4j.svg?branch=master)](https://travis-ci.org/bnorm-software/fsm4j)

## Maven ##
FSM4J is not yet part of Maven Central.

## Examples ##

### Turnstile ###
This example will introduce a very basic state machine and how to create it with just a few lines of code.

#### Turnstile state machine graph ####
![Turnstile State Machine](http://upload.wikimedia.org/wikipedia/commons/9/9e/Turnstile_state_machine_colored.svg)

#### Turnstile state machine transition table ####
| Current State | Input | Next State | Output                                          |
| ------------- | ----- | ---------- | ----------------------------------------------- |
| Locked        | coin  | Unlocked   | Release turnstile so customer can push through  |
|               | push  | Locked     | None                                            |
| Unlocked      | coin  | Unlocked   | None                                            |
|               | push  | Locked     | When customer has pushed through lock turnstile |

#### Turnstile state machine sample code ####
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

#### DVD player state machine transition table ####
| Current State | Parent State | Input | Next State |
| ------------- | ------------ | ----- | ---------- |
| Stopped       | None         | play  | Playing    |
| Active        | None         | stop  | Stopped    |
| Playing       | Active       | pause | Paused     |
| Paused        | Active       | play  | Playing    |

#### Turnstile state machine sample code ####
```java
StateMachineBuilder<String, String, Void> builder = StateMachineBuilderFactory.create();
builder.configure("Stopped")
       .handle("play", "Playing", (context) -> context.containsDVD());
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
dvdplayer.addTransitionListener((event, transition, context) -> System.out.println(event + " generated " + transition));
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
