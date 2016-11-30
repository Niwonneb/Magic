package com.magic_app.magic.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.magic_app.magic.R;
import com.magic_app.magic.controller.ImageController;


public class ImageActivity extends Activity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = (ImageView) findViewById(R.id.imageView);

        ImageController.getInstance().getRandomImage()
                .subscribe(
                        (result) -> {
                            Glide.with(getApplicationContext()).load(result.getBig()).asBitmap().dontAnimate()
                                               .centerCrop().into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    imageView.setImageBitmap(resource);
                                    imageView.setVisibility(View.VISIBLE);
                                }
                            });
                        },
                        (err) -> {
                            UiError.showError(getApplicationContext(), err);
                        }
                );
    }
}
