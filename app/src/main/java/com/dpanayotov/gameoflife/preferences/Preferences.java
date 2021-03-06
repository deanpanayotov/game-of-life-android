package com.dpanayotov.gameoflife.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dpanayotov.gameoflife.GameOfLifeApplication;
import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.util.Resolution;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dean Panayotov on 9/26/2016
 */

public class Preferences {

    private static SharedPreferences prefs;

    private static final List<Integer> tickRates;

    private static final List<Integer> minPopulationDensityOptions;
    private static final List<Integer> initialPopulationDensityOptions;


    static {
        Integer[] array = new Integer[]{16, 32, 100, 250, 500, 1000, 1500, 2000, 3000, 5000};
        tickRates = Arrays.asList(array);
        array = new Integer[]{4, 6, 8, 10, 12, 14, 16, 18, 20};
        minPopulationDensityOptions = Arrays.asList(array);
        array = new Integer[]{30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80};
        initialPopulationDensityOptions = Arrays.asList(array);
    }

    public static SharedPreferences getPrefs() {
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

    public static void setColor(Color color, int value) {
        getPrefs().edit().putInt(color.getKey(), value).commit();
    }

    public static int getColor(Color color) {
        return getPrefs().getInt(color.getKey(), color.getDefaultValue());
    }

    public static void swapColors(Color a, Color b) {
        int c = getColor(b);
        setColor(b, getColor(a));
        setColor(a, c);
    }

    public enum Color {
        PRIMARY("PRIMARY", R.string.color_primary, android.graphics.Color.parseColor("#0B083B")),
        SECONDARY("SECONDARY", R.string.color_secondary, android.graphics.Color.parseColor
                ("#FFD57C")),
        BACKGROUND("BACKGROUND", R.string.color_background, android.graphics.Color.parseColor
                ("#595594"));

        String key;
        int stringResId;
        int defaultValue;

        Color(String key, int stringResId, int defaultValue) {
            this.key = key;
            this.stringResId = stringResId;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public int getStringResId() {
            return stringResId;
        }

        public int getDefaultValue() {
            return defaultValue;
        }

        public static List<String> getKeys() {
            List<String> colors = new ArrayList<>();
            for (Color color : values()) {
                colors.add(color.getKey());
            }
            return colors;
        }

        public static List<Integer> getResIds() {
            List<Integer> colors = new ArrayList<>();
            for (Color color : values()) {
                colors.add(color.getStringResId());
            }
            return colors;
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

    public static void setIsometricProjection(boolean isometricProjection) {
        getPrefs().edit().putBoolean(Keys.ISOMETRIC_PROJECTION, isometricProjection).apply();
    }

    public static boolean getIsometricProjection() {
        return getPrefs().getBoolean(Keys.ISOMETRIC_PROJECTION, false);
    }

    public static void setHighlife(boolean highlife) {
        getPrefs().edit().putBoolean(Keys.HIGHLIFE, highlife).apply();
    }

    public static boolean getHighlife() {
        return getPrefs().getBoolean(Keys.HIGHLIFE, false);
    }

    public static List<Integer> getMinPopulationDensityOptions() {
        return minPopulationDensityOptions;
    }

    public static int getMinPopulationDensity() {
        return getPrefs().getInt(Keys.MIN_POPULATION_DENSITY, (minPopulationDensityOptions.size()
                - 1) /
                2);
    }

    public static void setMinPopulationDensity(int position) {
        getPrefs().edit().putInt(Keys.MIN_POPULATION_DENSITY, position).apply();
    }

    public static List<Integer> getInitialPopulationDensityOptions() {
        return initialPopulationDensityOptions;
    }

    public static int getInitialPopulationDensity() {
        return getPrefs().getInt(Keys.INITIAL_POPULATION_DENSITY,
                (initialPopulationDensityOptions.size() - 1) /
                        2);
    }

    public static void setInitialPopulationDensity(int position) {
        getPrefs().edit().putInt(Keys.INITIAL_POPULATION_DENSITY, position).apply();
    }

    public static boolean isInitialized() {
        return getPrefs().contains(Keys.RESOLUTION);
    }
}
