package com.dpanayotov.gameoflife.preferences;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.preferences.custom.SwitchPreference;
import com.dpanayotov.gameoflife.preferences.custom.ValueSetSeekBarPreference;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class PreferencesActivity extends Activity implements SurfaceHolder.Callback {

    @BindView(R.id.grid_width_height)
    ValueSetSeekBarPreference<Resolution> gridWidthHeight;

    @BindView(R.id.cell_size)
    ValueSetSeekBarPreference<Integer> cellSize;

    @BindView(R.id.tick_rate)
    ValueSetSeekBarPreference<Integer> tickRate;

    @BindView(R.id.isometric_projection)
    SwitchPreference isometricProjection;

    @BindView(R.id.highlife)
    SwitchPreference highlife;

    @BindView(R.id.initial_population_density)
    ValueSetSeekBarPreference<Integer> initialPopulationDensity;

    @BindView(R.id.min_population_density)
    ValueSetSeekBarPreference<Integer> minPopulationDensity;

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

    @BindView(R.id.progress_overlay)
    View progressOverlay;

    @BindView(R.id.lobster_picker_frame)
    FrameLayout lobsterPickerFrame;

    @BindView(R.id.lobster_picker)
    LobsterPicker lobsterPicker;

    @BindView(R.id.lobster_shadeslider)
    LobsterShadeSlider lobsterShadeSlider;

    @BindView(R.id.button_cancel)
    TextView buttonCancel;

    @BindView(R.id.button_done)
    TextView buttonDone;

    private Life life;

    private int canvasWidth;
    private int canvasHeight;

    private void calculateDimensions() {
        Rect surfaceFrame = surfaceView.getHolder().getSurfaceFrame();
        canvasWidth = surfaceFrame.width();
        canvasHeight = surfaceFrame.height();
    }

    private void initDemo() {
        if (life != null) {
            life.destroy();
        }

        life = new Life(canvasWidth, canvasHeight, surfaceView.getHolder(), true);
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
        cellSize.setOnValueChangeListener(new ValueSetSeekBarPreference
                .OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                gridWidthHeight.setPosition(position);
                Preferences.setResolution(position);
                calculateAndInitDemo();
            }
        });

        tickRate.setValues(Preferences.getTickRates());
        tickRate.setPosition(Preferences.getTickRate());
        tickRate.setOnValueChangeListener(new ValueSetSeekBarPreference
                .OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setTickRate(position);
                initDemo();
            }
        });


        isometricProjection.setChecked(Preferences.getIsometricProjection());
        isometricProjection.setOnCheckedChangeListener(new SwitchPreference.OnCheckedChangeListener
                () {

            @Override
            public void onCheckedChanged(boolean checked) {
                Preferences.setIsometricProjection(checked);
                initDemo();
            }
        });

        highlife.setChecked(Preferences.getHighlife());
        highlife.setOnCheckedChangeListener(new SwitchPreference.OnCheckedChangeListener
                () {

            @Override
            public void onCheckedChanged(boolean checked) {
                Preferences.setHighlife(checked);
                initDemo();
            }
        });

        initialPopulationDensity.setValues(Preferences.getInitialPopulationDensityOptions());
        initialPopulationDensity.setPosition(Preferences.getInitialPopulationDensity());
        initialPopulationDensity.setOnValueChangeListener(new ValueSetSeekBarPreference
                .OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setInitialPopulationDensity(position);
                initDemo();
            }
        });

        minPopulationDensity.setValues(Preferences.getMinPopulationDensityOptions());
        minPopulationDensity.setPosition(Preferences.getMinPopulationDensity());
        minPopulationDensity.setOnValueChangeListener(new ValueSetSeekBarPreference
                .OnValueChangeListener<Integer>() {
            @Override
            public void onValueChange(Integer value, int position) {
                Preferences.setMinPopulationDensity(position);
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

        lobsterPicker.addDecorator(lobsterShadeSlider);

    }

    private void swapColors(Preferences.Colors a, Preferences.Colors b) {
        Preferences.swapColors(a, b);
        updateColors();
        initDemo();
    }

    private void showColorPickerDialog(final Preferences.Colors color) {
        progressOverlay.setVisibility(View.VISIBLE);
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
        colorPickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                progressOverlay.setVisibility(View.INVISIBLE);
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
        life.destroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (life != null) {
            life.stop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (life != null) {
            life.start();
        }
    }
}
