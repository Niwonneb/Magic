package com.ngw.seed.server_connection;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ServerServiceFactory {


    static String serverUrl = "http://192.168.43.209:8080/";

    private static Retrofit retrofit;

    public static <T> T createService(final Class<T> className) {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient();
            client.setFollowRedirects(true);
            client.setFollowSslRedirects(true);
            client.setWriteTimeout(100, TimeUnit.SECONDS);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            client.interceptors().add(interceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(serverUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(className);
    }
}