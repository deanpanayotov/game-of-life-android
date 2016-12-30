package com.dpanayotov.gameoflife.life.di;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by Dean Panayotov on 11/5/2016
 */

public class AsyncHandlerWrapper extends SyncHandlerWrapper {

    private HandlerThread handlerThread;

    @Override
    public void destroy() {
        super.destroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
    }

    @Override
    protected Handler createHandler() {
        handlerThread = new HandlerThread("");
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }
}
