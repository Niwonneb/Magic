package com.ngw.mindbase.database;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ngw.mindbase.model.Thought;

public class SavedData {
    private SharedPreferences prefs;

    public SavedData(SharedPreferences prefs ) {
        this.prefs = prefs;
    }

    public void saveThoughtTree(Thought thoughtTree) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(thoughtTree);
        prefsEditor.putString("thoughts", json);
        prefsEditor.apply();
    }

    public Thought getThoughtTree() {
        if (prefs.contains("thoughts")) {
            Gson gson = new Gson();
            String json = prefs.getString("thoughts", "");
            return gson.fromJson(json, Thought.class);
        } else {
            return null;
        }
    }
}
