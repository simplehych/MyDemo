package com.simple.review.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * @author hych
 * @date 2018/8/9 11:11
 */
public class ValueAnimatorTest {

    public static final String TAG = "ValueAnimatorTest";

    public static void testValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                System.out.println(TAG + " onAnimationUpdate: " + animatedValue);
            }
        });
        valueAnimator.setStartDelay(1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void testObjectAnimator(Context context) {


        PointEvaluator pointEvaluator = new PointEvaluator();
        Point pointStart = new Point(0, 0);
        Point pointEnd = new Point(300, 300);
        ValueAnimator animator = ValueAnimator.ofObject(pointEvaluator, pointStart, pointEnd);
        animator.setDuration(5000);
        animator.start();
        animator.end();
        animator.cancel();
        View view = new TextView(context);
        view.animate()
                .alpha(0.5f)
                .x(50)
                .y(50)
                .translationY(50)
                .setDuration(5000)
                .setInterpolator(new LinearInterpolator())
                .setStartDelay(30)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                    }
                })
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }
                });

    }


}
