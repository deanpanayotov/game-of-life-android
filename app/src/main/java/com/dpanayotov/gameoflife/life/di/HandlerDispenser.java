package com.dpanayotov.gameoflife.life.di;

import android.os.Handler;

/**
 * Created by Dean Panayotov on 11/5/2016
 */

public interface HandlerDispenser {
    Handler start();
    void stop();
}
