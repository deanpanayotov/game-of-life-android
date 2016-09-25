package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dpanayotov.gameoflife.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferenceActivity extends Activity {

    @BindView(R.id.test)
    ValueSetSeekBar<Integer> seekBar;

    @BindView(R.id.test2)
    ValueSetSeekBar<Float> seekBar2;

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
        seekBar.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value) {
                Toast.makeText(PreferenceActivity.this, "" + value, Toast
                        .LENGTH_SHORT).show();
            }
        });

        final List<Float> vals2 = new ArrayList<>();
        vals2.add(1.5f);
        vals2.add(2.5f);
        vals2.add(3.5f);
        vals2.add(5.5f);
        vals2.add(8.5f);


        seekBar2.setValues(vals2);
        seekBar2.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Float>() {
            @Override
            public void onValueChange(Float value) {
                Toast.makeText(PreferenceActivity.this, "" + value, Toast
                        .LENGTH_SHORT).show();
            }
        });
    }
}
