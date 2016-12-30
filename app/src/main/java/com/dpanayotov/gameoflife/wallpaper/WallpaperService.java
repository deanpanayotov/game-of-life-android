package com.dpanayotov.gameoflife.wallpaper;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.preferences.Preferences;

/**
 * Created by Dean Panayotov Local on 2.9.2015
 */
public class WallpaperService extends android.service.wallpaper.WallpaperService {

    @Override
    public android.service.wallpaper.WallpaperService.Engine onCreateEngine() {
        Log.d("zxcv","WallpaperService.onCreateEngine() "+hashCode());
        return new WallpaperEngine();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("zxcv","WallpaperService.onCreate() "+hashCode());
    }

    @Override
    public void onDestroy() {
        Log.d("zxcv","WallpaperService.onDestroy() "+hashCode());
        super.onDestroy();
    }

    private class WallpaperEngine extends android.service.wallpaper.WallpaperService.Engine
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        private Life life;

        //dimensions
        private int screenWidth;
        private int screenHeight;

        //lifecycle
        private boolean restart;
        private boolean visible;


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Log.d("zxcv","WallpaperService.onSharedPreferenceChanged() "+hashCode());
            init(true);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                Log.d("zxcv","WallpaperService.onVisibilityChanged() visible "+hashCode());
                life.start();
            } else {
                Log.d("zxcv","WallpaperService.onVisibilityChanged() invisible "+hashCode());
                life.stop();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.d("zxcv","WallpaperService.onSurfaceCreated() "+hashCode());
            super.onSurfaceCreated(holder);
            Preferences.getPrefs().registerOnSharedPreferenceChangeListener(this);
            restart = true;
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d("zxcv","WallpaperService.onSurfaceDestroyed() "+hashCode());
            super.onSurfaceDestroyed(holder);
            Preferences.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
            onVisibilityChanged(false);
            Log.d("zxc", "onSurfaceDestroyed");
            life.destroy();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d("zxcv","WallpaperService.onSurfaceChanged() "+hashCode());
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
                    life.destroy();
                }
                life = new Life(screenWidth, screenHeight, getSurfaceHolder(), false);
                restart = false;
                if (visible) {
                    life.start();
                }
            }
        }
    }
}
