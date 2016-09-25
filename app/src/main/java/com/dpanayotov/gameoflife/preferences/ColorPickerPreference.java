package com.dpanayotov.gameoflife.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.dpanayotov.gameoflife.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 9/25/2016
 */

public class ColorPickerPreference extends LinearLayout {

    @BindView(R.id.primary_color)
    View primaryColor;
    @BindView(R.id.background_color)
    View backgroundColor;

    OnClickListener onClickListener;

    public ColorPickerPreference(Context context) {
        super(context);
        init();
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View root = inflate(getContext(), R.layout.color_settings, this);
        ButterKnife.bind(root, this);
        primaryColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onPrimaryColorClick();
                }
            }
        });
        backgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onBackgroundColorClick();
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    interface OnClickListener {
        void onPrimaryColorClick();
        void onBackgroundColorClick();
    }
}
