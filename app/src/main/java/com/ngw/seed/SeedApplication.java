package com.ngw.seed;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

public class SeedApplication extends Application {
    public void onCreate() {
        super.onCreate();
    }

    public static String getID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
