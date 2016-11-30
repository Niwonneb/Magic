package com.magic_app.magic.util;

import com.magic_app.magic.App;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

public class AppTracker {

    private MixpanelAPI mixpanel;

    private AppTracker() {
        mixpanel = MixpanelAPI.getInstance(App.getAppContext(), "39259b14d0685a26d043d2f394718d4b");
    }

    public void track(String event) {
        mixpanel.track(event);
        mixpanel.flush();
    }

    public void setIdentity(String id, String name, String mail) {
        mixpanel.identify(id);
        mixpanel.getPeople().set("email", mail);
        mixpanel.getPeople().set("name", name);
    }

    private static AppTracker instance;
    public static AppTracker getInstance() {
        if (instance == null) {
            instance = new AppTracker();
        }
        return instance;
    }
}