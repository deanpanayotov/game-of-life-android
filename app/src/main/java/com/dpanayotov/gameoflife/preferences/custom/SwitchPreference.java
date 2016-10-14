package com.dpanayotov.gameoflife.preferences.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dpanayotov.gameoflife.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dean Panayotov on 10/15/2016
 */

public class SwitchPreference extends LinearLayout {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.check_box)
    Switch checkBox;

    OnCheckedChangeListener onCheckedChangeListener;

    public SwitchPreference(Context context) {
        super(context);
        init(null);
    }

    public SwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(getAttributes(context, attrs));
    }

    public SwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(getAttributes(context, attrs));
    }

    private void init(SwitchPreference.Attributes attributes) {
        View root = inflate(getContext(), R.layout.view_switch_preference, this);
        ButterKnife.bind(root, this);
        if (attributes != null) {
            if (attributes.title != null) {
                title.setText(attributes.title);
            }
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(isChecked);
                }
            }
        });
    }

    private SwitchPreference.Attributes getAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .ValueSetSeekBarPreference, 0, 0);
        SwitchPreference.Attributes attributes = null;
        try {
            attributes = new SwitchPreference.Attributes();
            attributes.title = a.getString(R.styleable.Preference_title);
        } finally {
            a.recycle();
        }
        return attributes;
    }

    class Attributes {
        String title;
    }

    public void setOnCheckedChangeListener(SwitchPreference.OnCheckedChangeListener
                                                   onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    public void setChecked(boolean isChecked){
        checkBox.setChecked(isChecked);
    }
}
