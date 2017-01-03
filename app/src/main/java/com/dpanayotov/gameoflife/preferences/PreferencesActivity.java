package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.preferences.custom.ColorNamesAdapter;
import com.dpanayotov.gameoflife.preferences.custom.ColorValuesAdapter;
import com.dpanayotov.gameoflife.preferences.custom.SwitchPreference;
import com.dpanayotov.gameoflife.preferences.custom.ValueSetSeekBarPreference;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;
import com.woxthebox.draglistview.DragListView;

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

    @BindView(R.id.progress_overlay)
    View progressOverlay;

    //-------------

    @BindView(R.id.list_color_names)
    RecyclerView listColorNames;

    @BindView(R.id.list_color_values)
    DragListView listColorValues;
    ColorValuesAdapter colorValuesAdapter;
    List<Pair<Integer, Integer>> colorValues;

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

        listColorNames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listColorNames.setAdapter(new ColorNamesAdapter());


        colorValues = constructColorValues();
        colorValuesAdapter = new ColorValuesAdapter(colorValues);
        listColorValues.setLayoutManager(new LinearLayoutManager(this));
        listColorValues.setAdapter(colorValuesAdapter, true);
        listColorValues.setCanDragHorizontally(false);
       
    }

    private List<Pair<Integer, Integer>> constructColorValues() {
        Preferences.Color[] colors = Preferences.Color.values();
        List<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            pairs.add(new Pair<>(i, Preferences.getColor(colors[i])));
        }
        return pairs;
    }

    private void showColorPickerDialog(final Preferences.Color color) {
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
        //TODO
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
