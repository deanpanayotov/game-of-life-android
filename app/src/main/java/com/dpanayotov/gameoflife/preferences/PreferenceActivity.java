package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.util.Resolution;
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
    ValueSetSeekBar<Resolution> gridWidthHeight;

    @BindView(R.id.cell_size)
    ValueSetSeekBar<Integer> cellSize;

    @BindView(R.id.color_picker_preference)
    ColorPickerPreference colorPickerPreference;

    @BindView(R.id.tick_rate)
    ValueSetSeekBar<Integer> tickRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences3);
        ButterKnife.bind(this);

        List<Resolution> resolutions = ScreenUtil.getAvailableResolutions(this);

        List<Integer> cellSizes = new ArrayList<>();

        for (Resolution resolution : resolutions) {
            cellSizes.add(resolution.cellSize);
        }

        gridWidthHeight.setValues(resolutions);
        cellSize.setValues(cellSizes);
        cellSize.setPosition(Preferences.getResolution());
        cellSize.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                gridWidthHeight.setPosition(position);
                Preferences.setResolution(position);
            }
        });

        updateColors();

        colorPickerPreference.setOnClickListener(new ColorPickerPreference.OnClickListener() {
            @Override
            public void onPrimaryColorClick() {
                showColorPickerDialog(Preferences.Colors.PRIMARY);
            }

            @Override
            public void onBackgroundColorClick() {
                showColorPickerDialog(Preferences.Colors.BACKGROUND);
            }
        });

        tickRate.setValues(Preferences.getTickRates());
        tickRate.setPosition(Preferences.getTickRate());
        tickRate.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setTickRate(position);
            }
        });
    }

    private void showColorPickerDialog(final Preferences.Colors color) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        colorPickerDialog.hideOpacityBar();
        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int value, String hexVal) {
                Preferences.setColor(color, value);
                updateColors();
            }
        });
        colorPickerDialog.setInitialColor(Preferences.getColor(color));
        colorPickerDialog.show();
    }

    private void updateColors() {
        colorPickerPreference.setPrimaryColor(Preferences.getColor(Preferences.Colors.PRIMARY));
        colorPickerPreference.setBackgroundColor(Preferences.getColor(Preferences.Colors
                .BACKGROUND));
    }
}
