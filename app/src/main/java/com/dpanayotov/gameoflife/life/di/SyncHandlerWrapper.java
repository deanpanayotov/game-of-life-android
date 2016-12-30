package com.dpanayotov.gameoflife.life.di;

import android.os.Handler;

/**
 * Created by Dean Panayotov on 11/5/2016
 */

public class SyncHandlerWrapper {

    private boolean destroyCalled = false;
    protected Handler handler;

    public void init() {
        handler = createHandler();
    }

    public void destroy() {
        if (destroyCalled)
            throw new IllegalStateException("Calling destroy() after destroy() has been already " +
                    "called.");
        destroyCalled = true;
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    public Handler get() {
        if (destroyCalled)
            throw new IllegalStateException("Calling get() on a destroyed SyncHandlerWrapper.");
        if (handler == null) {
            throw new IllegalStateException("Please call init() before calling get().");
        }
        return handler;
    }

    protected Handler createHandler() {
        return new Handler();
    }

}
