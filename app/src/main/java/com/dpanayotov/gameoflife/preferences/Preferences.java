package com.dpanayotov.gameoflife.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dpanayotov.gameoflife.GameOfLifeApplication;
import com.dpanayotov.gameoflife.util.Resolution;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/26/2016
 */

public class Preferences {

    private static SharedPreferences prefs;

    private static SharedPreferences getPrefs() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(GameOfLifeApplication
                    .getInstance().getContext());
        }
        return prefs;
    }

    public static void setResolutions(List<Resolution> resolutions) {
        setJsonPrefrence(resolutions, Keys.RESOLUTIONS);
    }

    public static List<Resolution> getResolutions() {
        String json = getPrefs().getString(Keys.RESOLUTIONS, "");
        Resolution[] resolutions = new Gson().fromJson(json, Resolution[].class);
        if (resolutions != null) {
            return Arrays.asList(resolutions);
        }
        return null;
    }

    public static void setResolution(Resolution resolution) {
        setJsonPrefrence(resolution, Keys.RESOLUTION);
    }

    public static Resolution getResolution() {
        String json = getPrefs().getString(Keys.RESOLUTION, "");
        return new Gson().fromJson(json, Resolution.class);
    }

    private static void setJsonPrefrence(Object object, String key) {
        SharedPreferences.Editor prefsEditor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }
}
