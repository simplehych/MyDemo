package com.simple.practice.BezierCubic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hych on 2017/5/5 12:24
 */

public class BezierCubicView extends View {

    private Paint mPaint;
    private int mCenterX, mCenterY;
    private PointF mStartPoint, mEndPoint, mControlPoint1, mControlPoint2;
    private int mSelectControlPoint = 0;

    public BezierCubicView(Context context) {
        this(context, null);
    }

    public BezierCubicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setTextSize(60);

        mStartPoint = new PointF(0, 0);
        mEndPoint = new PointF(0, 0);
        mControlPoint1 = new PointF(0, 0);
        mControlPoint2 = new PointF(0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mStartPoint.x = mCenterX - 200;
        mStartPoint.y = mCenterY;

        mEndPoint.x = mCenterX + 200;
        mEndPoint.y = mCenterY;

        mControlPoint1.x = mCenterX - 150;
        mControlPoint1.y = mCenterY - 100;

        mControlPoint2.x = mCenterX + 150;
        mControlPoint2.y = mCenterY - 100;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(mStartPoint.x, mStartPoint.y, mPaint);
        canvas.drawPoint(mEndPoint.x, mEndPoint.y, mPaint);
        canvas.drawPoint(mControlPoint1.x, mControlPoint1.y, mPaint);
        canvas.drawPoint(mControlPoint2.x, mControlPoint2.y, mPaint);

        //绘制辅助线
        mPaint.setStrokeWidth(4);
        canvas.drawLine(mStartPoint.x, mStartPoint.y, mControlPoint1.x, mControlPoint1.y, mPaint);
        canvas.drawLine(mControlPoint1.x, mControlPoint1.y, mControlPoint2.x, mControlPoint2.y, mPaint);
        canvas.drawLine(mControlPoint2.x, mControlPoint2.y, mEndPoint.x, mEndPoint.y, mPaint);

        //绘制贝塞尔曲线
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);
        Path path = new Path();
        path.moveTo(mStartPoint.x, mStartPoint.y);
        path.cubicTo(mControlPoint1.x, mControlPoint1.y, mControlPoint2.x, mControlPoint2.y, mEndPoint.x, mEndPoint.y);
        canvas.drawPath(path, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //根据触摸的位置更新控制点，并提示重绘
        if (mSelectControlPoint == 0) {
            mControlPoint1.x = event.getX();
            mControlPoint1.y = event.getY();
        } else if (mSelectControlPoint == 1) {
            mControlPoint2.x = event.getX();
            mControlPoint2.y = event.getY();
        }
        invalidate();
        return true;
    }

    public void selectControlPoint(int point) {
        this.mSelectControlPoint = point;
    }

}
