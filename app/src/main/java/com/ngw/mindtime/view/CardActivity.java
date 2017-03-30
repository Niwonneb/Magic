package com.ngw.mindtime.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.ngw.mindtime.R;
import com.ngw.mindtime.database.SharedData;
import com.ngw.mindtime.model.Thought;
import com.ngw.mindtime.view.cards.Card;
import com.ngw.mindtime.view.cards.CreateThoughtCard;
import com.ngw.mindtime.view.cards.ThoughtCard;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;


public class CardActivity extends Activity {
    private SwipeDeckAdapter adapter;
    private SwipeDeck swipeDeck;
    private RelativeLayout layout;
    private SharedData sharedData;
    private Thought thoughtTree;
    private List<Thought> alreadyViewedThoughts = new LinkedList<>();
    private Thought previousThought;
    private FButton leftButton;
    private FButton middleButton;
    private FButton rightButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_silver));

        swipeDeck = (SwipeDeck) findViewById(R.id.swipe_deck);
        layout = (RelativeLayout) findViewById(R.id.swipeLayout);

        leftButton   = (FButton) findViewById(R.id.leftButton);
        middleButton = (FButton) findViewById(R.id.middleButton);
        rightButton  = (FButton) findViewById(R.id.rightButton);
        leftButton  .setOnTouchListener((v, event) -> handleOnTouch(v, event, leftButton));
        middleButton.setOnTouchListener((v, event) -> handleOnTouch(v, event, middleButton));
        rightButton .setOnTouchListener((v, event) -> handleOnTouch(v, event, rightButton));

        setSavedColors();

        swipeDeck.setLeftImage(R.id.left_image);
        swipeDeck.setRightImage(R.id.right_image);

        adapter = new SwipeDeckAdapter();


        swipeDeck.setAdapter(adapter);
        swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long stableId) {
                if (ignoreNextSwipe) {
                    ignoreNextSwipe = false;
                    return;
                }
                onSwipe((int) stableId, false);
            }

            @Override
            public void cardSwipedRight(long stableId) {
                onSwipe((int) stableId, true);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(this::addFirstCard, 1000);
    }

    private boolean handleOnTouch(View v, MotionEvent event, FButton fButton) {
            Vibrator vb = (Vibrator) CardActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            vb.vibrate(30);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            vb.vibrate(20);
        }
        fButton.onTouch(v, event);
        return false;
    }

    private void addFirstCard() {
        sharedData = new SharedData(this.getSharedPreferences("com.ngw.mindtime", Context.MODE_PRIVATE));
        thoughtTree = sharedData.getThoughtTree();
        if (thoughtTree == null) {
            thoughtTree = Thought.createInitialThoughtTree();
        }
        previousThought = thoughtTree;
        adapter.addCard(new ThoughtCard(thoughtTree.getFollowingThought()));
    }

    private void onSwipe(int cardId, boolean positive) {
        Card card = (Card) adapter.getItem((int) cardId);
        switch (card.getType()) {
            case THOUGHT:
                thoughtCardSwiped(card, positive);
                break;
            case CREATION:
                createThought((CreateThoughtCard) card, positive);
                break;
        }

    }

    private void thoughtCardSwiped(Card card, boolean positive) {
        Thought thought = ((ThoughtCard) card).getThought();
        if (positive) {
            alreadyViewedThoughts.clear();
            if (thought.hasFollowingThought()) {
                previousThought = thought;
                addThoughtCard(thought.getFollowingThought());
            } else {
                Thought.ThoughtType type = (thought.getType() == Thought.ThoughtType.Question ? Thought.ThoughtType.Answer : Thought.ThoughtType.Question);
                addCreationCard(type, thought);
            }
        } else {
            alreadyViewedThoughts.add(thought);
            if (previousThought.hasFollowingThought(alreadyViewedThoughts)) {
                addThoughtCard(previousThought.getFollowingThought(alreadyViewedThoughts));
            } else {
                alreadyViewedThoughts.clear();
                if (previousThought == thoughtTree) {
                    addThoughtCard(thoughtTree.getFollowingThought());
                } else {
                    addCreationCard(thought.getType(), previousThought);
                }
            }
        }
    }

    private void addCreationCard(Thought.ThoughtType type, Thought previousThought) {
        adapter.addCard(new CreateThoughtCard(type, previousThought));
    }

    private void addThoughtCard(Thought thought) {
        adapter.addCard(new ThoughtCard(thought));
    }

    private void createThought(CreateThoughtCard card, boolean positive) {
        String text = card.getText();
        if (!text.equals("") &&
            positive) {
            if (card.isQuestion() &&
                !text.endsWith("?")) {
                text = text + "?";
            } else if (!card.isQuestion() &&
                       text.endsWith("?") ) {
                text = text + "!";
            }
            Thought newThought = new Thought(text, card.getPreviousThought(), card.getCreatedThoughtType());
            card.getPreviousThought().addFollowingThought(newThought);

            sharedData.saveThoughtTree(thoughtTree);

            previousThought = card.getPreviousThought();
            adapter.addCard(new ThoughtCard(newThought));
        } else {
            previousThought = thoughtTree;
            adapter.addCard(new ThoughtCard(thoughtTree.getFollowingThought()));
        }
    }

    public void leftButtonClicked(View view) {
        Random rnd = new Random();

        int backgroundColor;
        int statusBarColor;
        int buttonColor;
        int shadowColor;

        if (rnd.nextInt(10) == 1) {
            statusBarColor = getResources().getColor(R.color.fbutton_color_silver);
            backgroundColor = getResources().getColor(R.color.fbutton_color_clouds);

            buttonColor = Color.parseColor("#007f7f");
            shadowColor = Color.parseColor("#006f6f");
        } else {
            int red   = rnd.nextInt(256);
            int green = rnd.nextInt(256);
            int blue  = rnd.nextInt(256);
            backgroundColor = Color.argb(255, red, green, blue);
            statusBarColor = Color.argb(255, addRGB(red, -10), addRGB(green, -10), addRGB(blue, -10));

            red   = rnd.nextInt(256);
            green = rnd.nextInt(256);
            blue  = rnd.nextInt(256);

            buttonColor = Color.argb(255, red, green, blue);
            shadowColor = Color.argb(255, addRGB(red, -20), addRGB(green, -20), addRGB(blue, -20));
        }

        setColors(backgroundColor, statusBarColor, buttonColor, shadowColor);
        saveColors(backgroundColor, statusBarColor, buttonColor, shadowColor);
    }

    private int addRGB(int i, int j) {
        int result = i + j;
        if (result > 255) {
            result = 255;
        } else if (result < 0) {
            result = 0;
        }
        return result;
    }

    private void saveColors(int backgroundColor, int statusBarColor, int buttonColor, int shadowColor) {
        SharedPreferences prefs = this.getSharedPreferences(
              "com.ngw.mindtime", Context.MODE_PRIVATE);

        prefs.edit().putInt("backgroundColor", backgroundColor).apply();
        prefs.edit().putInt("statusBarColor", statusBarColor).apply();
        prefs.edit().putInt("buttonColor", buttonColor).apply();
        prefs.edit().putInt("shadowColor", shadowColor).apply();
    }

    private void setSavedColors() {
        SharedPreferences prefs = this.getSharedPreferences(
              "com.ngw.mindtime", Context.MODE_PRIVATE);

        if (prefs.contains("backgroundColor") &&
            prefs.contains("statusBarColor")) {
            int backgroundColor = prefs.getInt("backgroundColor", 0);
            int statusBarColor = prefs.getInt("statusBarColor", 0);
            int buttonColor = prefs.getInt("buttonColor", 0);
            int shadowColor = prefs.getInt("shadowColor", 0);

            setColors(backgroundColor, statusBarColor, buttonColor, shadowColor);
        }
    }

    private void setColors(int backgroundColor, int statusBarColor, int buttonColor, int shadowColor) {
        getWindow().setStatusBarColor(statusBarColor);
        layout.setBackgroundColor(backgroundColor);

        setButtonColors(buttonColor, shadowColor);
    }

    private void setButtonColors(int buttonColor, int shadowColor) {
        leftButton  .setButtonColor(buttonColor);
        middleButton.setButtonColor(buttonColor);
        rightButton .setButtonColor(buttonColor);

        leftButton  .setShadowColor(shadowColor);
        middleButton.setShadowColor(shadowColor);
        rightButton .setShadowColor(shadowColor);

    }

    public void middleButtonClicked(View view) {
        if (previousThought != thoughtTree) {
            Card currentCard = (Card) adapter.getItem(adapter.getCount() - 1);
            if (currentCard instanceof ThoughtCard) {
                if (!((ThoughtCard) currentCard).getThought().hasFollowingThought()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogStyle));
                    builder.setMessage("Diese Karte löschen?")
                           .setPositiveButton("Ja", (dialog, which) -> {
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    previousThought.deleteFollowingThought(((ThoughtCard) currentCard).getThought());
                                    swipeDeck.swipeTopCardLeft(500);
                                }})
                           .setNegativeButton("Nein", null)
                           .show();
                }
            }
        }
    }

    private boolean ignoreNextSwipe = false;
    public void rightButtonClicked(View view) {
        if (previousThought != thoughtTree) {
            Card currentCard = (Card) adapter.getItem(adapter.getCount() - 1);
            if (currentCard instanceof ThoughtCard) {
                ThoughtCard card = (ThoughtCard) currentCard;
                Thought thought = card.getThought();
                ignoreNextSwipe = true;
                swipeDeck.swipeTopCardLeft(500);
                addCreationCard(thought.getType(), previousThought);
            }
        }
    }

    public class SwipeDeckAdapter extends BaseAdapter {

        private List<Card> cards;

        public SwipeDeckAdapter() {
            this.cards = new LinkedList<>();
        }

        public void addCard(final Card card) {
            this.cards.add(card);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public Object getItem(int position) {
            return cards.get(position);
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (position >= cards.size()) {
                throw new IndexOutOfBoundsException("position >= cards.size() in getNewInflatedView");
            }

            Card card = cards.get(position);

            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = card.getNewInflatedView(inflater, parent);
            }
            card.setUpView(view);

            return view;
        }

        @Override
        public int getViewTypeCount() {
            return Card.ViewType.values().length;
        }

        @Override
        public int getItemViewType(int position) {
            return cards.get(position).getViewTypeAsInt();
        }
    }
}
