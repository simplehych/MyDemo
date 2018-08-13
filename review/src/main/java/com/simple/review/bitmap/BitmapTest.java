package com.simple.review.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.widget.ImageView;

import com.simple.review.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 为了避免OOM异常。最好在解析每张图片的时候先检查一下图片的大小
 * 除非你非常信任图片的来源，保证这些图片都不会超出你程序的可用内存
 * 知道图片的大小后，我们就可以决定是将整张图片加载到内存中还是加载一个压缩版的图片到内存中，
 * 以下几个因素是需要我们考虑的：
 * 1. 预估一下加载整张图片所需占用的内存
 * 2. 为了加载这张图片你所愿意提供多少内存
 * 3. 用于展示这张图片的控件的实际大小
 * 4. 当前设备的屏幕尺寸和分辨率
 * <p>
 * 为了能够选择一个合适的缓存大小给LruCache，有以下多个因素应该返图考虑范围内，例如：
 * 1. 你的设备可以为每个应用程序分配多大的内存？
 * 2. 设备屏幕上一次最多能显示多少张图片？有多少图片需要进行预加载，因为有可能很快也会显示在屏幕上？
 * 3. 你的设备的屏幕大小和分辨率是多少？一个超高分辨率的设备比起一个超低分辨率的设备，在持有相同图片
 * 的时候，需要更大的缓存空间
 * 4. 图片的尺寸和大小，还有每张图片会占据多少内存空间
 * 5. 图片被访问的频率有多高？会不会有一些图片的访问频率比其他图片要高？如果有的话，你也许应该让一些图
 * 片常驻在内存当中，或者使用多个LruCache对象来区分不同组的图片
 * 6. 你能维持好数量和质量之间的平衡吗？有些时候，存储多个低像素的图片，而在后台去开线程加载高像素的图片会更加有效
 *
 * @author hych
 * @date 2018/8/13 08:13
 */
public class BitmapTest extends AppCompatActivity {
    private static final String TAG = "BitmapTest ";
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        System.out.println(TAG + maxMemory);
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int size = value.getByteCount() / 1024;
                return size;
            }
        };

        testStream();
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(
                decodeSampleBitmapFromResource(getResources(), R.mipmap.ic_launcher, 100, 100));
    }

    private void loadBitmap(int resId, ImageView imageView) {
        String imageKey = String.valueOf(resId);
        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(resId);
        }
    }

    private void addBitmapMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            Bitmap bitmap = decodeSampleBitmapFromResource(getResources(), integers[0], 100, 100);
            addBitmapMemoryCache(String.valueOf(integers[0]), bitmap);
            return bitmap;
        }
    }

    private Bitmap decodeSampleBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private void testStream() {
        new Thread() {
            @Override
            public void run() {
                String path = "http://img17.3lian.com/d/file/201701/16/2903447217bdc2a804e2d37487627032.jpg";
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = connection.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        BitmapFactory.decodeStream(inputStream, null, options);
                        int outWidth = options.outWidth;
                        int outHeight = options.outHeight;
                        String outMimeType = options.outMimeType;
                        int inSampleSize = options.inSampleSize;
                        System.out.println(TAG + " outWidth: " + outWidth
                                + " outHeight: " + outHeight + " outMimeType: " + outMimeType
                                + " inSampleSize: " + inSampleSize);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
