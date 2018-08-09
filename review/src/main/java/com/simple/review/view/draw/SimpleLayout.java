package com.simple.review.view.draw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author hych
 * @date 2018/7/31 15:04
 */
public class SimpleLayout extends ViewGroup {

    private static final String TAG = "SimpleLayout";
    private int mMeasureTime = 0;
    private int mLayoutTime = 0;

    public SimpleLayout(Context context) {
        this(context, null);
    }

    public SimpleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMeasureTime = 0;
        mLayoutTime = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View firstChild = getChildAt(0);
            measureChild(firstChild, widthMeasureSpec, heightMeasureSpec);

            Log.i(TAG, TAG + " onMeasure " + " time:" + mMeasureTime + " " + firstChild.getWidth() + " " + firstChild.getHeight()
                    + " " + firstChild.getMeasuredWidth() + " " + firstChild.getMeasuredHeight());
        }
        mMeasureTime++;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            View firstChild = getChildAt(0);
//            firstChild.layout(0, 0, firstChild.getMeasuredWidth(), firstChild.getMeasuredHeight());
            firstChild.layout(20, 20, 50, 80);
            Log.i(TAG, TAG + " onLayout " + " time:" + mLayoutTime + " " + firstChild.getWidth() + " " + firstChild.getHeight()
                    + " " + firstChild.getMeasuredWidth() + " " + firstChild.getMeasuredHeight());
        }
        mLayoutTime++;
        mLayoutTime++;
    }
}
