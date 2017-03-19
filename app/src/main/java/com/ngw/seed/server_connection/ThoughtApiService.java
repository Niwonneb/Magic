package com.ngw.seed.server_connection;

import com.ngw.seed.model.Thought;

import java.util.List;

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

        @FormUrlEncoded
        @POST("/thoughts/getNext")
        Observable<Response<Thought>> getNextThought(@Field("id") String id,
                                                     @Field("exceptionIds") List<String> exceptionIds);

        @FormUrlEncoded
        @POST("/thoughts/rateThought")
        Observable<Response<Thought>> rateThought(@Field("idBefore") String title,
                                                  @Field("idNow") String idNow,
                                                  @Field("likedNow") Boolean likedNow);

        @FormUrlEncoded
        @POST("/thoughts/createQuestion")
        Observable<Response<Thought>> createQuestion(@Field("text") String text,
                                                     @Field("idBefore") String idBefore);

        @FormUrlEncoded
        @POST("/thoughts/createAnswer")
        Observable<Response<Thought>> createAnswer(@Field("text") String text,
                                                   @Field("idBefore") String idBefore);
    }

    private SeedServerApi apiService = ServerServiceFactory.createService(SeedServerApi.class);

    public Observable<Thought> getThoughtFromStart() {
        return unwrapAndSubscribe(apiService.getThoughtFromStart());
    }

    public Observable<Thought> getNextThought(String thoughtId, List<String> exceptionIds) {
        return unwrapAndSubscribe(apiService.getNextThought(thoughtId, exceptionIds));
    }

    public void createQuestion(String text, String idBefore) {
        unwrapAndSubscribe(apiService.createQuestion(text, idBefore))
            .subscribe(
                    (result)->{},
                    (error) -> {});
    }

    public void createAnswer(String text, String idBefore) {
        unwrapAndSubscribe(apiService.createAnswer(text, idBefore))
            .subscribe(
                    (result)->{},
                    (error) -> {});
    }

    public void rateThought(String idBefore, String idNow, boolean likedNow) {
        unwrapAndSubscribe(apiService.rateThought(idBefore, idNow, likedNow))
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
