package com.magic_app.magic.view.cycle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.magic_app.magic.util.AppTracker;
import com.magic_app.magic.util.Debounce;
import com.magic_app.magic.view.slider.Animations.DescriptionAnimation;
import com.magic_app.magic.view.slider.SliderLayout;
import com.magic_app.magic.R;
import com.magic_app.magic.controller.ImageController;
import com.magic_app.magic.view.HomeActivity;
import com.magic_app.magic.view.UiError;
import com.magic_app.magic.view.slider.SliderTypes.BaseSliderView;
import com.magic_app.magic.view.slider.SliderTypes.TextSliderView;
import com.magic_app.magic.view.slider.Tricks.ViewPagerEx;

import java.util.HashMap;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class CycleActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("0", R.drawable.sample_0);
        file_maps.put("1", R.drawable.sample_1);
        file_maps.put("2", R.drawable.sample_2);
        file_maps.put("3", R.drawable.sample_3);
        file_maps.put("4", R.drawable.sample_4);

        for(String name : url_maps.keySet()){
            addImage(url_maps.get(name), name);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();
        ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        mDemoSlider.setPresetTransformer("DepthPage");
    }

    private void addImage(String url, String description) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(description)
                    .image(url)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

           mDemoSlider.addSlider(textSliderView);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    private void addInterchangingImages(int counter) {
        if (counter % 2 == 0) {
            addImage("http://appleneu.com/wp-content/uploads/2016/06/eb777b09c9bbf5bd006d0b4d5b4402b4.jpg", "internet");
        } else {
            addImage("https://upload.wikimedia.org/wikipedia/en/thumb/8/8c/Prisma_(app)_image_of_a_cat.jpg/300px-Prisma_(app)_image_of_a_cat.jpg", "cat");
        }
    }

    private Debounce addImageDebounce = new Debounce(2000);
    @Override
    public void onPageSelected(int position) {
        Log.d("debugMagic", "page selected: " + String.valueOf(position));
        if (position == 1) {
            //mDemoSlider.removeSliderAt(0);
        }
        if (!addImageDebounce.calledRecently()) {
            addInterchangingImages(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
