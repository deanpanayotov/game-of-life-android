package com.dpanayotov.wallpaper;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public class LinearColorDispenser extends ColorDispenser {

    protected float valueLimit;
    protected byte direction = 1;

    public LinearColorDispenser(short h, float s, float v, short hueStep, float valueStep, float
            valueLimit) {
        super(h, s, v, hueStep, valueStep);
        this.valueLimit = valueLimit;
    }

    @Override
    protected void runColorStrategy() {
        v += valueStep * direction;
        if (v == valueLimit) {
            direction *= -1;
            if(direction == 1){
                h += hueStep;
                h %= 360;
            }
        }
    }
}
