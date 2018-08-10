package com.simple.review.animator;

import android.animation.TimeInterpolator;

/**
 * @author hych
 * @date 2018/8/10 10:21
 */
public class DecelerateAccelerateInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        float result;
        if (input <= 0.5) {
            result = (float) Math.sin(Math.PI * input) / 2;
        } else {
            result = (float) (2 - Math.sin(Math.PI * input)) / 2;
        }
        return result;
    }
}
