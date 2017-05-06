package com.fudaojun.app.frescodemo;

import android.graphics.Bitmap;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.request.BasePostprocessor;

/**
 * Created by zyz on 16/12/03.
 * 
 * fresco切割图片
 */
public class CutProcess extends BasePostprocessor {
    private float mBeginXPercent;
    private float mBeginYPercent;
    private float mCutWidthPercent;
    private float mCutHeightPercent;

    public CutProcess(float beginXPercent, float beginYPercent, float cutWidthPercent, float cutHeightPercent) {
        this.mBeginXPercent = beginXPercent;
        this.mBeginYPercent = beginYPercent;
        this.mCutWidthPercent = cutWidthPercent;
        this.mCutHeightPercent = cutHeightPercent;
    }

    @Override
    public CloseableReference<Bitmap> process(
            Bitmap sourceBitmap,
            PlatformBitmapFactory bitmapFactory) {
        int viewWidth = sourceBitmap.getWidth();
        int viewHeight = sourceBitmap.getHeight();
        int beginx = (int) (mBeginXPercent * viewWidth);
        int beginy = (int) (mBeginYPercent * viewHeight);
        int width = (int) (mCutWidthPercent * viewWidth);
        int height = (int) (mCutHeightPercent * viewHeight);
        CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(sourceBitmap, beginx, beginy, width, height);
        return CloseableReference.cloneOrNull(bitmapRef);
    }
}
