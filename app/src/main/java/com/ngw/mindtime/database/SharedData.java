package com.ngw.mindtime.database;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ngw.mindtime.model.Thought;

public class SharedData {
    private SharedPreferences prefs;

    public SharedData(SharedPreferences prefs ) {
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

//        public void saveThoughtTree(Thought thoughtTree) {
//        Gson gson = new Gson();
//        String json = gson.toJson(thoughtTree);
//        FileOutputStream file;
//        try {
//            file = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
//            file.write(json.getBytes());
//            file.close();
//        } catch (IOException e) {
//            return;
//        }
//    }
//
//    public Thought getThoughtTree() {
//        Gson gson = new Gson();
//        FileInputStream file;
//        String json;
//        try {
//            file = context.openFileInput(FILENAME);
//            json = file.toString();
//            file.close();
//        } catch (IOException e) {
//            return null;
//        }
//        return gson.fromJson(json, Thought.class);
//    }
}
