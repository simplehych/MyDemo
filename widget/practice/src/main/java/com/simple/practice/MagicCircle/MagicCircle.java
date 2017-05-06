package com.simple.practice.MagicCircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by hych on 2017/5/6 13:47
 */

public class MagicCircle extends View {

    private Path mPath;
    private Paint mFillCirclePaint;

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;

    private float mMaxLength;
    private float mInterpolatedTime;
    private float mStretchDistance;
    private float mMoveDistance;
    private float mDistanceC;
    private float mRadius;
    private float C;
    private float mBlackMagic = 0.551915024494f;

    private VPoint p2, p4;
    private HPoint p1, p3;

    public MagicCircle(Context context) {
        this(context, null);
    }

    public MagicCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getWidth();
        mHeight = getHeight();

        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        mRadius = 50;
        C = mRadius * mBlackMagic;
        mStretchDistance = mRadius;
        mMoveDistance = mRadius * (3 / 5f);
        mDistanceC = C * 0.45f;

        mMaxLength = mWidth - mRadius - mRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        canvas.translate(mRadius, mRadius);

        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
            model1(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
            model2(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
            model3(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
            model4(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1) {
            model5(mInterpolatedTime);
        }

        float offset = mMaxLength * (mInterpolatedTime - 0.2f);
        offset = offset > 0 ? offset : 0;
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);

        mPath.moveTo(p1.x, p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);

        canvas.drawPath(mPath, mFillCirclePaint);
    }

    private void model0() {
        p1.setY(mRadius);
        p3.setY(-mRadius);
        p3.x = p1.x = 0;
        p3.left.x = p1.left.x = -C;
        p3.right.x = p1.right.x = C;

        p2.setX(mRadius);
        p4.setX(-mRadius);
        p2.y = p4.y = 0;
        p2.top.y = p4.top.y = -C;
        p2.bottom.y = p4.bottom.y = C;
    }

    private void model1(float time) {
        model0();
        p2.setX(mRadius + mStretchDistance * time * 5);
    }

    private void model2(float time) {
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        p1.adjustAllX(mStretchDistance / 2 * time);
        p3.adjustAllX(mStretchDistance / 2 * time);
        p2.adjustY(mDistanceC * time);
        p4.adjustY(mDistanceC * time);
    }

    private void model3(float time) {
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        p1.adjustAllX(mStretchDistance / 2 * time);
        p3.adjustAllX(mStretchDistance / 2 * time);
        p2.adjustY(-mDistanceC * time);
        p4.adjustY(-mDistanceC * time);

        p4.adjustAllX(mStretchDistance / 2 * time);
    }

    private void model4(float time) {
        model3(0.8f);
        time = (time - 0.8f) * 10;
        p4.adjustAllX(mStretchDistance / 2 * time);
    }

    private void model5(float time) {
        model4(0.9f);
        time = time - 0.9f;
        p4.adjustAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * mRadius)));
    }

    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFfe626d);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);

        mPath = new Path();

        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
    }

    class VPoint {
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x) {
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset) {
            top.y -= offset;
            bottom.y += offset;
        }

        public void adjustAllX(float offset) {
            this.x += offset;
            top.x += offset;
            bottom.x += offset;
        }
    }

    class HPoint {
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset) {
            this.x += offset;
            left.x += offset;
            right.x += offset;
        }
    }

    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            Log.e("Simple", " ------ " + interpolatedTime);
            invalidate();
        }
    }

    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(1000);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        //move.setRepeatCount(Animation.INFINITE);
        //move.setRepeatMode(Animation.REVERSE);
        startAnimation(move);
    }

}
