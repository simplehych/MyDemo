package com.simple.review.animator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

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
        currentPoint = new Point(RADIUS, RADIUS);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        System.out.println("MyAnimView  onDraw");
        drawCircle(canvas);
        startAnimation();
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    private void startAnimation() {
        PointEvaluator pointEvaluator = new PointEvaluator();
        Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator animator = ValueAnimator.ofObject(pointEvaluator, startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(5000);
        animator.start();
    }
}
