package com.simple.review.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

/**
 * @author hych
 * @date 2018/8/9 17:00
 */
public class MyAnimView extends View {
    private static final float RADIUS = 50f;
    private Point currentPoint;
    private Paint mPaint;

    public MyAnimView(Context context) {
        this(context, null);
    }

    public MyAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("MyAnimView  onDraw");
        if (currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    private void startAnimation() {
        PointEvaluator pointEvaluator = new PointEvaluator();
        Point startPoint = new Point(getWidth() / 2, RADIUS);
        Point endPoint = new Point(getWidth() / 2, getHeight() - RADIUS);
        ValueAnimator pointAnimator = ValueAnimator.ofObject(pointEvaluator, startPoint, endPoint);
        pointAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });

        ObjectAnimator colorAnimator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            colorAnimator = ObjectAnimator.ofArgb(this, "intColor", Color.parseColor("#0000FF"), Color.parseColor("#FF0000"));
        } else {
            colorAnimator = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                    "#0000FF", "#FF0000");
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(pointAnimator).with(colorAnimator);
        animatorSet.setDuration(3000);
        animatorSet.setInterpolator(new DecelerateAccelerateInterpolator());
        animatorSet.start();
    }

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }


    private int intColor;

    public int getIntColor() {
        return intColor;
    }

    public void setIntColor(int intColor) {
        this.intColor = intColor;
        mPaint.setColor(intColor);
        invalidate();
    }
}
