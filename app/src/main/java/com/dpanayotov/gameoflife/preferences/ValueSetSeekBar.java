package com.dpanayotov.gameoflife.preferences;

import android.content.Context;
import android.content.res.TypedArray;
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

public class ValueSetSeekBar<T> extends LinearLayout {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.value)
    TextView value;
    @BindView(R.id.suffix)
    TextView suffix;
    @BindView(R.id.seekbar)
    SeekBar seekBar;

    List<T> set;

    OnValueChangeListener<T> onValueChangeListener;

    T currentValue;

    public ValueSetSeekBar(Context context) {
        super(context);
        init(null);
    }

    public ValueSetSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getAttributes(context, attrs));
    }

    public ValueSetSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(getAttributes(context, attrs));
    }

    private void init(Attributes attributes) {
        View root = inflate(getContext(), R.layout.value_set_seekbar, this);
        ButterKnife.bind(root, this);
        if (attributes != null) {
            if (attributes.title != null) {
                title.setText(attributes.title);
            }
            if (attributes.suffix != null) {
                suffix.setText(attributes.suffix);
            }
            if (!attributes.showSeekBar) {
                seekBar.setVisibility(GONE);
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (set != null) {
                    currentValue = set.get(progress);
                    value.setText(currentValue.toString());
                    if (onValueChangeListener != null) {
                        onValueChangeListener.onValueChange(currentValue);
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

    public void setValues(List<T> values) {
        set = new ArrayList<>();
        set.addAll(values);

        int seekBarMax = values.size() - 1;
        int seekBarDefault = seekBarMax / 2;

        seekBar.setMax(seekBarMax);
        seekBar.setProgress(seekBarDefault);
    }

    public void setOnValueChangeListener(OnValueChangeListener<T> onValueChangedListener) {
        this.onValueChangeListener = onValueChangedListener;
    }

    interface OnValueChangeListener<U> {
        void onValueChange(U value);
    }

    private Attributes getAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .ValueSetSeekBar, 0, 0);
        Attributes attributes = null;
        try {
            attributes = new Attributes();
            attributes.showSeekBar = a.getBoolean(R.styleable.ValueSetSeekBar_show_bar, true);
            attributes.title = a.getString(R.styleable.ValueSetSeekBar_title);
            attributes.suffix = a.getString(R.styleable.ValueSetSeekBar_suffix);
        } finally {
            a.recycle();
        }
        return attributes;
    }

    class Attributes {
        boolean showSeekBar;
        String title;
        String suffix;
    }
}
