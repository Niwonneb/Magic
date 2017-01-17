package com.ngw.toowyn.view.cardstack;

import com.ngw.toowyn.R;
import com.ngw.toowyn.controller.ThoughtController;
import com.ngw.toowyn.model.Thought;
import com.ngw.toowyn.view.swipedeck.SwipeDeck;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;


public class CardActivity extends Activity {
    private SwipeDeckAdapter adapter;

    static private LinkedList<Thought> firstThoughts = new LinkedList<>();
    static {
        firstThoughts.add(new Thought("Welcome back :)"));
        firstThoughts.add(new Thought("2"));
        firstThoughts.add(new Thought("3"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        SwipeDeck cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);

        adapter = new SwipeDeckAdapter(firstThoughts, this);
        if(cardStack != null){
            cardStack.setAdapter(adapter);
        }
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(int stableId) {
                loadNewImage();
            }

            @Override
            public void cardSwipedRight(int stableId) {
                loadNewImage();
            }
        });
    }

    public void loadNewImage() {
        ThoughtController.getInstance().getRandomThought()
            .subscribe(
                            (thought) -> {
                                adapter.addThought(thought);
                            },
                            (error) -> {
                                adapter.addThought(new Thought("Couldn't reach server :("));
                            }
                    );
    }

    public class SwipeDeckAdapter extends BaseAdapter {

        private List<Thought> thoughts;

        public SwipeDeckAdapter(final List<Thought> thoughts, Context context) {
            this.thoughts = thoughts;
        }

        public void addThought(final Thought thought) {
            this.thoughts.add(thought);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return thoughts.size();
        }

        @Override
        public Object getItem(int position) {
            return thoughts.get(position);
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
            if (!thoughts.isEmpty()) {
                thoughts.remove(0);
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                v = inflater.inflate(R.layout.card_view, parent, false);
            }

            if (position >= thoughts.size()) {
                return v;
            }

            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
            TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(thoughts.get(position).getText());

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                    Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
                    /*Intent i = new Intent(v.getContext(), BlankActivity.class);
                    v.getContext().startActivity(i);*/
                }
            });
            return v;
        }
    }
}
