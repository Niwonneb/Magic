package com.ngw.toowyn;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

public class ToowynApplication extends Application {
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

    public static String getID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("com.magic_app.magic", Context.MODE_PRIVATE);
    }
}
