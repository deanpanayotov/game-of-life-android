package com.dpanayotov.gameoflife.preferences;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.dpanayotov.gameoflife.GameOfLifeApplication;
import com.dpanayotov.gameoflife.util.Resolution;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/26/2016
 */

public class Preferences {

    private static SharedPreferences prefs;

    private static final List<Integer> tickRates;

    static {
        Integer[] array = new Integer[]{16, 32, 100, 250, 500, 1000, 1500, 2000, 3000, 5000};
        tickRates = Arrays.asList(array);
    }

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

    public static void setResolution(int position) {
        getPrefs().edit().putInt(Keys.RESOLUTION, position).apply();
    }

    public static int getResolution() {
        return getPrefs().getInt(Keys.RESOLUTION, 0);
    }

    private static void setJsonPrefrence(Object object, String key) {
        SharedPreferences.Editor prefsEditor = getPrefs().edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    public static void setColor(Colors color, int value) {
        getPrefs().edit().putInt(color.getKey(), value).commit();
    }

    public static int getColor(Colors color) {
        return getPrefs().getInt(color.getKey(), color.getDefaultValue());
    }

    public enum Colors {
        PRIMARY("PRIMARY", Color.parseColor("#0B083B")),
        BACKGROUND("BACKGROUND", Color.parseColor("#595594"));

        String key;
        int defaultValue;

        Colors(String key, int defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public int getDefaultValue() {
            return defaultValue;
        }

    }

    public static List<Integer> getTickRates() {
        return tickRates;
    }

    public static void setTickRate(int poistion) {
        getPrefs().edit().putInt(Keys.TICK_RATE, poistion).apply();
    }

    public static int getTickRate() {
        return getPrefs().getInt(Keys.TICK_RATE, (tickRates.size() - 1) / 2);
    }

    public static boolean isInitialized() {
        return getPrefs().contains(Keys.RESOLUTION);
    }
}
