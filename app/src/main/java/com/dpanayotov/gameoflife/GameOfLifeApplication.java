package com.dpanayotov.gameoflife;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dean Panayotov on 9/26/2016
 */

public class GameOfLifeApplication extends Application {
    private static GameOfLifeApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }

    public static GameOfLifeApplication getInstance() {
        return instance;
    }
}
