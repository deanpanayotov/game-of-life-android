package com.dpanayotov.gameoflife.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.life.Constants;
import com.dpanayotov.gameoflife.life.Life;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends android.service.wallpaper.WallpaperService.Engine {

        private Life life = new Life();

        private static final int H = 240;
        private static final int S = 65;
        private static final int V = 15;
        private static final byte HUE_STEP = 5; //in degrees;
        private static final int VALUE_STEP = 10; //in percents;
        private static final int VALUE_LIMIT = 55; //in percents;

        private Paint paint = new Paint();

        //dimensions
        private int width;
        private int height;
        private int cellWidth;
        private int cellHeight;


        private ColorDispenser dispenser = new LinearColorDispenser(H, S, V, HUE_STEP,
                VALUE_STEP, VALUE_LIMIT);

        //lifecycle
        private boolean visible = true;
        private boolean restart;

        public WallpaperEngine() {
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
            //TODO example for preferences
//            maxNumber = Integer
//                    .valueOf(prefs.getString("numberOfCircles", "4"));
//            touchEnabled = prefs.getBoolean("touch", false);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
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
            this.width = width;
            this.height = height;
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
                handler.post(drawRunner);
            }
        }

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                step();
                handler.postDelayed(drawRunner, Constants.TICK_INTERVAL);
            }
        };

        private void step() {
            update();
            draw();
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
                        canvas.rotate(45f);
                        canvas.scale(1.7f, 1.7f);
                        canvas.translate(00,-700);
                        paint.setColor(Color.parseColor("#0B083B"));
                        canvas.drawRect(0, 0, width, height, paint);
                        paint.setColor(Color.parseColor("#595594"));
                        for (int i = 0; i < Constants.GRID_WIDTH; i++) {
                            for (int j = 0; j < Constants.GRID_HEIGHT; j++) {
                                if (life.summedGrid.cells[i][j] > 0) {
                                    int cellX = i * cellWidth + cellWidth / 2;
                                    int cellY = j * cellHeight + cellHeight / 2;
                                    paint.setAlpha((int) (255 * (1 / (float) life.summedGrid
                                            .cells[i][j])));
                                    int radius = (cellWidth / 2 - Constants.CELL_PADDING) -
                                            Constants.RADIUS_STEP * (life.summedGrid.cells[i][j]
                                                    - 1);
                                    canvas.drawCircle(cellX, cellY, radius, paint);
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
