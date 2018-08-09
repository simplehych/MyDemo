package com.simple.review.distribute;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author hych
 * @date 2018/7/30 08:51
 */
public class MyLayout extends LinearLayout {

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
//        return false;
//        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
