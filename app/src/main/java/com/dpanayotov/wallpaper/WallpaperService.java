package com.dpanayotov.wallpaper;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends android.service.wallpaper.WallpaperService.Engine {

        private static final byte FRAMES_PER_SECOND = 100;
        private static final byte FRAME = 1000 / FRAMES_PER_SECOND; //in milliseconds;

        private static final byte NUMBER_OF_BARS = 4; //in milliseconds;
        private static final int H = 240;
        private static final int S = 65;
        private static final int V = 15;
        private static final byte HUE_STEP = 5; //in degrees;
        private static final int VALUE_STEP = 10; //in percents;
        private static final int VALUE_LIMIT = 55; //in percents;
        private static final float SPEED = 0.05f; //in bars per second;

        private Paint paint = new Paint();

        //dimensions
        private int width;
        private int height;


        private ColorDispenser dispenser = new LinearColorDispenser(H, S, V, HUE_STEP,
                VALUE_STEP, VALUE_LIMIT);
        private List<Bar> bars = new ArrayList<>();
        private int step; //pixels;
        private int border; //pixels;
        private float actualSpeed; //per second;

        //lifecycle
        private boolean visible = true;
        private boolean restart;
        private float delta;
        private long then;
        private long now;

        public MyWallpaperEngine() {
            getPreferences();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }

        private SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(WallpaperService.this);
        private SharedPreferences.OnSharedPreferenceChangeListener prefsListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        getPreferences();
                        init(true);
                    }
                };

        private void getPreferences() {
//            maxNumber = Integer
//                    .valueOf(prefs.getString("numberOfCircles", "4"));
//            touchEnabled = prefs.getBoolean("touch", false);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                then = System.currentTimeMillis();
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            prefs.registerOnSharedPreferenceChangeListener(prefsListener);
            restart = true;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            prefs.unregisterOnSharedPreferenceChangeListener(prefsListener);
            onVisibilityChanged(false);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.width = (int) width;
            this.height = (int) height;
            init(false);
        }

        /**
         * Initialize the whole drawing process
         */
        private void init(boolean force) {
            if (force || restart) {
                restart = false;

                step = (int) (height / NUMBER_OF_BARS);
                for (int i = 0; i < NUMBER_OF_BARS + 1; i++) {
                    bars.add(new Bar((int) (step * i), dispenser.nextColor()));
                }
                actualSpeed = (int) (step * SPEED * -1);
                border = (int) (step * -1);

                then = System.currentTimeMillis();
                handler.post(drawRunner);
            }
        }

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                step();
            }
        };

        private void step() {
            now = System.currentTimeMillis();
            delta = (now - then) / 1000f;
            then = now;
            update();
            draw();
        }

        private void update() {
            float stepSpeed = actualSpeed * delta;
            Bar bar, lastBar;
            for (int i = 0; i < bars.size(); i++) {
                bar = bars.get(i);
                bar.y += stepSpeed;
                if (bar.y <= border) {
                    lastBar = bars.get(bars.size() - 1);
                    bars.remove(bar);
                    bar.y = (int) (lastBar.y + step);
                    bar.color = dispenser.nextColor();
                    bars.add(bar);
                }
            }
        }

        private void draw() {
            if (visible) {
                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        for (Bar bar : bars) {
                            paint.setColor(bar.color);
                            canvas.drawRect(0, bar.y, width, bar.y + step, paint);
                        }
                    }
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
                handler.postDelayed(drawRunner, FRAME);
            }
        }

    }
}
