package com.ngw.seed.server_connection;

import com.ngw.seed.model.Thought;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThoughtApiService {

    public interface SeedServerApi {
        @GET("/thoughts/getThoughtFromStart")
        Observable<Response<Thought>> getThoughtFromStart();

        @GET("/thoughts/getNextAfterLiked/{id}")
        Observable<Response<Thought>> getNextThoughtAfterLiked(@Path("id") String id);

        @GET("/thoughts/getNextAfterDisliked/{id}")
        Observable<Response<Thought>> getNextThoughtAfterDisliked(@Path("id") String id);

        @FormUrlEncoded
        @POST("/thoughts/rateThought")
        Observable<Response<Thought>> rateThought(@Field("idBefore") String title,
                                                  @Field("likedBefore") Boolean likedBefore,
                                                  @Field("idNow") String idNow,
                                                  @Field("likedNow") Boolean likedNow);

        @FormUrlEncoded
        @POST("/thoughts/createThought")
        Observable<Response<Thought>> createThought(@Field("text") String text,
                                                    @Field("idBefore") String idBefore,
                                                    @Field("likedBefore") Boolean likedBefore);
    }

    private SeedServerApi apiService = ServerServiceFactory.createService(SeedServerApi.class);

    public Observable<Thought> getThoughtFromStart() {
        return unwrapAndSubscribe(apiService.getThoughtFromStart());
    }

    public Observable<Thought> getNextThought(String thoughtId, boolean liked) {
        if (liked) {
            return unwrapAndSubscribe(apiService.getNextThoughtAfterLiked(thoughtId));
        } else {
            return unwrapAndSubscribe(apiService.getNextThoughtAfterDisliked(thoughtId));
        }
    }

    public void createThought(String text, String idBefore, boolean likedBefore) {
        unwrapAndSubscribe(apiService.createThought(text, idBefore, likedBefore))
            .subscribe(
                    (result)->{},
                    (error) -> {});
    }

    public void rateThought(String idBefore, boolean likedBefore, String idNow, boolean likedNow) {
        unwrapAndSubscribe(apiService.rateThought(idBefore, likedBefore, idNow, likedNow))
            .subscribe(
                    (result)->{},
                    (error) -> {});
    }

    private Observable<Thought> unwrapAndSubscribe(Observable<Response<Thought>> observable) {
        return observable
                .flatMap(response -> Observable.just(response.body()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
