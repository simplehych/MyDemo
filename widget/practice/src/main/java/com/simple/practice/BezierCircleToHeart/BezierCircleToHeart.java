package com.simple.practice.BezierCircleToHeart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hych on 2017/5/6 10:41
 */

public class BezierCircleToHeart extends View {

    private static final float C = 0.551915024494f;     //一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置

    private Paint mPaint;
    private int mCenterX, mCenterY;

    private PointF mCenter = new PointF(0, 0);
    private float mCircleRadius = 200;                  //圆的半径
    private float mDifference = mCircleRadius * C;      //圆形控制点与数据点的差值

    private float[] mDataPoints = new float[8];         //顺时针记录绘制圆形的四个数据点
    private float[] mCtrlPoints = new float[16];        //顺时针记录绘制圆形的八个控制点

    private float mDuration = 1000;                     //变化总时长
    private float mCurTime = 0;                         //当前已进行时长
    private float mTimeCount = 100;                     //将时长总共划分为多少份
    private float mPieceTime = mDuration / mTimeCount;  //每一份的时长

    public BezierCircleToHeart(Context context) {
        this(context, null);
    }

    public BezierCircleToHeart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60);

        //初始化数据点  顺时针方向  从Y轴正方向开始
        mDataPoints[0] = 0;
        mDataPoints[1] = mCircleRadius;

        mDataPoints[2] = mCircleRadius;
        mDataPoints[3] = 0;

        mDataPoints[4] = 0;
        mDataPoints[5] = -mCircleRadius;

        mDataPoints[6] = -mCircleRadius;
        mDataPoints[7] = 0;

        //初始化控制点
        mCtrlPoints[0] = mDataPoints[0] + mDifference;
        mCtrlPoints[1] = mDataPoints[1];

        mCtrlPoints[2] = mDataPoints[2];
        mCtrlPoints[3] = mDataPoints[3] + mDifference;

        mCtrlPoints[4] = mDataPoints[2];
        mCtrlPoints[5] = mDataPoints[3] - mDifference;

        mCtrlPoints[6] = mDataPoints[4] + mDifference;
        mCtrlPoints[7] = mDataPoints[5];

        mCtrlPoints[8] = mDataPoints[4] - mDifference;
        mCtrlPoints[9] = mDataPoints[5];

        mCtrlPoints[10] = mDataPoints[6];
        mCtrlPoints[11] = mDataPoints[7] - mDifference;

        mCtrlPoints[12] = mDataPoints[6];
        mCtrlPoints[13] = mDataPoints[7] + mDifference;

        mCtrlPoints[14] = mDataPoints[0] - mDifference;
        mCtrlPoints[15] = mDataPoints[1];
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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCoordinateSystem(canvas);           //绘制坐标系

        canvas.translate(mCenterX, mCenterY);   //将坐标系移动到画布中央
        canvas.scale(1, -1);                    //翻转Y轴

        drawAuxiliaryLine(canvas);              //绘制数据点，控制点，辅助线
        drawCircleToHeart(canvas);
    }

    /**
     * @param canvas
     */
    private void drawCircleToHeart(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.moveTo(mDataPoints[0], mDataPoints[1]);

        path.cubicTo(mCtrlPoints[0], mCtrlPoints[1], mCtrlPoints[2], mCtrlPoints[3], mDataPoints[2], mDataPoints[3]);
        path.cubicTo(mCtrlPoints[4], mCtrlPoints[5], mCtrlPoints[6], mCtrlPoints[7], mDataPoints[4], mDataPoints[5]);
        path.cubicTo(mCtrlPoints[8], mCtrlPoints[9], mCtrlPoints[10], mCtrlPoints[11], mDataPoints[6], mDataPoints[7]);
        path.cubicTo(mCtrlPoints[12], mCtrlPoints[13], mCtrlPoints[14], mCtrlPoints[15], mDataPoints[0], mDataPoints[1]);
        canvas.drawPath(path, mPaint);

        /**
         * 通过一个的时间间隔，来改变数据点和控制点来重新绘制图形
         */
        mCurTime += mPieceTime;
        if (mCurTime < mDuration) {

            mDataPoints[1] -= 120 / mTimeCount;
            mCtrlPoints[6] -= 50 / mTimeCount;
            mCtrlPoints[8] += 50 / mTimeCount;

            postInvalidateDelayed((long) mPieceTime);
        }
    }


    /**
     * 绘制辅助线
     *
     * @param canvas
     */
    private void drawAuxiliaryLine(Canvas canvas) {
        //绘制数据点和控制点
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(10);

        //绘制数据点
        for (int i = 0; i < mDataPoints.length; i += 2) {
            canvas.drawPoint(mDataPoints[i], mDataPoints[i + 1], mPaint);
        }

        //绘制控制点
        for (int i = 0; i < mCtrlPoints.length; i += 2) {
            canvas.drawPoint(mCtrlPoints[i], mCtrlPoints[i + 1], mPaint);
        }

        //绘制控制线
        mPaint.setStrokeWidth(4);
        for (int i = 2, j = 2; i < 8; i += 2, j += 4) {
            canvas.drawLine(mDataPoints[i], mDataPoints[i + 1], mCtrlPoints[j], mCtrlPoints[j + 1], mPaint);
            canvas.drawLine(mDataPoints[i], mDataPoints[i + 1], mCtrlPoints[j + 2], mCtrlPoints[j + 3], mPaint);
        }
        canvas.drawLine(mDataPoints[0], mDataPoints[1], mCtrlPoints[0], mCtrlPoints[1], mPaint);
        canvas.drawLine(mDataPoints[0], mDataPoints[1], mCtrlPoints[14], mCtrlPoints[15], mPaint);
    }


    /**
     * 绘制坐标系
     *
     * @param canvas
     */
    private void drawCoordinateSystem(Canvas canvas) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);    //将坐标系移动到画布中央
        canvas.scale(1, -1);                     //翻转Y轴

        Paint tmpPaint = new Paint();
        tmpPaint.setColor(Color.RED);
        tmpPaint.setStrokeWidth(5);
        tmpPaint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0, -2000, 0, 2000, tmpPaint);
        canvas.drawLine(-2000, 0, 2000, 0, tmpPaint);
        canvas.restore();
    }
}
