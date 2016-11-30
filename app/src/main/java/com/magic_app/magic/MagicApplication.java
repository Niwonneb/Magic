package com.magic_app.magic;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MagicApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context appContext() {
        return context;
    }

    public static final int BLUR_BORDER = 10;

    public static int imageSize() {
        return (int) context.getResources().getDimension(R.dimen.image_size) + BLUR_BORDER * 2;
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("com.magic_app.magic", Context.MODE_PRIVATE);
    }
}
