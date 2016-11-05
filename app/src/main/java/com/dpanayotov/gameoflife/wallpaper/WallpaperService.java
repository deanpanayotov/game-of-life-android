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
import com.dpanayotov.gameoflife.preferences.Preferences;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;

import static com.dpanayotov.gameoflife.R.xml.prefs;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends android.service.wallpaper.WallpaperService.Engine
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        private Life life;

        //dimensions
        private int screenWidth;
        private int screenHeight;

        //lifecycle
        private boolean restart;


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            init(true);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                life.start();
            } else {
                life.stop();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            Preferences.getPrefs().registerOnSharedPreferenceChangeListener(this);
            restart = true;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Preferences.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
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
                if (life != null) {
                    life.stop();
                }
                life = new Life(screenWidth, screenHeight, getSurfaceHolder());
                restart = false;
                life.start();
            }
        }
    }
}
