package com.ngw.toowyn.apiservice.thoughts;

import com.ngw.toowyn.ToowynApplication;
import com.ngw.toowyn.apiservice.Api;
import com.ngw.toowyn.apiservice.ServiceFactory;
import com.ngw.toowyn.apiservice.errorhandling.GenericErrorHandler;
import com.ngw.toowyn.model.Thought;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import retrofit.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public class ThoughtApiService {

    public Observable<Thought> getRandomThought() {
        return service.getRandomThought()
                .flatMap(response -> Observable.just(response.body()));
    }

    public interface SearchApi {
        @POST(Api.version + "/thoughts/{androidId}")
        Observable<Response<Object>> createThought(@Path("androidId") String androidId,
                                                   @Body String thought);

        @GET(Api.version + "/thoughts/random/")
        Observable<Response<Thought>> getRandomThought();
    }

    private SearchApi service = ServiceFactory.createService(SearchApi.class);

    public Observable<Object> createThought(String thought) {
        return service.createThought(ToowynApplication.getID(), thought)
                .flatMap(response -> Observable.just(response.body()));
    }
}
