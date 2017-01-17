package com.ngw.toowyn.controller;

import com.ngw.toowyn.apiservice.thoughts.ThoughtApiService;
import com.ngw.toowyn.model.Thought;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThoughtController {

    private ThoughtApiService thoughtService;

    private static ThoughtController instance;
    public static ThoughtController getInstance() {
        if (instance == null) {
            instance = new ThoughtController();
        }
        return instance;
    }

    private ThoughtController() {
        thoughtService = new ThoughtApiService();
    }

    public Observable<Object> createThought(String thought) {
        return thoughtService.createThought(thought)
//                .doOnNext((val) -> AppTracker.getInstance().track("Thought Created"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Thought> getRandomThought() {
        return thoughtService.getRandomThought()
//                .doOnNext((val) -> AppTracker.getInstance().track("Random Thought Request"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
