package com.ngw.toowyn;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // TODO: mixpanel App tracking
        //AppTracker.getInstance().track("App | become active");
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return context;
    }

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("com.locator_app.locator", Context.MODE_PRIVATE);
    }
}
