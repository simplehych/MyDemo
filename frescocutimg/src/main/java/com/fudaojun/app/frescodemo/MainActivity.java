package com.fudaojun.app.frescodemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private SimpleDraweeView mTmpImageView;
    private SimpleDraweeView mNewImageView;
    private int mScreenWidth;
    private int mScreenHeight;

    public static Drawable mTmpDrawable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels; //768
        mScreenHeight = displayMetrics.heightPixels; //1184

        mTmpImageView = (SimpleDraweeView) findViewById(R.id.sdvTmp);
        mNewImageView = (SimpleDraweeView) findViewById(R.id.sdvNew);

//        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, com.facebook.imagepipeline.image.ImageInfo imageInfo, Animatable animatable) {
//                int viewWidth = imageInfo.getWidth();
//                int viewHeight = imageInfo.getHeight();
//                mImageView.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
//            }
//        };
//
//        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(600, 400));
//
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xxx);
//
//        Bitmap zoomBitmap = zoomImg(bitmap, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels / 6);
//        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), zoomBitmap, null, null));
//
//        new FrescoBuilder(mImageView, uri)
//                .cutPic(0f, 0.2f, 1f, 0.8f)
//                .setControllerListener(controllerListener)
//                .build();
//
//        Drawable drawable = mImageView.getDrawable();


        new myAsynTask().execute();

    }

    class myAsynTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;
            Bitmap bitmap = null;
            try {
                url = new URL("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
//                url = new URL("http://rmrbimg2.people.cn/original/data/ydyl//userfiles/1/images/cms/mobile/2017/05/1493878071091.jpg__.webp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                try {
                    Bitmap zoomImg = zoomImg(bitmap, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().widthPixels / 6);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), zoomImg, null, null));
                    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, com.facebook.imagepipeline.image.ImageInfo imageInfo, Animatable animatable) {
                            mTmpDrawable = mTmpImageView.getDrawable();



                            startActivity(new Intent(MainActivity.this, testActivity.class));
                        }
                    };
                    new FrescoBuilder(mTmpImageView, uri)
                            .cutPic(0f, 0.2f, 1f, 0.8f)
                            .setControllerListener(controllerListener)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap returnBitmap(Uri uri) {
        Bitmap bitmap = null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()));
        File file = resource.getFile();
        bitmap = BitmapFactory.decodeFile(file.getPath());
        return bitmap;

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    /**
     * 旋转图片
     *
     * @param rotate
     */
    private void rotate(SimpleDraweeView img, int rotate) {
        RotationOptions rotationOptions = RotationOptions.forceRotation(rotate);
        ImageRequest build = ImageRequestBuilder.newBuilderWithSource(getUriForFresco(this, R.mipmap.test_img))
                .setRotationOptions(rotationOptions)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(build)
                .build();
        mTmpImageView.setController(controller);
    }

    /**
     * 获得图片宽高
     *
     * @param controllerListener
     */
    private void getImageInfo(ControllerListener<? super ImageInfo> controllerListener) {
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setUri(getUriForFresco(this, R.mipmap.test_img))
                .build();
        mTmpImageView.setController(controller);
    }

    /**
     * 裁剪图片
     *
     * @param processor
     */
    private void cutPic(BasePostprocessor processor) {
        ImageRequest build = ImageRequestBuilder.newBuilderWithSource(getUriForFresco(this, R.mipmap.test_img))
                .setPostprocessor(processor)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(build)
                .build();

        mTmpImageView.setController(controller);
    }

    /**
     * 旋转+裁剪图片
     *
     * @param processor
     */
    private void rotateAndcutPic(BasePostprocessor processor, int rotate) {
        RotationOptions rotationOptions = RotationOptions.forceRotation(rotate);

        ImageRequest build = ImageRequestBuilder.newBuilderWithSource(getUriForFresco(this, R.mipmap.test_img))
                .setPostprocessor(processor)
                .setRotationOptions(rotationOptions)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(build)
                .build();

        mTmpImageView.setController(controller);
    }

    /**
     * 获得freso使用的本地图片的uri
     *
     * @param context
     * @param resourseId
     * @return
     */
    public static Uri getUriForFresco(Context context, int resourseId) {
        String packageName = getPackageName(context);
        if (packageName != null) {
            Uri parse = Uri.parse("res://" + packageName + "/" + resourseId);
            return parse;
        } else {
            return null;
        }
    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
