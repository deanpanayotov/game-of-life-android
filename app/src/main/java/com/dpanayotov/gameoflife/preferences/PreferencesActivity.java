package com.dpanayotov.gameoflife.preferences;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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

public class PreferencesActivity extends Activity implements SurfaceHolder.Callback {

    @BindView(R.id.grid_width_height)
    ValueSetSeekBar<Resolution> gridWidthHeight;

    @BindView(R.id.cell_size)
    ValueSetSeekBar<Integer> cellSize;

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

    @BindView(R.id.color_background)
    View colorBackgorund;

    @BindView(R.id.color_secondary)
    LinearLayout colorSecondary;

    @BindView(R.id.color_primary)
    View colorPrimary;

    @BindView(R.id.swap_left)
    View swapLeft;

    @BindView(R.id.swap_right)
    View swapRight;

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

    private void calculateAndInitDemo() {
        calculateDimensions();
        initDemo();
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
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
                calculateAndInitDemo();
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

        updateColors();

        colorBackgorund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog(Preferences.Colors.BACKGROUND);
            }
        });
        colorSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog(Preferences.Colors.SECONDARY);
            }
        });
        colorPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog(Preferences.Colors.PRIMARY);
            }
        });

        swapLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapColors(Preferences.Colors.BACKGROUND, Preferences.Colors.SECONDARY);
            }
        });

        swapRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapColors(Preferences.Colors.SECONDARY, Preferences.Colors.PRIMARY);
            }
        });

    }

    private void swapColors(Preferences.Colors a, Preferences.Colors b) {
        Preferences.swapColors(a, b);
        updateColors();
        initDemo();
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
        animateBackgroundColor(colorBackgorund, Preferences.getColor(Preferences.Colors
                .BACKGROUND));
        animateBackgroundColor(colorSecondary, Preferences.getColor(Preferences.Colors.BACKGROUND));
        for (int i = 0; i < colorSecondary.getChildCount(); i++) {
            animateBackgroundColor(colorSecondary.getChildAt(i), Preferences.getColor(Preferences
                    .Colors.SECONDARY));
        }
        animateBackgroundColor(colorPrimary, Preferences.getColor(Preferences.Colors.PRIMARY));
    }

    private void animateBackgroundColor(final View view, int newColor) {
        ColorDrawable currentColorDrawable = (ColorDrawable) view.getBackground();
        int oldColor = currentColorDrawable.getColor();
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor,
                newColor);
        colorAnimation.setDuration(500); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        calculateAndInitDemo();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}
