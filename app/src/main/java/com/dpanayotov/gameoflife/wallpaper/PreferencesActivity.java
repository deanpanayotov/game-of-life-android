package com.dpanayotov.gameoflife.wallpaper;

import android.app.Activity;
import android.os.Bundle;

import com.dpanayotov.gameoflife.R;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class PreferencesActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
    }
}