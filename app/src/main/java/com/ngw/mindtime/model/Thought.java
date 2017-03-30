package com.ngw.mindtime.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Thought {
    private List<Thought> followingThoughts = new LinkedList<>();
    private String text;
    private ThoughtType type;

    public Thought(String text, Thought previousThought, ThoughtType type) {
        this.text = text;
        this.type = type;
    }

    public void addFollowingThought(Thought thought) {
        followingThoughts.add(thought);
    }

    public boolean hasFollowingThought() {
        return !followingThoughts.isEmpty();
    }

    public Thought getFollowingThought() {
        Random rand = new Random();
        int selectedIndex = rand.nextInt(followingThoughts.size());
        return followingThoughts.get(selectedIndex);
    }

    public boolean hasFollowingThought(List<Thought> viewedThoughts) {
        return !viewedThoughts.containsAll(followingThoughts);
    }

    public Thought getFollowingThought(List<Thought> viewedThoughts) {
        viewedThoughts.retainAll(followingThoughts);
        Random rand = new Random();
        int randVal = rand.nextInt(followingThoughts.size() - viewedThoughts.size());
        Thought similarThought = null;
        for (Thought thought : followingThoughts) {
            if (!viewedThoughts.contains(thought)) {
                if (randVal == 0) {
                    similarThought = thought;
                    break;
                }
                --randVal;
            }
        }
        return similarThought;
    }

    public void deleteFollowingThought(Thought thought) {
        followingThoughts.remove(thought);
    }

    public String getText() {
        return text;
    }

    public enum ThoughtType {
        Answer, Question
    }

    public ThoughtType getType() {
        return type;
    }

    public static Thought createInitialThoughtTree() {
        Thought thoughtTree = new Thought("Super error delux", null, ThoughtType.Question);
        thoughtTree.addFollowingThought(new Thought("Was macht mir Spaß?", thoughtTree, ThoughtType.Question));
        thoughtTree.addFollowingThought(new Thought("Was hab ich schon erlebt?", thoughtTree, ThoughtType.Question));
        thoughtTree.addFollowingThought(new Thought("Was will ich mal werden?", thoughtTree, ThoughtType.Question));
        return thoughtTree;
    }

}
