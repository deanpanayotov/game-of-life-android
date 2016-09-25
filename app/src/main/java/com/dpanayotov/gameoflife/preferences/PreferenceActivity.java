package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.util.Screen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferenceActivity extends Activity {

    @BindView(R.id.grid_height)
    ValueSetSeekBar<Integer> gridHeight;

    @BindView(R.id.grid_width)
    ValueSetSeekBar<Integer> gridWidth;

    @BindView(R.id.cell_size)
    ValueSetSeekBar<Integer> cellSize;

    @BindView(R.id.color_picker_preference)
    ColorPickerPreference colorPickerPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences2);
        ButterKnife.bind(this);

        List<Screen.Resolution> resolutions = Screen.getAvailableResolutions(this);

        final List<Integer> gridWidths = new ArrayList<>();
        final List<Integer> gridHeights = new ArrayList<>();
        List<Integer> cellSizes = new ArrayList<>();

        for(Screen.Resolution resolution : resolutions){
            gridWidths.add(resolution.gridWidth);
            gridHeights.add(resolution.gridHeight);
            cellSizes.add(resolution.cellSize);
        }

        gridWidth.setValues(gridWidths);
        gridHeight.setValues(gridHeights);
        cellSize.setValues(cellSizes);

        cellSize.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                gridWidth.setPosition(position);
                gridHeight.setPosition(position);
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
