package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferenceActivity extends Activity implements SurfaceHolder.Callback {

    @BindView(R.id.grid_width_height)
    ValueSetSeekBar<Resolution> gridWidthHeight;

    @BindView(R.id.cell_size)
    ValueSetSeekBar<Integer> cellSize;

    @BindView(R.id.color_picker_preference)
    ColorPickerPreference colorPickerPreference;

    @BindView(R.id.tick_rate)
    ValueSetSeekBar<Integer> tickRate;

    @BindView(R.id.isometric_projection)
    Switch isometricProjection;

    @BindView(R.id.highlife)
    Switch highlife;

    @BindView(R.id.restard_population)
    ValueSetSeekBar<Integer> restartPopulation;

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;

    private Life life;

    private int canvasWidth;
    private int canvasHeight;
    private int demoGridWidth;
    private int demoGridHeight;

    private void calculateDimensions() {
        Resolution resolution = Preferences.getResolutions().get(Preferences.getResolution());
        Rect surfaceFrame = surfaceView.getHolder().getSurfaceFrame();
        canvasWidth = surfaceFrame.width();
        canvasHeight = surfaceFrame.height();
        demoGridWidth = (int) Math.ceil((float) canvasWidth / (float) resolution.cellSize);
        demoGridHeight = (int) Math.ceil((float) canvasHeight / (float) resolution.cellSize);
    }

    private void initDemo() {
        if (life != null) {
            life.stop();
        }

        life = new Life(canvasWidth, canvasHeight, demoGridWidth, demoGridHeight, surfaceView
                .getHolder());
        life.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences3);
        ButterKnife.bind(this);

        List<Resolution> resolutions = ScreenUtil.getAvailableResolutions();

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
                calculateDimensions();
                initDemo();
            }
        });

        updateColors();

        colorPickerPreference.setOnClickListener(new ColorPickerPreference.OnClickListener() {
            @Override
            public void onPrimaryColorClick() {
                showColorPickerDialog(Preferences.Colors.PRIMARY);
                initDemo();
            }

            @Override
            public void onBackgroundColorClick() {
                showColorPickerDialog(Preferences.Colors.BACKGROUND);
                initDemo();
            }
        });

        tickRate.setValues(Preferences.getTickRates());
        tickRate.setPosition(Preferences.getTickRate());
        tickRate.setOnValueChangeListener(new ValueSetSeekBar.OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setTickRate(position);
                initDemo();
            }
        });
        isometricProjection.setChecked(Preferences.getIsometricProjection());
        isometricProjection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener
                () {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Preferences.setIsometricProjection(checked);
                initDemo();
            }
        });

        highlife.setChecked(Preferences.getHighlife());
        highlife.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener
                () {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Preferences.setHighlife(checked);
                initDemo();
            }
        });

        restartPopulation.setValues(Preferences.getPopulationPercentages());
        restartPopulation.setPosition(Preferences.getPopulationPercentage());
        restartPopulation.setOnValueChangeListener(new ValueSetSeekBar
                .OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setPopulationPercentage(position);
                initDemo();
            }
        });
        surfaceView.getHolder().addCallback(this);
    }

    private void showColorPickerDialog(final Preferences.Colors color) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this);
        colorPickerDialog.hideOpacityBar();
        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int value, String hexVal) {
                Preferences.setColor(color, value);
                updateColors();
                initDemo();
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        calculateDimensions();
        initDemo();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
