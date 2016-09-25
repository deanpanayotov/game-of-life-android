package com.dpanayotov.gameoflife.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dpanayotov.gameoflife.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class ValueSetSeekBar extends LinearLayout {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.value)
    TextView value;
    @BindView(R.id.suffix)
    TextView suffix;
    @BindView(R.id.seekbar)
    SeekBar seekBar;

    List<Integer> set;

    OnValueChangeListener onValueChangeListener;

    public ValueSetSeekBar(Context context) {
        super(context);
        init();
    }

    public ValueSetSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ValueSetSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.value_set_seekbar, this);
        ButterKnife.bind(root, this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (set != null) {
                    Integer val = set.get(progress);
                    value.setText(val.toString());
                    if(onValueChangeListener!=null){
                        onValueChangeListener.onValueChange(val);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setValues(List<Integer> values) {
        set = new ArrayList<>();
        set.addAll(values);

        int seekBarMax = values.size() - 1;
        int seekBarDefault = seekBarMax / 2;

        seekBar.setMax(seekBarMax);
        seekBar.setProgress(seekBarDefault);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangedListener){
        this.onValueChangeListener = onValueChangedListener;
    }

    interface OnValueChangeListener {
        void onValueChange(int value);
    }

}
