package com.dpanayotov.gameoflife.wallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.preferences.Preferences;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;

import java.util.List;

/**
 * Created by Dean Panayotov on 9/25/2016
 */

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                if (!Preferences.isInitialized()) {
                    List<Resolution> resolutions = ScreenUtil.getAvailableResolutions();
                    if (resolutions != null && resolutions.size() > 0) {
                        Preferences.setResolution((resolutions.size() - 1) / 2);
                        return true;
                    }
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean initializationSuccessful) {
                if (initializationSuccessful) {
                    Intent intent = new Intent();
                    intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new
                            ComponentName(SplashScreenActivity.this, WallpaperService.class));
                    startActivity(intent);
                    SplashScreenActivity.this.finish();
                } else {
                    new AlertDialog.Builder(SplashScreenActivity.this).setTitle(R.string.error)
                            .setMessage(R.string.incompatible_message).setNeutralButton(R.string
                            .close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SplashScreenActivity.this.finish();
                        }
                    }).show();
                }
            }
        }.execute();
    }
}
