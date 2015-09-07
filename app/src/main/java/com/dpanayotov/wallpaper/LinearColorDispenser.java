package com.dpanayotov.wallpaper;

import android.util.Log;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public class LinearColorDispenser extends ColorDispenser {

    protected float valueLimit;
    protected float initialValue;
    protected byte direction = 1;

    public LinearColorDispenser(short h, float s, float v, short hueStep, float valueStep, float
            valueLimit) {
        super(h, s, v, hueStep, valueStep);
        this.valueLimit = valueLimit;
        this.initialValue = v;
    }

    @Override
    protected void runColorStrategy() {
        v += valueStep * direction;
        Log.d("zxc", "zxc v:" + v);
        if (v == valueLimit || v == initialValue) {
            direction *= -1;
            if(direction == 1){
                h += hueStep;
                h %= 360;
            }
        }
    }
}
