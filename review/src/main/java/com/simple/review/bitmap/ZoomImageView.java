package com.simple.review.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author hych
 * @date 2018/8/13 14:59
 */
public class ZoomImageView extends View {

    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;

    /**
     * 用于对图片进行移动和缩放变换的矩阵
     */
    private Matrix matrix = new Matrix();

    /**
     * 待展示的Bitmap对象
     */
    private Bitmap sourceBitmap;

    /**
     * 记录当前操作的状态
     */
    private int currentStatus;

    /**
     * ZoomImageView控件的宽度
     */
    private int width;

    /**
     * ZoomImageView控件的高度
     */
    private int height;

    /**
     * 记录俩指同时放在屏幕上时，中心点的横坐标
     */
    private float centerPointX;

    /**
     * 记录俩指同时放在屏幕上时，中心点的纵坐标
     */
    private float centerPointY;

    /**
     * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapWidth;

    /**
     * 记录当前图片的高度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapHeight;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    private float moveDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    private float moveDistanceY;

    private float totalTranslateX;
    private float totalTranslateY;
    private float totalRatio;
    private float scaledRatio;
    private float initRatio;
    private double lastFingerDis;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentStatus = STATUS_INIT;
    }

    private void setImageBitmap(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (2 == event.getPointerCount()) {
                    //当有俩个手指按在屏幕上时，计算俩指之间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (1 == event.getPointerCount()) {
                    //只有单指按在屏幕上移动时，为拖动状态
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (lastXMove == -1 && lastYMove == -1) {
                        lastXMove = xMove;
                        lastYMove = yMove;
                    }
                    currentStatus = STATUS_MOVE;
                    moveDistanceX = xMove - lastXMove;
                    moveDistanceY = yMove - lastYMove;
                    //进行边界检查，不允许将图片拖出边界
                    if (totalTranslateX + moveDistanceX > 0) {
                        moveDistanceX = 0;
                    } else if (width - (totalTranslateX + moveDistanceX) > currentBitmapWidth) {
                        moveDistanceX = 0;
                    }

                    if (totalTranslateY + moveDistanceY > 0) {
                        moveDistanceY = 0;
                    } else if (height - (totalTranslateY + moveDistanceY) > currentBitmapHeight) {
                        moveDistanceY = 0;
                    }

                    invalidate();
                    lastXMove = xMove;
                    lastYMove = yMove;

                } else if (2 == event.getPointerCount()) {
                    //当有俩根手指按在屏幕上移动时，为缩放状态
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (2 == event.getPointerCount()) {
                    lastYMove = -1;
                    lastXMove = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                /*
                 * 手指离开屏幕时将临时值还原
                 */
                lastXMove = -1;
                lastYMove = -1;
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private double distanceBetweenFingers(MotionEvent event) {
        return 0;
    }
}
