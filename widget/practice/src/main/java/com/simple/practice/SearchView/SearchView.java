package com.simple.practice.SearchView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 搜索动画
 * <p>
 * PathMeasure类
 * getPosTan():得到某段距离的Position位置点的坐标，和Tan的角度
 * getMatrix():用于得到路径上某一长度的位置以及该位置的正切值的矩阵
 * getSegment():得到Path的某个片段，第三个参数Boolean，起始点的位置是否为原位置，不和之前的位置点相连
 * nextContour():只能一条path一条path的测量，path的顺序即为set的顺序setPath()
 * isClosed():是否闭合
 * <p>
 * Created by hych on 2017/5/8 11:07
 */

public class SearchView extends View {

    private Paint mPaint;//画笔

    private int mViewWidth;//视图的宽
    private int mViewHeight;//视图的高

    private State mCurState = State.NONE;//当前视图的状态 - 非常重要

    private Path path_search;//放大镜
    private Path path_circle;//外部圆环

    private PathMeasure mPathMeasure;//测量Path并截取部分的工具

    private int mDefaultDuration = 2000;//默认的动效周期 2s

    /**
     * 控制各个动画过程的动画
     */
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    private float mAnimatorValue = 0;//动画数值 用于控制动画状态，因为同一时间至于许有一种状态出现，具体数值处理取决于当前状态

    //动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    private Handler mAnimatorHandler;//用于控制动画状态转换

    private boolean isOver = false;//判断是否已经搜索结束
    private int count = 0;

    /**
     * 该视图的过程状态
     */
    public enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAll();
    }

    private void initAll() {
        initPaint();
        initPath();
        initListener();
        initAnimator();
        initHandler();

        // 进入开始动画
        mCurState = State.STARTING;
        mStartingAnimator.start();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    /**
     * 初始化Path
     */
    private void initPath() {
        path_search = new Path();
        path_circle = new Path();

        mPathMeasure = new PathMeasure();

        RectF oval1 = new RectF(-50, -50, 50, 50);//放大镜搜索图标的圆环
        path_search.addArc(oval1, 45, 359.99f);//注意，不要到360度，否则内部会自动优化，测量不能取到需要的数值

        RectF oval2 = new RectF(-100, -100, 100, 100);//外部圆环
        path_circle.addArc(oval2, 45, -359.99f);

        float[] pos = new float[2];

        mPathMeasure.setPath(path_circle, false);//放大镜把手的位置
        mPathMeasure.getPosTan(0, pos, null);//http://www.gcssloop.com/customview/Path_PathMeasure 获取指定长度的位置坐标及该点切线值

        path_search.lineTo(pos[0], pos[1]);//放大镜把手

        Log.e("Simple", "pos = pos[0]:" + pos[0] + "  pos[1] " + pos[1]);
    }

    private void initListener() {

        mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                Log.e("Simple", "mAnimatorValue = " + mAnimatorValue);

                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorHandler.sendEmptyMessage(0);//getHandler发消息通知动画状态更新
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

    }

    private void initAnimator() {
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);
        mEndingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mDefaultDuration);

        mStartingAnimator.addUpdateListener(mAnimatorUpdateListener);
        mSearchingAnimator.addUpdateListener(mAnimatorUpdateListener);
        mEndingAnimator.addUpdateListener(mAnimatorUpdateListener);

        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }

    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurState) {
                    case STARTING:
                        isOver = false;//从开始动画转换好搜索动画
                        mCurState = State.SEARCHING;
                        mStartingAnimator.removeAllListeners();
                        mSearchingAnimator.start();
                        break;

                    case SEARCHING:
                        if (!isOver) {//如果搜索未结束，则继续执行搜索动画
                            mSearchingAnimator.start();
                            Log.e("Simple", "RESTART");

                            count++;
                            if (count > 2) {    //count大于2则进入结束状态
                                isOver = true;
                            }
                        } else {//如果搜索已经结束，则进入结束动画
                            mCurState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;

                    case ENDING:
                        mCurState = State.NONE;//从结束动画变为无状态
                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    private void drawSearch(Canvas canvas) {

        mPaint.setColor(Color.WHITE);

        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        canvas.drawColor(Color.parseColor("#0082D7"));

        switch (mCurState) {
            case NONE:
                canvas.drawPath(path_search, mPaint);
                break;

            case STARTING:
                mPathMeasure.setPath(path_search, false);
                Path dst1 = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst1, true);
                canvas.drawPath(dst1, mPaint);
                break;

            case SEARCHING:
                mPathMeasure.setPath(path_circle, false);
                Path dst2 = new Path();
                float stop = mPathMeasure.getLength() * mAnimatorValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
                mPathMeasure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;

            case ENDING:
                mPathMeasure.setPath(path_search, false);
                Path dst3 = new Path();
                mPathMeasure.getSegment(mPathMeasure.getLength() * mAnimatorValue, mPathMeasure.getLength(), dst3, true);
                canvas.drawPath(dst3, mPaint);
                break;
        }
    }
}
