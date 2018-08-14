package com.simple.review.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import java.security.Key;

/**
 * @author hych
 * @date 2018/8/14 11:23
 */
public class AnimatorTest {
    public static void test(Context context, View view) {
//        Animator animator = AnimatorInflater.loadAnimator(context, R.animator.animator_set);
//        animator.setTarget(view);
//        animator.start();

        Keyframe keyframe0 = Keyframe.ofFloat(0f, 0);
        Keyframe keyframe1 = Keyframe.ofFloat(0.1f, 100);
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 180);
        Keyframe keyframe3 = Keyframe.ofFloat(0.8f, 270);
        Keyframe keyframe4 = Keyframe.ofFloat(1f, 360);

        PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("rotation", keyframe0, keyframe1, keyframe2, keyframe3, keyframe4);
//        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("rotation", 10, 50, 20, 80, 100);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, holder);
        objectAnimator.setTarget(view);
        objectAnimator.setDuration(3000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
        objectAnimator.end();
        objectAnimator.cancel();
    }

    public static void test11(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f);
        animator.setDuration(1000);
        animator.start();


        ObjectAnimator moveIn = ObjectAnimator.ofFloat(view, "translationX", -500f, 0f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotate).with(fadeInOut).before(moveIn);
        animSet.setDuration(5000);
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        moveIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        moveIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        moveIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
    }
}
