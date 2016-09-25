package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dpanayotov.gameoflife.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferenceActivity extends Activity {

    @BindView(R.id.test)
    ValueSetSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences2);
        ButterKnife.bind(this);

        final List<Integer> vals = new ArrayList<>();
        vals.add(1);
        vals.add(2);
        vals.add(3);
        vals.add(5);
        vals.add(8);
        vals.add(13);
        vals.add(21);

        seekBar.setValues(vals);
        seekBar.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                Toast.makeText(PreferenceActivity.this, "" + value, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
