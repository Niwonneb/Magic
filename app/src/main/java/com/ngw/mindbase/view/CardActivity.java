package com.ngw.mindbase.view;

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
import com.ngw.mindbase.R;
import com.ngw.mindbase.database.SavedData;
import com.ngw.mindbase.model.Thought;
import com.ngw.mindbase.view.cards.Card;
import com.ngw.mindbase.view.cards.CreateThoughtCard;
import com.ngw.mindbase.view.cards.ThoughtCard;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import info.hoang8f.widget.FButton;


public class CardActivity extends Activity {
    private SwipeDeckAdapter adapter;
    private SwipeDeck swipeDeck;
    private RelativeLayout layout;
    private SavedData savedData;
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
        savedData = new SavedData(this.getSharedPreferences("ngw.mindtime", Context.MODE_PRIVATE));
        thoughtTree = savedData.getThoughtTree();
        if (thoughtTree == null) {
            thoughtTree = Thought.createInitialThoughtTree(getResources());
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
                addCreationCard(thought);
            }
        } else {
            alreadyViewedThoughts.add(thought);
            if (previousThought.hasFollowingThought(alreadyViewedThoughts)) {
                addThoughtCard(previousThought.getFollowingThought(alreadyViewedThoughts));
            } else {
                if (previousThought == thoughtTree) {
                    alreadyViewedThoughts.clear();
                    addThoughtCard(thoughtTree.getFollowingThought());
                } else {
                    addCreationCard(previousThought);
                }
            }
        }
    }

    private void addCreationCard(Thought previousThought) {
        adapter.addCard(new CreateThoughtCard(previousThought, middleButton.getShadowColor()));
    }

    private void addThoughtCard(Thought thought) {
        adapter.addCard(new ThoughtCard(thought));
    }

    private void createThought(CreateThoughtCard card, boolean positive) {
        String text = card.getText();
        if (!text.equals("") &&
            positive) {
            Thought newThought = new Thought(text);
            card.getPreviousThought().addFollowingThought(newThought);

            savedData.saveThoughtTree(thoughtTree);

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
              "com.ngw.mindbase", Context.MODE_PRIVATE);

        prefs.edit().putInt("backgroundColor", backgroundColor).apply();
        prefs.edit().putInt("statusBarColor", statusBarColor).apply();
        prefs.edit().putInt("buttonColor", buttonColor).apply();
        prefs.edit().putInt("shadowColor", shadowColor).apply();
    }

    private void setSavedColors() {
        SharedPreferences prefs = this.getSharedPreferences(
              "com.ngw.mindbase", Context.MODE_PRIVATE);

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
        if (adapter != null) {
            Card currentCard = (Card) adapter.getItem(adapter.getCount() - 1);
            if (currentCard instanceof CreateThoughtCard) {
                CreateThoughtCard creationCard = (CreateThoughtCard) currentCard;
                creationCard.setColor(shadowColor);
            }
        }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogStyle));
                builder.setMessage(getResources().getString(R.string.delete))
                        .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                previousThought.deleteFollowingThought(((ThoughtCard) currentCard).getThought());
                                swipeDeck.swipeTopCardLeft(500);
                                savedData.saveThoughtTree(thoughtTree);
                            }})
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .show();
            }
        }
    }

    private boolean ignoreNextSwipe = false;
    public void rightButtonClicked(View view) {
        if (previousThought != thoughtTree) {
            Card currentCard = (Card) adapter.getItem(adapter.getCount() - 1);
            if (currentCard instanceof ThoughtCard) {
                ignoreNextSwipe = true;
                swipeDeck.swipeTopCardLeft(500);
                addCreationCard(previousThought);
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
