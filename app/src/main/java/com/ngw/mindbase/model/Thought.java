package com.ngw.mindbase.model;

import android.content.res.Resources;

import com.ngw.mindbase.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Thought {
    private List<Thought> followingThoughts = new LinkedList<>();
    private String text;
    private boolean isInitialQuestion;

    public Thought(String text) {
        this(text, false);
    }

    public Thought(String text, boolean isInitialQuestion) {
        this.text = text;
        this.isInitialQuestion = isInitialQuestion;
    }

    public boolean isInitialQuestion() {
        return isInitialQuestion;
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

    public static Thought createInitialThoughtTree(Resources res) {
        Thought initialPastQuestion    = new Thought(res.getString(R.string.pastQuestion), true);
        Thought initialPresentQuestion = new Thought(res.getString(R.string.presentQuestion), true);
        Thought initialFutureQuestion  = new Thought(res.getString(R.string.futureQuestion), true);

        initialPastQuestion.addFollowingThought(new Thought(res.getString(R.string.born)));

        initialPresentQuestion.addFollowingThought(new Thought(res.getString(R.string.eat)));
        initialPresentQuestion.addFollowingThought(new Thought(res.getString(R.string.sleep)));

        initialFutureQuestion.addFollowingThought(new Thought(res.getString(R.string.die)));

        Thought thoughtTree = new Thought("Super Error Delux");
        thoughtTree.addFollowingThought(initialPastQuestion);
        thoughtTree.addFollowingThought(initialPresentQuestion);
        thoughtTree.addFollowingThought(initialFutureQuestion);
        return thoughtTree;
    }

}
