package com.magic_app.magic.view.cardstack;

import com.magic_app.magic.R;
import com.magic_app.magic.model.Image;
import com.magic_app.magic.view.camera.CameraActivity;
import com.magic_app.magic.view.swipedeck.SwipeDeck;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CardActivity extends Activity {

    private static final String TAG = "CardActivity";
    private ReloadingSwipeDeck cardStack;
    private Context context = this;
    private SwipeDeckAdapter adapter;
    private ArrayList<String> testData;
    private CheckBox dragCheckbox;
    static boolean tookImageToday = false;

    static private LinkedList<Image> firstImages = new LinkedList<>();
    static {
        firstImages.add(new Image("first", "https://i.kinja-img.com/gawker-media/image/upload/s--IXBOglB4--/c_scale,fl_progressive,q_80,w_800/17jvp3a19o6gpjpg.jpg"));
        firstImages.add(new Image("second", "http://i1.sndcdn.com/artworks-000079112755-4m6zsc-original.jpg?30a2558"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if (!tookImageToday) {
            tookImageToday = true;
            Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
            startActivity(startCustomCameraIntent);
        }

        cardStack = (ReloadingSwipeDeck) findViewById(R.id.swipe_deck);
        dragCheckbox = (CheckBox) findViewById(R.id.checkbox_drag);

        adapter = new SwipeDeckAdapter(firstImages, this);
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

        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        Button btn = (Button) findViewById(R.id.button_left);
        btn.setOnClickListener(v -> {
            cardStack.swipeTopCardLeft(500);
        });

        Button btn2 = (Button) findViewById(R.id.button_right);
        btn2.setOnClickListener(v -> {
            cardStack.swipeTopCardRight(180);
        });

        Button btn3 = (Button) findViewById(R.id.button_center);
        btn3.setOnClickListener(v -> {
//                testData.add("a sample string.");
//                adapter.notifyDataSetChanged();
            Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
            startActivity(startCustomCameraIntent);
        });

    }

    private int counter = 0;
    public void loadNewImage() {
        Image image;

        ++counter;
        if (counter % 2 == 0) {
            image = new Image("right", "https://trekking.org/wp-content/uploads/2013/09/3111d631bce1cffd11dc9e00d5e7ddb5.jpg");
        } else {
            image = new Image("left", "https://img.posterlounge.de/images/wbig/poster-schoenes-ruhiges-plaetzchen-in-der-natur-228366.jpg");
        }

        adapter.addImage(image);
    }

    public class SwipeDeckAdapter extends BaseAdapter {

        private Context context;
        private List<Image> images;

        public SwipeDeckAdapter(final List<Image> images, Context context) {
            this.images = images;
            this.context = context;
        }

        public void addImage(final Image image) {
            this.images.add(image);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
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
            if (!images.isEmpty()) {
                images.remove(0);
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

            if (position >= images.size()) {
                return v;
            }

            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
            Picasso.with(context)
                    .load(images.get(position).getBig())
                    .fit().centerCrop()
                    .into(imageView);
            TextView textView = (TextView) v.findViewById(R.id.sample_text);
            textView.setText(images.get(position).getDescription());

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
