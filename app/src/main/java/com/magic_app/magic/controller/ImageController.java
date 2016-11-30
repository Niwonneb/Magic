package com.magic_app.magic.controller;

import android.graphics.Bitmap;

import com.magic_app.magic.apiservice.images.ImagesApiService;
import com.magic_app.magic.model.Image;
import com.magic_app.magic.util.AppTracker;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageController {

    ImagesApiService locationService;

    private static ImageController instance;
    public static ImageController getInstance() {
        if (instance == null) {
            instance = new ImageController();
        }
        return instance;
    }

    private ImageController() {
        locationService = new ImagesApiService();
    }

    public Observable<Object> createImage(String description, Bitmap image) {
        return locationService.createImage(description, image)
                .doOnNext((val) -> AppTracker.getInstance().track("Image Created"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Image> getRandomImage() {
        return locationService.getRandomImage()
                .doOnNext((val) -> AppTracker.getInstance().track("Random Image Request"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
