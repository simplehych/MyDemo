package com.simple.multiimageview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * 可以显示多张图片的View
 * <p>
 * Created by hych on 2017/5/2 15:54
 */

public class MultiImageView extends LinearLayout {

    private static int MAX_WIDTH = 0;//本View的最大宽度
    private List<String> mImgUrlList;//需要显示图片的地址集合，可以为1-N张

    private int mImgCountPerRow = 3;//默认每行显示3张图片,四张图片显示俩行

    private int mOneImgMaxWH;  // 单张图最大允许宽高，正方形
    private int mMultiImgsMaxWH = 0;// 多张图的宽高，正方形

    private LayoutParams mOneImgParams;//单张图大小参数
    private LayoutParams mMultiImgsFirstColParams;//多张图在首列的图片参数，没有margin
    private LayoutParams mMultiImgsParams;//多张图非首列的图片参数，带margin
    private LayoutParams mRowImgsParams;//每行图片的参数，本View继承自LinearLayout,一行一行添加
    private int mImgPadding = dip2px(getContext(), 5);// 图片间的间距

    private OnMultiImageViewItemClickListener mOnMultiItemClickListener;
    private int mImagePlaceholder = R.mipmap.ic_launcher;//默认图

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {//第一次是0，只执行一次，比如列表文件，第二个条目就不用不走里面的代码，提高效率

            int width = measureWidth(widthMeasureSpec);//测量获取最大宽度

            if (width > 0) {
                MAX_WIDTH = width;
                if (mImgUrlList != null && mImgUrlList.size() > 0) {
                    setImageUrlList(mImgUrlList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 测量宽度，可方法结果实际都为specSize = MeasureSpec.getSize(measureSpec);因为获取最大宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {//布局文件使用match_parent,即为specSize
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {//布局文件使用wrap_content,即为specSize,其实这里都为specSize，但为了学习加深印象还是展开处理
                result = Math.max(result, specSize);
            }
        }
        return result;
    }

    public void setImageUrlList(List<String> imgUrlList) {
        if (imgUrlList == null) {
            throw new IllegalArgumentException("imageUrlList is null...");
        }

        this.mImgUrlList = imgUrlList;

        if (MAX_WIDTH > 0) {
            mMultiImgsMaxWH = (MAX_WIDTH - mImgPadding * (mImgCountPerRow - 1)) / mImgCountPerRow; //解决右侧图片和内容对不齐问题
            mOneImgMaxWH = MAX_WIDTH;//单张图可以显示最大宽度
            initImageLayoutParams();
        }

        addImageView();
    }

    /**
     * 初始化相关的参数
     */
    private void initImageLayoutParams() {
        int matchParent = LayoutParams.MATCH_PARENT;
        int wrapContent = LayoutParams.WRAP_CONTENT;

        mOneImgParams = new LayoutParams(wrapContent, wrapContent);//一张图的大小参数

        mMultiImgsFirstColParams = new LayoutParams(mMultiImgsMaxWH, mMultiImgsMaxWH);//多张图首列的大小参数
        mMultiImgsParams = new LayoutParams(mMultiImgsMaxWH, mMultiImgsMaxWH);//多张图非首列的大小参数
        mMultiImgsParams.setMargins(mImgPadding, 0, 0, 0);

        mRowImgsParams = new LayoutParams(matchParent, wrapContent);
    }

    /**
     * 添加图片，包括一张或者N张，根据数量添加不同的布局，并且为每个ImageView添加点击事件
     * 一张图，四张图，九张图的样式
     */
    private void addImageView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();

        if (MAX_WIDTH == 0) {//该可能极小，因为之前已经测量过，为了以防万一，测试添加View,触发onMeasure()重新测量该View的最大宽度
            addView(new View(getContext()));
            return;
        }

        if (mImgUrlList == null || mImgUrlList.size() == 0) {
            return;
        }

        if (mImgUrlList != null && mImgUrlList.size() == 1) {//如果图片为一张
            addView(creatImageView(0, false));

        } else if (mImgUrlList != null && mImgUrlList.size() > 1) {//如果图片大于一张
            int allCount = mImgUrlList.size();
            if (allCount == 4) {
                mImgCountPerRow = 2;//四张图片每行显示俩张
            } else {
                mImgCountPerRow = 3;//其他每行显示三张
            }

            int rowCount = allCount / mImgCountPerRow
                    + (allCount % mImgCountPerRow > 0 ? 1 : 0);// 一共有多少行

            for (int rowCursor = 0; rowCursor < rowCount; rowCount++) {
                LinearLayout rowLinearLayout = new LinearLayout(getContext());
                rowLinearLayout.setOrientation(HORIZONTAL);
                rowLinearLayout.setLayoutParams(mRowImgsParams);

                if (rowCursor != 0) {
                    rowLinearLayout.setPadding(0, mImgPadding, 0, 0);
                }

                int colCountPerRow = (allCount % mImgCountPerRow == 0) ? mImgCountPerRow : allCount % mImgCountPerRow;//每行的列数
                if (rowCursor != rowCount - 1) {
                    colCountPerRow = mImgCountPerRow;
                }

                int rowOffset = rowCursor * mImgCountPerRow;//行偏移
                for (int colCursor = 0; colCursor < colCountPerRow; colCursor++) {
                    int postion = rowOffset + colCursor;
                    rowLinearLayout.addView(creatImageView(postion, true));
                }

                addView(rowLinearLayout);
            }
        }

    }

    /**
     * 创建ImageView，可以设置单张或者多张的大小，用glide加载
     *
     * @param position
     * @param isMultiImgs
     * @return
     */
    private ImageView creatImageView(int position, boolean isMultiImgs) {
        String imgUrl = mImgUrlList.get(position);
        ImageView imageView = new ImageView(getContext());

        if (isMultiImgs) {//多张图的参数大小
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams((position % mImgCountPerRow) == 0 ? mMultiImgsFirstColParams : mMultiImgsParams);//根据位置判断是否是首列，是否有margin

        } else {//一张图的参数大小
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            //预期设置一张图的大小，可以修改为从调用的地方传进来，在这里暂时写死
            int expectW = dip2px(getContext(), 50);
            int expectH = dip2px(getContext(), 50);

            if (expectW == 0 || expectH == 0) {
                imageView.setLayoutParams(mOneImgParams);

            } else {
                //实际的宽高
                int actuleW = 0;
                int actuleH = 0;

                int scale = actuleH / actuleW;

                if (expectW > mOneImgMaxWH) {
                    actuleW = mOneImgMaxWH;
                    actuleH = actuleW * scale;

                } else if (expectW < mMultiImgsMaxWH) {
                    actuleW = mMultiImgsMaxWH;
                    actuleH = actuleW * scale;

                } else {
                    actuleW = expectW;
                    actuleH = expectH;
                }
                imageView.setLayoutParams(new LayoutParams(actuleW, actuleH));
            }

            imageView.setId(imgUrl.hashCode());
            imageView.setOnClickListener(new OnMultiImageViewClickListener(position));
            imageView.setBackgroundColor(Color.parseColor("#dddddd"));

            Glide.with(getContext())
                    .load(imgUrl)
                    .placeholder(mImagePlaceholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
        return imageView;
    }


    private void SetOnMultiImageViewItemClickListener(OnMultiImageViewItemClickListener listener) {
        this.mOnMultiItemClickListener = listener;
    }

    private class OnMultiImageViewClickListener implements OnClickListener {
        private int position;

        public OnMultiImageViewClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (mOnMultiItemClickListener == null) {
                mOnMultiItemClickListener.onItemClick(v, position);
            }
        }
    }


    public interface OnMultiImageViewItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 屏幕适配，dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

}
