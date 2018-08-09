package com.simple.review.animator;

import android.animation.TypeEvaluator;

/**
 * @author hych
 * @date 2018/8/9 16:47
 */
public class PointEvaluator implements TypeEvaluator<Point> {
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
        Point point = new Point(x, y);
        return point;
    }
}
