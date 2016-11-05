package com.dpanayotov.gameoflife.life.di;

import android.os.Handler;

/**
 * Created by Dean Panayotov on 11/5/2016
 */

public class LiveHandlerDispenser implements HandlerDispenser {

    @Override
    public Handler start() {
        return new Handler();
    }

    @Override
    public void stop() {
    }
}
