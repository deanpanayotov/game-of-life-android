package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferenceActivity extends Activity {

    @BindView(R.id.grid_width_height)
    ValueSetSeekBar<ScreenUtil.Resolution> gridWidthHeight;

    @BindView(R.id.cell_size)
    ValueSetSeekBar<Integer> cellSize;

    @BindView(R.id.color_picker_preference)
    ColorPickerPreference colorPickerPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences3);
        ButterKnife.bind(this);

        List<ScreenUtil.Resolution> resolutions = ScreenUtil.getAvailableResolutions(this);

        List<Integer> cellSizes = new ArrayList<>();

        for(ScreenUtil.Resolution resolution : resolutions){
            cellSizes.add(resolution.cellSize);
        }

        gridWidthHeight.setValues(resolutions);
        cellSize.setValues(cellSizes);

        cellSize.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                gridWidthHeight.setPosition(position);
            }
        });

        colorPickerPreference.setOnClickListener(new ColorPickerPreference.OnClickListener() {
            @Override
            public void onPrimaryColorClick() {
                Toast.makeText(PreferenceActivity.this, "primary", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackgroundColorClick() {
                Toast.makeText(PreferenceActivity.this, "background", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
