package com.fudaojun.app.frescodemo;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by zyz on 16/12/1.
 *
 * 封装的fresco处理图片工具
 */
public class FrescoBuilder {
    private SimpleDraweeView mSdv;
    private Uri mUri;
    private ImageRequestBuilder mImageRequestBuilder;
    private ControllerListener<? super ImageInfo> mControllerListener;

    public FrescoBuilder(SimpleDraweeView sdv, Uri uri) { //必须的成员
        mSdv = sdv;
        mUri = uri;
    }

    private ImageRequestBuilder getImageRequestBuilder() {
        if (mImageRequestBuilder == null) {
            mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(mUri);
        }
        return mImageRequestBuilder;
    }

    /**
     * 设置图片下载监听器
     *
     * @param controllerListener
     */
    public FrescoBuilder setControllerListener(ControllerListener<? super ImageInfo> controllerListener) {
        mControllerListener = controllerListener;
        return this;
    }

    /**
     * 旋转
     *
     * @param rotate
     * @return
     */
    public FrescoBuilder setRotate(int rotate) {
        RotationOptions rotationOptions = RotationOptions.forceRotation(rotate);
        getImageRequestBuilder().setRotationOptions(rotationOptions);
        return this;
    }

    /**
     * 切割图片
     *
     * @param beginX 起点的横向的百分比值
     * @param beginY 起点的纵向的百分比值
     * @param width  横向切割的百分比值
     * @param height 纵向切割的百分比值
     * @return
     */
    public FrescoBuilder cutPic(float beginX, float beginY, float width, float height) {
        getImageRequestBuilder().setPostprocessor(new CutProcess(beginX, beginY, width, height));
        return this;
    }

    public void build() {
        PipelineDraweeControllerBuilder builder =
                Fresco.newDraweeControllerBuilder();
        if (mControllerListener != null) {
            builder = builder.setControllerListener(mControllerListener).setUri(mUri);
        } else {
            builder = builder.setOldController(mSdv.getController());
        }
        if (mImageRequestBuilder != null) {
            ImageRequest build = mImageRequestBuilder.build();
            builder = builder.setImageRequest(build);
        }
        PipelineDraweeController controller = (PipelineDraweeController) builder.build();
        mSdv.setController(controller);
    }
}
