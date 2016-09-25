package com.dpanayotov.gameoflife.wallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dpanayotov.gameoflife.R;

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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean initializationSuccessful) {
                if (initializationSuccessful) {
                    Intent intent = new Intent(SplashScreenActivity.this, WallpaperActivity.class);
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
