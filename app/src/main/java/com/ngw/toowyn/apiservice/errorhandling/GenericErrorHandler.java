package com.ngw.toowyn.apiservice.errorhandling;


import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.ngw.toowyn.apiservice.errorhandling.RequestError.RequestErrorType;

import java.net.UnknownHostException;
import java.util.List;

import retrofit.Response;
import rx.Observable;

public class GenericErrorHandler {

    public static <T> Observable<T> wrapSingle(Observable<Response<T>> observable) {
        return observable
                .doOnError(GenericErrorHandler::logErrorMessage)
                .onErrorResumeNext(throwable -> {
                    return Observable.error(getRequestErrorFromThrowable(throwable));
                })
                .flatMap(response -> {
                    if (response.isSuccess()) {
                        return Observable.just(response.body());
                    }
                    return Observable.error(getHttpErrorFromResponse(response));
                });
    }

    private static void logErrorMessage(Throwable throwable) {
        if (throwable != null) {
            Log.d("Exception occurred: ", throwable.getMessage());
        } else {
            Log.d("Exception occurred: ", "[throwable is null]");
        }
    }

    public static <T> Observable<T> wrapList(Observable<Response<List<T>>> observable) {
        return wrapSingle(observable)
                .flatMapIterable(x -> x);
    }

    private static HttpError getHttpErrorFromResponse(Response response) {
        return new HttpError(response.code());
    }

    private static RequestError getRequestErrorFromThrowable(Throwable throwable) {
        RequestErrorType errorType = RequestErrorType.Other;
        if (throwable instanceof UnknownHostException) {
            errorType = RequestErrorType.ServerUnreachable;
        } else if (throwable instanceof JsonSyntaxException) {
            errorType = RequestErrorType.InvalidJson;
        }
        return new RequestError(errorType);
    }
}
