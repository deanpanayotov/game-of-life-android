package com.dpanayotov.gameoflife.wallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.life.Constants;
import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.preferences.Preferences;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends android.service.wallpaper.WallpaperService.Engine {

        private Resolution resolution;
        private int halfCell;
        private int colorPrimary;
        private int colorBackground;
        private int tickRate;
        private boolean isometricProjection;
        private boolean highlife;

        private Life life;

        private static final int H = 240;
        private static final int S = 65;
        private static final int V = 15;
        private static final byte HUE_STEP = 5; //in degrees;
        private static final int VALUE_STEP = 10; //in percents;
        private static final int VALUE_LIMIT = 55; //in percents;

        private Paint paint = new Paint();

        //dimensions
        private int screenWidth;
        private int screenHeight;

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

        private SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
                (WallpaperService.this);
        private SharedPreferences.OnSharedPreferenceChangeListener prefsListener = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                getPreferences();
                init(true);
            }
        };

        private void getPreferences() {
            resolution = Preferences.getResolutions().get(Preferences.getResolution());
            halfCell = resolution.cellSize / 2;
            colorPrimary = Preferences.getColor(Preferences.Colors.PRIMARY);
            colorBackground = Preferences.getColor(Preferences.Colors.BACKGROUND);
            tickRate = Preferences.getTickRates().get(Preferences.getTickRate());
            isometricProjection = Preferences.getIsometricProjection();
            highlife = Preferences.getHighlife();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            handler.removeCallbacks(drawRunner);
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
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
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            this.screenWidth = width;
            this.screenHeight = height;

            init(false);
        }

        /**
         * Initialize the whole drawing process
         */
        private void init(boolean force) {
            if (force || restart) {
                handler.removeCallbacks(drawRunner);
                life = new Life(resolution.gridWidth, resolution.gridHeight, highlife);
                restart = false;
                handler.post(drawRunner);
            }
        }

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                step();
                handler.postDelayed(drawRunner, tickRate);
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
                        paint.setColor(colorBackground);
                        canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
                        paint.setColor(colorPrimary);
                        for (int i = 0; i < resolution.gridWidth; i++) {
                            for (int j = 0; j < resolution.gridHeight; j++) {
                                if (life.summedGrid.cells[i][j] > 0) {
                                    int cellX = i * resolution.cellSize + halfCell;
                                    int cellY = j * resolution.cellSize + halfCell;
                                    if (isometricProjection) {
                                        cellY = (cellY + i * halfCell) % 1920;
                                    }
                                    paint.setAlpha((int) (255 * (1 / (float) life.summedGrid
                                            .cells[i][j])));
                                    int radius = (halfCell - Constants.CELL_PADDING) - Constants
                                            .RADIUS_STEP * (life.summedGrid.cells[i][j] - 1);
                                    canvas.drawCircle(cellX, cellY, radius, paint);
                                }
                            }
                        }
                    }
                } finally {
                    if (canvas != null) holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
