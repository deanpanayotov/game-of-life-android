package com.dpanayotov.wallpaper;

import android.graphics.Color;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public abstract class ColorDispenser {
    protected short h;
    protected short s;
    protected short v;
    protected short hueStep;
    protected float valueStep;

    public ColorDispenser(short h, short s, short v, short hueStep, short valueStep) {
        this.h = h;
        this.s = s;
        this.v = v;
        this.hueStep = hueStep;
        this.valueStep = valueStep;
    }

    public int nextColor() {

        int color = Color.HSVToColor(new float[]{h, s/100f, v/100f});
        runColorStrategy();
        return color;
    }

    protected abstract void runColorStrategy();
}
