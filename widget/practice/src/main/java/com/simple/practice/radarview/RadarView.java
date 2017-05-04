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
 * 雷达蛛网图
 * Created by hych on 2017/5/4 17:16
 */

public class RadarView extends View {

    private int mPolygionCount = 6;                                 //多边形个数，几边形
    private float mAngle = (float) (Math.PI * 2 / mPolygionCount);  //正多边形的每个内角度数


    private float mMaxRadius;   //最大半径
    private int mCenterX;       //中心点X坐标
    private int mCenterY;       //中心点Y坐标

    private Paint mRadarPaint;  //雷达画笔

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRadarPaint = new Paint();
        mRadarPaint.setColor(Color.GRAY);
        mRadarPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mMaxRadius = Math.min(w, h) / 2 * 0.9f;
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
    }

    /**
     * 绘制正多边形
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
                    float x = (float) (mCenterX + curR * Math.cos(mAngle * j));
                    float y = (float) (mCenterY + curR * Math.sin(mAngle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();//闭合路径，注意是闭合，并非收尾相连，如果收尾相连不是闭合，那么什么都不做
            canvas.drawPath(path, mRadarPaint);
        }
    }


}
