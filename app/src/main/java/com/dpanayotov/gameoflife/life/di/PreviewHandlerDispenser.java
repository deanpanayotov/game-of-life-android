package com.dpanayotov.gameoflife.life.di;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by Dean Panayotov on 11/5/2016
 */

public class PreviewHandlerDispenser implements HandlerDispenser {

    private HandlerThread ht;

    @Override
    public Handler start() {
        ht = new HandlerThread("");
        ht.start();
        return new Handler(ht.getLooper());
    }

    @Override
    public void stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ht.quitSafely();
        }else{
            ht.quit();
        }
    }
}
