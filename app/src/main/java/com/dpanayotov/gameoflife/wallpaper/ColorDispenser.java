package com.dpanayotov.gameoflife.wallpaper;

import android.graphics.Color;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public abstract class ColorDispenser {
    protected int h;
    protected int s;
    protected int v;
    protected int hueStep;
    protected float valueStep;

    public ColorDispenser(int h, int s, int v, int hueStep, int valueStep) {
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
