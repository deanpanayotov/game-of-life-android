package com.dpanayotov.wallpaper;

/**
 * Created by Dean Panayotov Local on 7.9.2015
 */
public class LinearColorDispenser extends ColorDispenser {

    protected float valueLimit;
    protected float initialValue;
    protected byte direction = 1;

    public LinearColorDispenser(short h, short s, short v, short hueStep, short valueStep, short
            valueLimit) {
        super(h, s, v, hueStep, valueStep);
        this.valueLimit = valueLimit;
        this.initialValue = v;

        if((valueLimit - v) % valueStep != 0){
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void runColorStrategy() {
        v += valueStep * direction;
        if (v == valueLimit || v == initialValue) {
            direction *= -1;
            if(direction == 1){
                h += hueStep;
                h %= 360;
            }
        }
    }
}
