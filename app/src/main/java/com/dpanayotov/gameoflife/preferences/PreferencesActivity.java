package com.dpanayotov.gameoflife.preferences;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dpanayotov.gameoflife.R;
import com.dpanayotov.gameoflife.life.Life;
import com.dpanayotov.gameoflife.preferences.custom.ColorNamesAdapter;
import com.dpanayotov.gameoflife.preferences.custom.ColorValuesAdapter;
import com.dpanayotov.gameoflife.preferences.custom.SwitchPreference;
import com.dpanayotov.gameoflife.preferences.custom.ValueSetSeekBarPreference;
import com.dpanayotov.gameoflife.util.Resolution;
import com.dpanayotov.gameoflife.util.ScreenUtil;

import com.woxthebox.draglistview.DragListView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    //-------------

    @BindView(R.id.list_color_names)
    RecyclerView listColorNames;

    @BindView(R.id.list_color_values)
    DragListView listColorValues;
    ColorValuesAdapter colorValuesAdapter;
    List<Pair<Integer, Integer>> colorValues;

    @BindView(R.id.colorpicker_frame)
    FrameLayout colorPickerFrame;

    @BindView(R.id.holo_colorpicker)
    ColorPicker colorPicker;

    @BindView(R.id.holo_svbar)
    SVBar svBar;

    @BindView(R.id.button_done)
    TextView buttonDone;

    @BindView(R.id.hex_code_input)
    EditText hexCodeInput;

    private Life life;

    private int canvasWidth;
    private int canvasHeight;

    ColorValuesAdapter.OnItemClickedListener onItemClickedListener = new ColorValuesAdapter
            .OnItemClickedListener() {
        @Override
        public void onItemClicked(int position) {
            Log.d("zxc", "onItemClicked: " + position);
            showColorPickerDialog(Preferences.Color.values()[position]);
        }
    };

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

        listColorNames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        listColorNames.setAdapter(new ColorNamesAdapter());

        listColorValues.setLayoutManager(new LinearLayoutManager(this));
        listColorValues.setCanDragHorizontally(false);
        listColorValues.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    colorItemDragged(fromPosition, toPosition);
                    colorValues = constructColorValues();
                    initDemo();
                }
            }
        });

        colorPicker.addSVBar(svBar);
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                setHexInput(color);
            }
        });

        initFadeAnimations();

        initColorValues();

        hexCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("zxc", "afterTextChanged");
                if (s.toString().length() == 6) {
                    String newColor = s.toString().toUpperCase();
                    String oldColor = String.format("%06X", (0xFFFFFF & colorPicker.getColor()));
                    if (!newColor.equals(oldColor)) {
                        Log.d("zxc", newColor + " " + colorPicker.getColor());
                        colorPicker.setColor(Color.parseColor("#"+newColor));
                    }
                }
            }
        });
    }

    private void initColorValues() {
        colorValues = constructColorValues();
        colorValuesAdapter = new ColorValuesAdapter(new ArrayList<>(colorValues),
                onItemClickedListener);
        listColorValues.post(new Runnable() {
            @Override
            public void run() {
                fadeListColorValues(false);
            }
        });
        listColorValues.setAdapter(colorValuesAdapter, true);
    }

    private void colorItemDragged(int from, int to) {
        setColorValueByPosition(to, from);
        if (from < to) {
            for (int i = from; i < to; i++) {
                setColorValueByPosition(i, i + 1);
            }
        } else {
            for (int i = to + 1; i <= from; i++) {
                setColorValueByPosition(i, i - 1);
            }
        }
    }

    private void setColorValueByPosition(int target, int source) {
        Preferences.setColor(Preferences.Color.values()[target], colorValues.get(source).second);
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
        colorPicker.setOldCenterColor(Preferences.getColor(color));
        colorPicker.setColor(Preferences.getColor(color));
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.setColor(color, colorPicker.getColor());
                updateColors();
                initDemo();
                buttonDone.setOnClickListener(null);
                cancelColorPicker();
            }
        });
        setHexInput(Preferences.getColor(color));
        showColorPickerFrame();
    }

    private void setHexInput(int color) {
        String newColor = String.format("%06X", (0xFFFFFF & color));
        Log.d("zxc", "onColorChanged");
        if (!newColor.equals(hexCodeInput.getText().toString().toUpperCase())) {
            Log.d("zxc", newColor + " " + hexCodeInput.getText().toString().toUpperCase());
            hexCodeInput.setText(newColor);
        }
    }

    @OnClick(R.id.button_cancel)
    void cancelColorPicker() {
        hideColorPickerFrame();
    }

    private Animation fadeInListColorValues;
    private Animation fadeOutListColorValues;

    private Animation fadeInColorPickerFrame;
    private Animation fadeOutColorPickerFrame;

    private static final long FADE_ANIMATION_DURATION = 300;

    private void initFadeAnimations() {
        fadeInListColorValues = new AlphaAnimation(0, 1);
        fadeInListColorValues.setDuration(FADE_ANIMATION_DURATION);

        fadeOutListColorValues = new AlphaAnimation(1, 0);
        fadeOutListColorValues.setDuration(FADE_ANIMATION_DURATION);
        fadeOutListColorValues.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initColorValues();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeInColorPickerFrame = new AlphaAnimation(0, 1);
        fadeInColorPickerFrame.setDuration(FADE_ANIMATION_DURATION);

        fadeOutColorPickerFrame = new AlphaAnimation(1, 0);
        fadeOutColorPickerFrame.setDuration(FADE_ANIMATION_DURATION);
        fadeOutColorPickerFrame.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                colorPickerFrame.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void fadeListColorValues(boolean out) {
        listColorValues.startAnimation(out ? fadeOutListColorValues : fadeInListColorValues);
    }

    private void showColorPickerFrame() {
        colorPickerFrame.setVisibility(View.VISIBLE);
        colorPickerFrame.startAnimation(fadeInColorPickerFrame);
    }

    private void hideColorPickerFrame() {
        colorPickerFrame.startAnimation(fadeOutColorPickerFrame);
    }

    private void updateColors() {
        fadeListColorValues(true);
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
        life = null;
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


    @Override
    public void onBackPressed() {
        if (colorPickerFrame.getVisibility() == View.VISIBLE) {
            hideColorPickerFrame();
        } else {
            super.onBackPressed();
        }
    }
}
