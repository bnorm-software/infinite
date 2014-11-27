## Infinite File State Machine Structure ##

Infinite has a way to define a state machine structure via a standard text file.  This definition uses some basic text
property file standard such as comment lines beginning with a pound sign (`#`).  There are a few other special
characters that are used in the specification.  A lot of these will make more sense after an example is presented but
for reference they are listed here.

 * Comment (`#`) - The pound sign is used to denote a comment and any text following this sign is ignored.  If the pound
sign is the first non-whitespace character in a line, the whole line is ignored.
 * Parent state (`:`) - The colon starts the definition of a parent state.
 * Action (`/`) - A slash starts an action definition, whether it be a state action or a transition action.  For state
actions, the first action is the entry action, and the second is the exit action.  If the entry action is only
whitespace, it will not be converted into an action.
 * Destination state (`->`) - An arrow denotes the destination state.
 * Transition guard (`[` and `]`) -

### State definition ###

A state is defined by a unique string of text that represents the state name.  This text must start the line and not
contain any of the state machine definition special characters.  The state name can contain whitespace but leading and
trailing whitespace is trimmed.  States can be repeated if more entry or exit actions need to be added, however, if a
parent state is specified in both definitions, the later parent state will override the first.

To specify a parent state, follow the state name with a colon (`:`) and another state name.  The same rules about
whitespace apply to the parent state name as the state name.

Adding an entry or exit action is as simple as following the parent state name - or the state name if there is no parent
state - with a slash and then the action text.  The first action is always the entry action and a second slash followed
by action text is always the exit action.  If no entry action is desired, then simply leave the action text empty or as
all whitespace.

Examples:

```
state 1
state 2 : state 1
state 3 / entrance action
state 4 / entrance action / exit action
state 5 // exit action
state 6 : state 2 / entrance action
state 7 : state 3 / entrance action / exit action
state 7           /                 / exit action
```

### Transition definition ###

Transitions are defined on the lines immediately following a state definition.  To differentiate from a state
definition, a transition must start with whitespace.  A transition is made up of up to 4 parts, an event, destination
state, transition guard, and transition action, but only the event is required to be considered a valid transition.

The first text in a transition line, after the leading white space, is the event name.  The event name can contain any
number of characters including whitespace but excluding the special characters.

Next is the destination state special character follow by the state name.  This state does _not_ need to be defined at
this point as it will be created on the fly as needed.  This has the draw back of not throwing an error if the state
does not exist so care must be taken to validate state names before loading.  If no destination state is specified, the
event is considered a reentrant transition and the destination state is considered the source state.

Then comes the transition guard.  A transition guard is surrounded but the transition guard special characters.  The
text of the transition guard can contain any number of whitespaces but leading and trailing whitespace will be trimmed.
In fact, the ending square bracket is not even required, but when specified it is used to end the transition guard text.

Finally is the transition action.  The transition action is specified with a slash and then the action text.  This is
the same as the state entrance and exit action definition.

Examples:

```
state n
   event 1 -> state 1
   event 2 -> state 2 [guard]
   event 3 -> state 3 [ guard ] / action
   event 4 -> state 4           / action
   event 5
```

### Complete example ###

```
# state machine structure definition example
# states, events, actions, and guards (state machine objects) can contain spaces

State 1 : Parent 1 / entry action / exit action # comments can go at the end of a line too
   Event 2 -> State 2 / action
   Event 3 -> State 3 [ guard ] # the guard ending ']' is not required but it makes it more readable
   # transition comment
   Event 4->State 4[guard]/action # extra whitespaces are not required and are trimmed before reading

   Event 5 -> State 5

State 2 // exit # this is how you would specify only an exit action
    EventReentrant
State 3 /              / exit # white space is ignored when not part of a state machine object

State 4     /     entry      # only an entrance action
   Event 1        ->     State 1



# all these extra empty lines are ignored
State 5      :     State 1
```

### File parsing ###

TODO.
