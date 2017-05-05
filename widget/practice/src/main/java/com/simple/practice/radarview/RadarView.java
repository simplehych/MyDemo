package com.simple.practice.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 雷达蛛网图,暂不考虑性能及优化，注意思路清晰
 * Created by hych on 2017/5/4 17:16
 */

public class RadarView extends View {

    private int mPolygionCount = 6;                                 //多边形个数，几边形
    private float mAngle = (float) (Math.PI * 2 / mPolygionCount);  //正多边形的每个内角度数


    private float mMaxRadius;   //最大半径
    private int mCenterX;       //中心点X坐标
    private int mCenterY;       //中心点Y坐标

    private Paint mRadarPaint;      //雷达画笔
    private Paint mTextPaint;       //文字画笔
    private Paint mRegionPaint;     //区域画笔

    private String[] mTitles = {"aaa", "bbb", "ccc", "ddd", "eee", "fff"};
    private int[] mDatas = {100, 70, 50, 80, 30, 60};
    private float mMaxData = 100;//数据最大值

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //初始化雷达画笔
        mRadarPaint = new Paint();
        mRadarPaint.setColor(Color.GRAY);
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarPaint.setAntiAlias(true);

        //初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);

        //初始化区域画笔
        mRegionPaint = new Paint();
        mRegionPaint.setColor(Color.GREEN);
        mRegionPaint.setAlpha(120);//设置透明
        mRegionPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float maxTitleWidth = 0;
        for (int i = 0; i < mPolygionCount; i++) {
            float titleWidth = mTextPaint.measureText(mTitles[i]);
            if (titleWidth > maxTitleWidth) {
                maxTitleWidth = titleWidth;
            }
        }
        mMaxRadius = Math.min(w, h) / 2 * 0.9f - maxTitleWidth;
        mCenterX = w / 2;
        mCenterY = h / 2;
        postInvalidate();       //  和invalidate()的区别    ？？？
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPolygon(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制正多边形，多层网格
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float r = mMaxRadius / (mPolygionCount - 1);    //每个多边形之间的间距，即每个多边形的半径，依次变大
        for (int i = 0; i < mPolygionCount; i++) {
            float curR = r * i;     //当前多边形的半径
            path.reset();
            for (int j = 0; j < mPolygionCount; j++) {
                if (j == 0) {
                    path.moveTo(mCenterX + curR, mCenterY);
                } else {
                    //根据半径计算出正多边形每个点的坐标
                    float curAngle = mAngle * j;
                    float x = (float) (mCenterX + curR * Math.cos(curAngle));
                    float y = (float) (mCenterY + curR * Math.sin(curAngle));
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径，注意是闭合，并非收尾相连，如果收尾相连不是闭合，那么什么都不做
            canvas.drawPath(path, mRadarPaint);
        }
    }

    /**
     * 绘制直线，从中心到末端的直线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mPolygionCount; i++) {
            path.reset();
            path.moveTo(mCenterX, mCenterY);
            float curAngle = mAngle * i;
            float x = (float) (mCenterX + mMaxRadius * Math.cos(curAngle));
            float y = (float) (mCenterY + mMaxRadius * Math.sin(curAngle));
            path.lineTo(x, y);
            canvas.drawPath(path, mRadarPaint);
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < mPolygionCount; i++) {
            float dis = mTextPaint.measureText(mTitles[i]);//文本的长度 这里用作偏距
            float curAngle = mAngle * i;//当前角度
            double pi = Math.PI;// 180度
            float x = (float) (mCenterX + (mMaxRadius + fontHeight / 2) * Math.cos(curAngle));
            float y = (float) (mCenterY + (mMaxRadius + fontHeight / 2) * Math.sin(curAngle));

            //角度是从中心圆点顺时针旋转，依次是2 3 4 1象限
            if (curAngle >= 0 && curAngle < pi / 2) {                   //第二象限
                canvas.drawText(mTitles[i], x, y + fontHeight / 2, mTextPaint);
            } else if (curAngle >= pi / 2 && curAngle < pi) {           //第三象限
                canvas.drawText(mTitles[i], x - dis, y + fontHeight / 2, mTextPaint);
            } else if (curAngle >= pi && curAngle < pi * 3 / 2) {       //第四象限
                canvas.drawText(mTitles[i], x - dis, y, mTextPaint);
            } else if (curAngle >= pi * 3 / 2 && curAngle < 2 * pi) {   //第一象限
                canvas.drawText(mTitles[i], x, y, mTextPaint);
            } else {
                canvas.drawText(mTitles[i], x, y, mTextPaint);
            }
        }
    }

    /**
     * 绘制区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mPolygionCount; i++) {
            float percent = mDatas[i] / mMaxData;
            float curAngle = mAngle * i;//当前角度
            float x = (float) (mCenterX + mMaxRadius * percent * Math.cos(curAngle));
            float y = (float) (mCenterY + mMaxRadius * percent * Math.sin(curAngle));

            if (i == 0) {
                path.moveTo(x, mCenterY);
            } else {
                path.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 8, mRegionPaint);
        }
        canvas.drawPath(path, mRegionPaint);
    }


}
