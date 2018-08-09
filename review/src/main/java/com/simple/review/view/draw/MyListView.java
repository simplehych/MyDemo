package com.simple.review.view.draw;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * @author hych
 * @date 2018/8/1 13:39
 */
public class MyListView extends ListView
        implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private GestureDetector gestureDetector;
    private OnDeleteListener deleteListener;
    private View deleteButton;
    private ViewGroup itemLayout;
    private int selectedItem;
    private boolean isDeleteShown;

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDeleteShown) {
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShown = false;
        } else {
            return gestureDetector.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShown) {
            selectedItem = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            deleteButton = new Button(getContext());
            ((Button) deleteButton).setText("删除");
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    deleteListener.onDelete(selectedItem);
                }
            });

            itemLayout = (ViewGroup) getChildAt(selectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public void setOnDeleteListener(OnDeleteListener listener){
        deleteListener = listener;
    }

    public interface OnDeleteListener {
        public void onDelete(int position);
    }
}
