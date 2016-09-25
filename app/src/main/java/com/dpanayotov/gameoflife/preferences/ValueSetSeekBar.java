package com.dpanayotov.gameoflife.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dpanayotov.gameoflife.R;

/**
 * Created by Dean Panayotov on 9/24/2016
 */

public class ValueSetSeekBar extends LinearLayout {

    TextView title;
    TextView value;
    TextView suffix;
    SeekBar seekBar;

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
        inflate(getContext(), R.layout.value_set_seekbar, this);
        this.title = (TextView)findViewById(R.id.title);
        this.value = (TextView)findViewById(R.id.value);
        this.suffix = (TextView)findViewById(R.id.suffix);
        this.seekBar = (SeekBar) findViewById(R.id.seekbar);
    }

}
