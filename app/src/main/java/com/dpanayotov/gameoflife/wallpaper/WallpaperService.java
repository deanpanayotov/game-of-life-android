package com.dpanayotov.gameoflife.wallpaper;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.life.Constants;
import com.dpanayotov.gameoflife.life.Life;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends android.service.wallpaper.WallpaperService.Engine {

        private Life life = new Life();

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
        private int cellWidth;
        private int cellHeight;


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
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                          String key) {
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
            this.cellWidth = width / Constants.GRID_WIDTH;
            this.cellHeight = height / Constants.GRID_HEIGHT;
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
            update();
            draw();
            handler.postDelayed(drawRunner, Constants.TICK_INTERVAL);
        }

        private void update() {
            life.update();
        }

        private void draw() {
            if (visible) {
                SurfaceHolder holder = getSurfaceHolder();
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(0, 0, width, height, paint);
                        paint.setColor(Color.WHITE);
                        for (int i = 0; i < Constants.GRID_WIDTH; i++) {
                            for (int j = 0; j < Constants.GRID_HEIGHT; j++) {
                                if (life.grid.cells[i][j] == 1) {
                                    int cellX = i * cellWidth;
                                    int cellY = j * cellHeight;
                                    canvas.drawRect(cellX, cellY, cellX + cellWidth, cellY +
                                            cellHeight, paint);
                                }
                            }
                        }
                    }
                } finally {
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
            }
        }

    }
}
