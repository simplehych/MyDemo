package com.simple.matrixbitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通过Matrix确定Bitmap
 * mImageView.setScaleType(ImageView.ScaleType.MATRIX);
 * <p>
 * 旋转，平移，缩放，错切等
 * <p>
 * 本例子中，从网络中加载图片显示，然后将图片缩放或者扩大到屏幕的尺寸，然后上移
 * <p>
 * 可以选择保存到本地缓存，并在本地获取
 */
public class MainActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private Bitmap mNewBitmap;
    private Bitmap mNewNewBitmap;
    private ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //从图片中获取Bitmap此方法，测试无效，可能姿势不对
//        ImageView imageView = new ImageView(this);
//        imageView.setDrawingCacheEnabled(true);
//        final Bitmap bitmap = imageView.getDrawingCache();
//        imageView.setDrawingCacheEnabled(false);

        //从图片中可以直接获取Drawable，然后转成Bitmap，但是转的方法有问题，可能不适用在fresco的SimpleDrawView中
//        Drawable drawable = imageView.getDrawable();
//        Bitmap bitmap1 = drawableToBitmap(drawable);

        //另：通过Intent进行Bitmap的传递可以，但是Drawable不行，可以通过设置全局静态变量来获取，详情请看frescocutimg Demo
//        Intent intent = new Intent(MainActivity.this, RadarViewActivity.class);
//        intent.putExtra("bitmap", bitmap);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("bitmap", bitmap);
//        intent.putExtra("bundle", bundle);
//        startActivity(intent);

        init();
    }

    class myAsynTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromNetwork();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                int screenW = getResources().getDisplayMetrics().widthPixels;
                Matrix matrix = new Matrix();
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float scale = ((float) screenW) / ((float) width);

                matrix.setScale(scale, scale);

                Log.e("Simple", "screenW -------- " + screenW);
                Log.e("Simple", "width -------- " + width);
                Log.e("Simple", "height -------- " + height);
                Log.e("Simple", "scale -------- " + scale);
                Log.e("Simple", "screenW / width -------- " + (float) screenW / width);

                mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                mImageView.setImageBitmap(mNewBitmap);
                testTranslate1(mNewBitmap);
            }

        }
    }

    /**
     * 从网络下载Bitmap
     *
     * @return
     */
    public Bitmap getBitmapFromNetwork() {
        try {
            URL url = new URL("http://rmrbimg2.people.cn/original/data/ydyl//userfiles/1/images/cms/mobile/2017/05/1493878071091.jpg__.webp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存Bitmap到本地
     *
     * @param bitmap
     */
    public void saveBitmap(Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {     // 判断是否可以对SDcard进行操作
            // 获取SDCard指定目录下
            String sdCardDir = Environment.getExternalStorageDirectory() + "/CoolImage/";
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            //文件夹有啦，就可以保存图片啦
            File file = new File(sdCardDir, System.currentTimeMillis() + ".png");// 在SDcard的目录下创建图片文,以当前时间为其命名
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从本地文件读取Bitmap
     *
     * @param path
     * @return
     */
    public Bitmap getBitmap(String path) {
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setScaleType(ImageView.ScaleType.MATRIX);

//        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
//        mBitmap = bitmapDrawable.getBitmap();

        new myAsynTask().execute();


//        Matrix matrix = new Matrix();
//        int width = mBitmap.getWidth();
//        int height = mBitmap.getHeight();
//        matrix.setScale(1.5f, 1.5f);
//        mNewBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
//        mImageView.setImageBitmap(mNewBitmap);

//        Matrix matrixNew = mImageView.getImageMatrix();
//        int widthNew = mNewBitmap.getWidth();
//        int heightNew = mNewBitmap.getHeight();
//        matrix.postTranslate(width, -height / 2);
//        mNewNewBitmap = Bitmap.createBitmap(mNewBitmap, 0, 0, widthNew, heightNew, matrixNew, false);
//        mImageView.setImageBitmap(mNewNewBitmap);


//        Matrix matrix1 = new Matrix();
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        matrix.postTranslate(width, -height / 2);
//        mImageView.setImageMatrix(matrix);

        //平移的两种方式,效果一致
//        testTranslate1(mNewBitmap);
//        testTranslate2(mBitmap);

        //围绕图片中心点旋转且位移的两种方式,效果一致
        //testRotate1(mBitmap);
        //testRotate2(mBitmap);

        //围绕原点旋转后平移的两种方式,效果一致
//        testRotateAndTranslate1(mBitmap);
        //testRotateAndTranslate2(mBitmap);

        //测试缩放的两种方式,效果一致
//        testScale1();
        //testScale2(mBitmap);

        //测试倾斜各两种方式,效果一致
        //testSkewX1();
        //testSkewX2(mBitmap);
        //testSkewY1();
        //testSkewY2(mBitmap);
        //testSkewXY1();
        //testSkewXY2(mBitmap);

        //测试对称
        //testSymmetryX(mBitmap);
        //testSymmetryY(mBitmap);
        //testSymmetryXY(mBitmap);
    }


    //平移的方式一
    private void testTranslate1(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.postTranslate(0, -height / 4f);
        mImageView.setImageMatrix(matrix);
    }


    //平移的方式二
    private void testTranslate2(Bitmap bitmap) {
        Matrix matrix = mImageView.getImageMatrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.postTranslate(width, height);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }


    //围绕图片中心点旋转且位移的方式一
    private void testRotate1(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.postRotate(45f, width / 2, height / 2);
        matrix.postTranslate(width, height);
        mImageView.setImageMatrix(matrix);
    }

    //围绕图片中心点旋转且位移的方式二
    //注意问题:
    //在方式一种旋转45°采用matrix.postRotate(45f, width/2, height/2);即可
    //但在方式二中只需旋转22.5度matrix.postRotate(45/2f, width/2, height/2);
    private void testRotate2(Bitmap bitmap) {
        Matrix matrix = mImageView.getImageMatrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.postRotate(45 / 2f, width / 2, height / 2);
        matrix.postTranslate(width, height);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }


    //围绕原点旋转后平移的方式一
    private void testRotateAndTranslate1(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setRotate(45f);
        matrix.postTranslate(width, height);
        mImageView.setImageMatrix(matrix);
    }


    //围绕原点旋转后平移的方式二
    //注意问题:
    //同上
    private void testRotateAndTranslate2(Bitmap bitmap) {
        Matrix matrix = mImageView.getImageMatrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setRotate(45 / 2f);
        matrix.postTranslate(width, height);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }


    //缩放的方式一
    private void testScale1() {
        Matrix matrix = new Matrix();
        matrix.setScale(1.5f, 1.5f);
        mImageView.setImageMatrix(matrix);
    }

    //缩放的方式二
    private void testScale2(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setScale(0.5f, 0.5f);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }

    //水平倾斜的方式一
    private void testSkewX1() {
        Matrix matrix = new Matrix();
        matrix.setSkew(0.5f, 0);
        mImageView.setImageMatrix(matrix);
    }

    //水平倾斜的方式二
    private void testSkewX2(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setSkew(0.5f, 0);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }

    // 垂直倾斜的方式一
    private void testSkewY1() {
        Matrix matrix = new Matrix();
        matrix.setSkew(0, 0.5f);
        mImageView.setImageMatrix(matrix);
    }

    // 垂直倾斜的方式二
    private void testSkewY2(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setSkew(0, 0.5f);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }

    // 水平且垂直倾斜的方式一
    private void testSkewXY1() {
        Matrix matrix = new Matrix();
        matrix.setSkew(0.5f, 0.5f);
        mImageView.setImageMatrix(matrix);
    }

    // 水平且垂直倾斜的方式二
    private void testSkewXY2(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        matrix.setSkew(0.5f, 0.5f);
        mNewBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        mImageView.setImageBitmap(mNewBitmap);
    }


    // 水平对称--图片关于X轴对称
    private void testSymmetryX(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int height = bitmap.getHeight();
        float matrixValues[] = {1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrixValues);
        //若是matrix.postTranslate(0, height);//表示将图片上下倒置
        matrix.postTranslate(0, height * 2);
        mImageView.setImageMatrix(matrix);
    }


    // 垂直对称--图片关于Y轴对
    private void testSymmetryY(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        float matrixValues[] = {-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrixValues);
        //若是matrix.postTranslate(width,0);//表示将图片左右倒置
        matrix.postTranslate(width * 2, 0);
        mImageView.setImageMatrix(matrix);
    }


    // 关于X=Y对称
    private void testSymmetryXY(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float matrixValues[] = {0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f, 1f};
        matrix.setValues(matrixValues);
        matrix.postTranslate(width + height, width + height);
        mImageView.setImageMatrix(matrix);
    }
}
