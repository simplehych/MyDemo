package com.simple.review.animator;

import android.animation.ValueAnimator;

/**
 * @author hych
 * @date 2018/8/9 11:11
 */
public class ValueAnimatorTest {

    public static final String TAG = "ValueAnimatorTest";

    public static void testValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float animatedValue = (Float) animation.getAnimatedValue();
                System.out.println(TAG + " onAnimationUpdate: " + animatedValue);
            }
        });
        valueAnimator.setStartDelay(1000);
        valueAnimator.start();
    }

    public static void testObjectAnimator() {

        PointEvaluator pointEvaluator = new PointEvaluator();
        Point pointStart = new Point(0, 0);
        Point pointEnd = new Point(300, 300);
        ValueAnimator animator = ValueAnimator.ofObject(pointEvaluator, pointStart, pointEnd);
        animator.setDuration(5000);
        animator.start();
    }


}
