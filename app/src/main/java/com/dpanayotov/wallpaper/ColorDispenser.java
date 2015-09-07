package com.dpanayotov.wallpaper;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public abstract class ColorDispenser {
    protected short h;
    protected float s;
    protected float v;
    protected short hueStep;
    protected float valueStep;

    public ColorDispenser(short h, float s, float v, short hueStep, float valueStep) {
        this.h = h;
        this.s = s;
        this.v = v;
        this.hueStep = hueStep;
        this.valueStep = valueStep;
    }

    public int nextColor() {

        int color = Color.HSVToColor(new float[]{h, s, v});
        runColorStrategy();
        Log.d("zxc", "zxc color: " + color);
        Log.d("zxc", "zxc color v: " + v);

        return color;
    }

    protected abstract void runColorStrategy();
}
