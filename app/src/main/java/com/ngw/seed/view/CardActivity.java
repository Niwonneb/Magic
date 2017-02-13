package com.ngw.seed.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.ngw.seed.R;
import com.ngw.seed.server_connection.ThoughtApiService;
import com.ngw.seed.model.Thought;
import com.ngw.seed.view.cards.Card;
import com.ngw.seed.view.cards.CreateThoughtCard;
import com.ngw.seed.view.cards.InfoCard;
import com.ngw.seed.view.cards.ThoughtCard;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class CardActivity extends Activity {
    private SwipeDeckAdapter adapter;
    private ThoughtApiService thoughtApiService;

    private Thought previousThought;
    private Thought currentThought;
    private boolean previousLiked;
    private boolean currentLiked;

    private RelativeLayout layout;

    static private LinkedList<Card> firstCards = new LinkedList<>();
    static {
        firstCards.add(new InfoCard("Welcome back :)"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.fbutton_color_silver));

        previousThought = new Thought("start", "");
        currentThought = new Thought("start", "");
        previousLiked = true;
        currentLiked = true;

        SwipeDeck swipeDeck = (SwipeDeck) findViewById(R.id.swipe_deck);
        layout = (RelativeLayout) findViewById(R.id.swipeLayout);

        swipeDeck.setLeftImage(R.id.left_image);
        swipeDeck.setRightImage(R.id.right_image);

        thoughtApiService = new ThoughtApiService();

        adapter = new SwipeDeckAdapter();
        adapter.addCard(new InfoCard("Welcome back :)"));
        newThoughtFromStart();

        swipeDeck.setAdapter(adapter);
        swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long stableId) {
                onSwipe((int) stableId, false);
            }

            @Override
            public void cardSwipedRight(long stableId) {
                onSwipe((int) stableId, true);
            }
        });
    }

    private void onSwipe(int cardId, boolean swipedRight) {
        Card card = (Card) adapter.getItem((int) cardId);
        switch (card.getType()) {
            case THOUGHT:
                rateThought((ThoughtCard) card, swipedRight);
                break;
            case CREATION:
                createThought((CreateThoughtCard) card);
                break;
        }

    }

    private void createThought(CreateThoughtCard card) {
        if (!card.getText().equals("")) {
            thoughtApiService.createThought(card.getText(), previousThought.getId(), previousLiked);
        }
    }

    private void rateThought(ThoughtCard card, boolean liked) {
        currentLiked = liked;
        thoughtApiService.rateThought(previousThought.getId(), previousLiked, card.getThoughtId(), liked);
        if (card.getThoughtId().equals(currentThought.getId())) {
            thoughtApiService.getNextThought(currentThought.getId(), liked)
                    .subscribe(
                            this::addThoughtCard,
                        (error) -> {
                            adapter.addCard(new InfoCard("Couldn't reach server :("));
                        }
                    );
        }
    }

    private void addThoughtCard(Thought thought) {
        if (thought != null) {
            adapter.addCard(new ThoughtCard(thought));
            previousThought = currentThought;
            previousLiked = currentLiked;
            currentThought = thought;
        } else {
            adapter.addCard(new InfoCard("Server returned something dumb :("));
        }
    }

    public void addCreateThoughtCard() {
        adapter.addCard(new CreateThoughtCard());
    }


    private void newThoughtFromStart() {
        thoughtApiService.getThoughtFromStart()
            .subscribe(
                    this::addThoughtCard,
                (error) -> {
                    adapter.addCard(new InfoCard("Couldn't reach server :("));
                }
            );
    }

    public void redButtonClicked(View view) {
        Random rnd = new Random();

        if (rnd.nextInt(10) == 1) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.fbutton_color_silver));
            layout.setBackgroundColor(getResources().getColor(R.color.fbutton_color_clouds));
        } else {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            layout.setBackgroundColor(color);
            getWindow().setStatusBarColor(color);
        }
    }

    public void blueButtonClicked(View view) {
        newThoughtFromStart();
    }

    public void yellowButtonClicked(View view) {
        addCreateThoughtCard();
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

        public void removeFirst() {
            if (!cards.isEmpty()) {

                cards.remove(0);
                notifyDataSetChanged();
            }
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
