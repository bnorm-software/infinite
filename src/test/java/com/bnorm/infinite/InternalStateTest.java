package com.bnorm.infinite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit tests for {@link InternalState}
 *
 * @author Brian Norman
 * @since 1.0.0
 */
public class InternalStateTest {

    /**
     * JUnit test for {@link InternalState#getState()}.
     */
    @Test
    public void testGetState() {
        InternalState<String, Void, Void> state1 = new InternalStateBase<>("State1");
        Assert.assertEquals("State1", state1.getState());
        InternalState<String, Void, Void> state2 = new InternalStateBase<>("State2");
        Assert.assertEquals("State2", state2.getState());
    }

    /**
     * JUnit test for {@link InternalState#setParentState(InternalState)}, {@link InternalState#getParentState()},
     * {@link InternalState#isParent(Object)}, {@link InternalState#getChildrenStates()}, {@link
     * InternalState#addChild(InternalState)}, and {@link InternalState#isChild(Object)}.
     *
     * Tests the hierarchical nature of internal states.
     */
    @Test
    public void testHierarchy() {
        InternalState<String, Void, Void> grandparent = new InternalStateBase<>("Grandparent");
        InternalState<String, Void, Void> parent = new InternalStateBase<>("Parent");
        InternalState<String, Void, Void> child1 = new InternalStateBase<>("Child1");
        InternalState<String, Void, Void> child2 = new InternalStateBase<>("Child2");
        InternalState<String, Void, Void> child3 = new InternalStateBase<>("Child3");
        InternalState<String, Void, Void> child4 = new InternalStateBase<>("Child4");
        InternalState<String, Void, Void> grandchild1_1 = new InternalStateBase<>("Grandchild1-1");
        InternalState<String, Void, Void> grandchild1_2 = new InternalStateBase<>("Grandchild1-2");
        InternalState<String, Void, Void> grandchild2_1 = new InternalStateBase<>("Grandchild2-1");
        InternalState<String, Void, Void> grandchild4_1 = new InternalStateBase<>("Grandchild4-1");

        Set<InternalState<String, Void, Void>> parents = new LinkedHashSet<>();
        parents.add(parent);

        Set<InternalState<String, Void, Void>> children = new LinkedHashSet<>();
        children.add(child1);
        children.add(child2);
        children.add(child3);
        children.add(child4);

        Set<InternalState<String, Void, Void>> grandchildren1 = new LinkedHashSet<>();
        grandchildren1.add(grandchild1_1);
        grandchildren1.add(grandchild1_2);
        Set<InternalState<String, Void, Void>> grandchildren2 = new LinkedHashSet<>();
        grandchildren2.add(grandchild2_1);
        Set<InternalState<String, Void, Void>> grandchildren3 = new LinkedHashSet<>();
        Set<InternalState<String, Void, Void>> grandchildren4 = new LinkedHashSet<>();
        grandchildren4.add(grandchild4_1);

        // Parents have children

        child1.setParentState(parent);
        child2.setParentState(parent);
        child3.setParentState(parent);
        child4.setParentState(parent);
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);

        Assert.assertFalse(parent.getParentState().isPresent());
        Assert.assertTrue(child1.getParentState().isPresent());
        Assert.assertTrue(child2.getParentState().isPresent());
        Assert.assertTrue(child3.getParentState().isPresent());
        Assert.assertTrue(child4.getParentState().isPresent());

        Assert.assertTrue(child1.isParent(parent.getState()));
        Assert.assertTrue(child2.isParent(parent.getState()));
        Assert.assertTrue(child3.isParent(parent.getState()));
        Assert.assertTrue(child4.isParent(parent.getState()));

        Assert.assertEquals(parent, child1.getParentState().get());
        Assert.assertEquals(parent, child2.getParentState().get());
        Assert.assertEquals(parent, child3.getParentState().get());
        Assert.assertEquals(parent, child4.getParentState().get());

        Assert.assertTrue(parent.isChild(child1.getState()));
        Assert.assertTrue(parent.isChild(child2.getState()));
        Assert.assertTrue(parent.isChild(child3.getState()));
        Assert.assertTrue(parent.isChild(child4.getState()));

        Assert.assertEquals(parent.getChildrenStates(), children);

        // Parents also have parents - grandparents!

        parent.setParentState(grandparent);
        grandparent.addChild(parent);

        Assert.assertFalse(grandparent.getParentState().isPresent());
        Assert.assertTrue(parent.getParentState().isPresent());

        Assert.assertTrue(parent.isParent(grandparent.getState()));
        Assert.assertTrue(child1.isParent(grandparent.getState()));
        Assert.assertTrue(child2.isParent(grandparent.getState()));
        Assert.assertTrue(child3.isParent(grandparent.getState()));
        Assert.assertTrue(child4.isParent(grandparent.getState()));

        Assert.assertEquals(grandparent, parent.getParentState().get());

        Assert.assertTrue(grandparent.isChild(parent.getState()));
        Assert.assertTrue(grandparent.isChild(child1.getState()));
        Assert.assertTrue(grandparent.isChild(child2.getState()));
        Assert.assertTrue(grandparent.isChild(child3.getState()));
        Assert.assertTrue(grandparent.isChild(child4.getState()));

        Assert.assertEquals(grandparent.getChildrenStates(), parents);

        // And when they get older, children have their own children - grandchildren!

        grandchild1_1.setParentState(child1);
        grandchild1_2.setParentState(child1);
        grandchild2_1.setParentState(child2);
        grandchild4_1.setParentState(child4);
        child1.addChild(grandchild1_1);
        child1.addChild(grandchild1_2);
        child2.addChild(grandchild2_1);
        child4.addChild(grandchild4_1);

        Assert.assertTrue(grandchild1_1.getParentState().isPresent());
        Assert.assertTrue(grandchild1_2.getParentState().isPresent());
        Assert.assertTrue(grandchild2_1.getParentState().isPresent());
        Assert.assertTrue(grandchild4_1.getParentState().isPresent());

        Assert.assertTrue(grandchild1_1.isParent(child1.getState()));
        Assert.assertTrue(grandchild1_2.isParent(child1.getState()));
        Assert.assertTrue(grandchild2_1.isParent(child2.getState()));
        Assert.assertTrue(grandchild4_1.isParent(child4.getState()));
        Assert.assertTrue(grandchild1_1.isParent(parent.getState()));
        Assert.assertTrue(grandchild1_2.isParent(parent.getState()));
        Assert.assertTrue(grandchild2_1.isParent(parent.getState()));
        Assert.assertTrue(grandchild4_1.isParent(parent.getState()));
        Assert.assertTrue(grandchild1_1.isParent(grandparent.getState()));
        Assert.assertTrue(grandchild1_2.isParent(grandparent.getState()));
        Assert.assertTrue(grandchild2_1.isParent(grandparent.getState()));
        Assert.assertTrue(grandchild4_1.isParent(grandparent.getState()));

        Assert.assertEquals(child1, grandchild1_1.getParentState().get());
        Assert.assertEquals(child1, grandchild1_2.getParentState().get());
        Assert.assertEquals(child2, grandchild2_1.getParentState().get());
        Assert.assertEquals(child4, grandchild4_1.getParentState().get());

        Assert.assertTrue(child1.isChild(grandchild1_1.getState()));
        Assert.assertTrue(child1.isChild(grandchild1_2.getState()));
        Assert.assertTrue(child2.isChild(grandchild2_1.getState()));
        Assert.assertTrue(child4.isChild(grandchild4_1.getState()));
        Assert.assertTrue(parent.isChild(grandchild1_1.getState()));
        Assert.assertTrue(parent.isChild(grandchild1_2.getState()));
        Assert.assertTrue(parent.isChild(grandchild2_1.getState()));
        Assert.assertTrue(parent.isChild(grandchild4_1.getState()));
        Assert.assertTrue(grandparent.isChild(grandchild1_1.getState()));
        Assert.assertTrue(grandparent.isChild(grandchild1_2.getState()));
        Assert.assertTrue(grandparent.isChild(grandchild2_1.getState()));
        Assert.assertTrue(grandparent.isChild(grandchild4_1.getState()));

        Assert.assertEquals(child1.getChildrenStates(), grandchildren1);
        Assert.assertEquals(child2.getChildrenStates(), grandchildren2);
        Assert.assertEquals(child3.getChildrenStates(), grandchildren3);
        Assert.assertEquals(child4.getChildrenStates(), grandchildren4);

        // But we don't want any confused parents on who their children are...

        Assert.assertFalse(child1.isChild(grandchild2_1.getState()));
        Assert.assertFalse(child1.isChild(grandchild4_1.getState()));

        Assert.assertFalse(child2.isChild(grandchild1_1.getState()));
        Assert.assertFalse(child2.isChild(grandchild1_2.getState()));
        Assert.assertFalse(child2.isChild(grandchild4_1.getState()));

        Assert.assertFalse(child3.isChild(grandchild1_1.getState()));
        Assert.assertFalse(child3.isChild(grandchild1_2.getState()));
        Assert.assertFalse(child3.isChild(grandchild2_1.getState()));
        Assert.assertFalse(child3.isChild(grandchild4_1.getState()));

        Assert.assertFalse(child4.isChild(grandchild1_1.getState()));
        Assert.assertFalse(child4.isChild(grandchild1_2.getState()));
        Assert.assertFalse(child4.isChild(grandchild2_1.getState()));

        // Nor do we want any confused children on who their parents are...

        Assert.assertFalse(grandchild1_1.isParent(child2.getState()));
        Assert.assertFalse(grandchild1_1.isParent(child3.getState()));
        Assert.assertFalse(grandchild1_1.isParent(child4.getState()));

        Assert.assertFalse(grandchild1_2.isParent(child2.getState()));
        Assert.assertFalse(grandchild1_2.isParent(child3.getState()));
        Assert.assertFalse(grandchild1_2.isParent(child4.getState()));

        Assert.assertFalse(grandchild2_1.isParent(child1.getState()));
        Assert.assertFalse(grandchild2_1.isParent(child3.getState()));
        Assert.assertFalse(grandchild2_1.isParent(child4.getState()));

        Assert.assertFalse(grandchild4_1.isParent(child1.getState()));
        Assert.assertFalse(grandchild4_1.isParent(child2.getState()));
        Assert.assertFalse(grandchild4_1.isParent(child3.getState()));
    }

    /**
     * JUnit test for {@link InternalState#getEntranceActions()}, {@link InternalState#addEntranceAction(Action)},
     * {@link InternalState#getExitActions()}, and {@link InternalState#addExitAction(Action)}.
     *
     * Tests the action lists of an internal state.
     */
    @Test
    public void testActions() {
        InternalState<String, Void, List<String>> state = new InternalStateBase<>("State");

        Action<String, Void, List<String>> action1 = (s, e, t, c) -> {
        };
        Action<String, Void, List<String>> action2 = (s, e, t, c) -> {
        };
        Action<String, Void, List<String>> action3 = (s, e, t, c) -> {
        };

        Set<Action<? super String, ? super Void, ? super List<String>>> actions = new LinkedHashSet<>();
        actions.add(action1);
        actions.add(action2);
        actions.add(action3);

        // Entrance actions

        state.addEntranceAction(action1);
        state.addEntranceAction(action2);
        state.addEntranceAction(action3);

        Assert.assertEquals(actions, state.getEntranceActions());

        // Exit actions

        state.addExitAction(action1);
        state.addExitAction(action2);
        state.addExitAction(action3);

        Assert.assertEquals(actions, state.getEntranceActions());
    }

    /**
     * JUnit test for {@link InternalState#enter(Object, Transition, Object)} and {@link InternalState#exit(Object,
     * Transition, Object)}.
     *
     * Tests the transition behavior of an internal state.
     */
    @Test
    public void testTransitions() {
        InternalState<String, Void, List<String>> grandparent = new InternalStateBase<>("Grandparent");
        InternalState<String, Void, List<String>> parent = new InternalStateBase<>("Parent");
        InternalState<String, Void, List<String>> child1 = new InternalStateBase<>("Child1");
        InternalState<String, Void, List<String>> child2 = new InternalStateBase<>("Child2");
        InternalState<String, Void, List<String>> child3 = new InternalStateBase<>("Child3");
        InternalState<String, Void, List<String>> child4 = new InternalStateBase<>("Child4");
        InternalState<String, Void, List<String>> grandchild1_1 = new InternalStateBase<>("Grandchild1-1");
        InternalState<String, Void, List<String>> grandchild1_2 = new InternalStateBase<>("Grandchild1-2");
        InternalState<String, Void, List<String>> grandchild2_1 = new InternalStateBase<>("Grandchild2-1");
        InternalState<String, Void, List<String>> grandchild4_1 = new InternalStateBase<>("Grandchild4-1");

        parent.setParentState(grandparent);
        grandparent.addChild(parent);

        child1.setParentState(parent);
        child2.setParentState(parent);
        child3.setParentState(parent);
        child4.setParentState(parent);
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        parent.addChild(child4);

        grandchild1_1.setParentState(child1);
        grandchild1_2.setParentState(child1);
        grandchild2_1.setParentState(child2);
        grandchild4_1.setParentState(child4);
        child1.addChild(grandchild1_1);
        child1.addChild(grandchild1_2);
        child2.addChild(grandchild2_1);
        child4.addChild(grandchild4_1);

        Action<String, Void, List<String>> action = (s, e, t, c) -> c.add(s);
        List<String> actions = new ArrayList<>();
        Transition<String, List<String>> transition;

        grandparent.addEntranceAction(action);
        parent.addEntranceAction(action);
        child1.addEntranceAction(action);
        child2.addEntranceAction(action);
        child3.addEntranceAction(action);
        child4.addEntranceAction(action);
        grandchild1_1.addEntranceAction(action);
        grandchild1_2.addEntranceAction(action);
        grandchild2_1.addEntranceAction(action);
        grandchild4_1.addEntranceAction(action);

        grandparent.addExitAction(action);
        parent.addExitAction(action);
        child1.addExitAction(action);
        child2.addExitAction(action);
        child3.addExitAction(action);
        child4.addExitAction(action);
        grandchild1_1.addExitAction(action);
        grandchild1_2.addExitAction(action);
        grandchild2_1.addExitAction(action);
        grandchild4_1.addExitAction(action);

        // Reentrant transitions

        transition = new TransitionBase<>(parent.getState(), parent::getState, TransitionGuard.none());

        actions.clear();
        parent.exit(null, transition, actions);
        Assert.assertEquals(Arrays.asList(parent.getState()), actions);

        actions.clear();
        parent.enter(null, transition, actions);
        Assert.assertEquals(Arrays.asList(parent.getState()), actions);

        // Child to parent transitions

        transition = new TransitionBase<>(parent.getState(), grandparent::getState, TransitionGuard.none());

        actions.clear();
        parent.exit(null, transition, actions);
        Assert.assertEquals(Arrays.asList(parent.getState()), actions);

        actions.clear();
        grandparent.enter(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);

        transition = new TransitionBase<>(grandchild4_1.getState(), grandparent::getState, TransitionGuard.none());

        actions.clear();
        grandchild4_1.exit(null, transition, actions);
        Assert.assertEquals(Arrays.asList(grandchild4_1.getState(), child4.getState(), parent.getState()), actions);

        actions.clear();
        grandparent.enter(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);

        // Parent to child transitions

        transition = new TransitionBase<>(grandparent.getState(), parent::getState, TransitionGuard.none());

        actions.clear();
        grandparent.exit(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);

        actions.clear();
        parent.enter(null, transition, actions);
        Assert.assertEquals(Arrays.asList(parent.getState()), actions);

        transition = new TransitionBase<>(grandparent.getState(), grandchild2_1::getState, TransitionGuard.none());

        actions.clear();
        grandparent.exit(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);

        actions.clear();
        grandchild2_1.enter(null, transition, actions);
        Assert.assertEquals(Arrays.asList(parent.getState(), child2.getState(), grandchild2_1.getState()), actions);

        // Outside to child transitions

        transition = new TransitionBase<>("", grandchild1_1::getState, TransitionGuard.none());

        actions.clear();
        grandparent.exit(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);

        actions.clear();
        grandchild1_1.enter(null, transition, actions);
        Assert.assertEquals(
                Arrays.asList(grandparent.getState(), parent.getState(), child1.getState(), grandchild1_1.getState()),
                actions);

        // Child to outside transitions

        transition = new TransitionBase<>(grandchild1_1.getState(), () -> "", TransitionGuard.none());

        actions.clear();
        grandchild1_1.exit(null, transition, actions);
        Assert.assertEquals(
                Arrays.asList(grandchild1_1.getState(), child1.getState(), parent.getState(), grandparent.getState()),
                actions);

        actions.clear();
        grandchild1_1.enter(null, transition, actions);
        Assert.assertEquals(Collections.<String>emptyList(), actions);
    }
}
