package com.simple.practice.RegionClickView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by hych on 2017/5/9 14:29
 */

public class RegionClickView extends View {

    private Paint mDeafultPaint;
    private Region mCircleRegion;
    private Path mCirclePath;

    public RegionClickView(Context context) {
        this(context, null);
    }

    public RegionClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDeafultPaint = new Paint();
        mDeafultPaint.setColor(0xFF4e5268);

        mCircleRegion = new Region();
        mCirclePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCirclePath.addCircle(w / 2, h / 2, 300, Path.Direction.CW);
        Region globalRegion = new Region(-w, -h, w, h);
        mCircleRegion.setPath(mCirclePath, globalRegion);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path circle = mCirclePath;
        canvas.drawPath(circle, mDeafultPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                if (mCircleRegion.contains(x, y)) {
                    Toast.makeText(getContext(), "圆被点击", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        return true;
    }
}
