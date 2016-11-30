package com.magic_app.magic.apiservice.images;

import android.graphics.Bitmap;

import java.io.File;
import java.util.List;
import java.util.Vector;

import com.magic_app.magic.apiservice.Api;
import com.magic_app.magic.apiservice.ServiceFactory;
import com.magic_app.magic.apiservice.errorhandling.GenericErrorHandler;
import com.magic_app.magic.model.Image;
import com.magic_app.magic.util.BitmapHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class ImagesApiService {

    public Observable<Image> getRandomImage() {
        return GenericErrorHandler.wrapSingle(service.getRandomImage());
    }

    public interface SearchApi {
        //@Path("description") String description,

        @Multipart
        @POST(Api.version + "/images/")
        Observable<Response<Object>> createImage(@Part("file\"; filename=image.png") RequestBody file);

        @GET(Api.version + "/images/random/")
        Observable<Response<Image>> getRandomImage();
    }

    private SearchApi service = ServiceFactory.createService(SearchApi.class);

    public Observable<Object> createImage(String description, Bitmap bitmap) {
        File pngFile = BitmapHelper.toPngFile(bitmap);
        if (pngFile == null) {
            return Observable.error(new Throwable("could not store image"));
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), pngFile);
        return GenericErrorHandler.wrapSingle(service.createImage(requestBody));
    }
}
