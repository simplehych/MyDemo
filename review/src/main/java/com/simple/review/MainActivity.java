package com.simple.review;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.simple.review.animator.ValueAnimatorTest;
import com.simple.review.distribute.DistributeActivity;
import com.simple.review.immerse.ImmerseActivity;
import com.simple.review.scroller.ScrollerActivity;
import com.simple.review.view.draw.MyActivity;
import com.simple.review.view.inflate.InflateActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private int clickCount;
    private View mTestBtn;
    private View stubView;
    private View button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.test_image_view);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        findViewById(R.id.act_main_scroller).setOnClickListener(this);
        findViewById(R.id.act_main_event_distribute).setOnClickListener(this);
        findViewById(R.id.act_main_event_inflate).setOnClickListener(this);
        findViewById(R.id.act_main_event_draw).setOnClickListener(this);
        mTestBtn = findViewById(R.id.test_btn);

        String title = "() 、 我们。。";
        String newTitle = title.replaceAll("[^\u4E00-\u9FA5]", "");
        Log.i("Simple", "new：" + newTitle);
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int memoryClass = activityManager.getMemoryClass();
        System.out.println("memoryClass: " + memoryClass);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        System.out.println("maxMemory: " + maxMemory + "kb");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.get("key");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putString("key", "last words before be kill");
        outState.putAll(bundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_main_scroller:
                testBitmap();
//                startActivity(ImmerseActivity.class);
//                startActivity(ScrollerActivity.class);
                break;
            case R.id.act_main_event_distribute:
                startActivity(DistributeActivity.class);
                break;
            case R.id.act_main_event_inflate:
                startActivity(InflateActivity.class);
                break;
            case R.id.act_main_event_draw:
                startActivity(MyActivity.class);
                break;
            case R.id.button:
                if (stubView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.viewStub2);
                    if (viewStub != null) {
                        stubView = viewStub.inflate();
                        break;
                    }
                }
                if (stubView.getVisibility() == View.VISIBLE) {
                    stubView.setVisibility(View.GONE);
                } else {
                    stubView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    private void startActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void click_test_btn(View view) {
        ToastUtils.showShort(this, clickCount + " test --");
        clickCount++;

        Snackbar snackbar = Snackbar.make(mTestBtn, "test", Snackbar.LENGTH_SHORT)
                .setAction("UN DO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        snackbar.show();
    }

    public void click_test_notify(View view) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder
                .setContentTitle("这是通知标题")
                .setContentText("这是通知内容")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }

    public void click_test_density(View view) {
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        Log.i(TAG, TAG + " xdpi: " + xdpi + " ydpi: " + ydpi);
        System.gc();

        testWarmSwap1();
    }

    public void testWarmSwap() {
        ObjectAnimator trans = ObjectAnimator.ofFloat(button, "translationX", 0, 200);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(button, "rotation", 0f, 360f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(trans).after(rotate);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animatorSet.start();
    }

    public void testWarmSwap1() {
        ValueAnimator animator = (ValueAnimator) AnimatorInflater.loadAnimator(this, R.animator.object_anim);
        animator.setTarget(button);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                System.out.println("onAnimationUpdate  " + animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {
                System.out.println("AnimatorListener  onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                System.out.println("AnimatorListener  onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                System.out.println("AnimatorListener  onAnimationRepeat");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                System.out.println("AnimatorListener  onAnimationStart");
            }
        });
        button.getAlpha();
        animator.start();
    }

    public void testWarmSwap2() {
        AnimationDrawable frameAnim = new AnimationDrawable();
        frameAnim.addFrame(getResources().getDrawable(R.drawable.ic_launcher_background), 50);
        frameAnim.addFrame(getResources().getDrawable(R.drawable.ic_launcher_background), 50);
        frameAnim.addFrame(getResources().getDrawable(R.drawable.ic_launcher_background), 50);
        //是否重复
        frameAnim.setOneShot(false);
        //播放开始
        frameAnim.start();
        //播放结束
        frameAnim.start();

    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_UI_HIDDEN:
                //用户离开了我们的程序，进行释放资源的操作
                break;
            default:
                break;
        }
    }

    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024;
            File httpCacheDir = new File(getCacheDir(), "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            httpResponseCacheNotAvailable.printStackTrace();
        }
    }

    private void testBitmap() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String path = "http://www.baidu.com/img/bd_logo1.png";
                Bitmap imageFromNet = getImageFromNet(path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);
                InputStream inputStream = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    System.out.println("testBitmap responseCode: " + responseCode);
                    inputStream = conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                 * 获取去图片信息之前不能使用非option方法，否则过去不出来
                 */
//                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


                Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream, null, options);
//                byte[] data = inputStream2ByteArr(inputStream);//将InputStream转为byte数组，可以多次读取
//                BitmapFactory.decodeByteArray(data, 0, data.length, options);
                int outWidth = options.outWidth;
                int outHeight = options.outHeight;
                String outMimeType = options.outMimeType;

                System.out.println("testBitmap outWidth: " + outWidth + " outHeight: " + outHeight + " outMimeType: " + outMimeType);

            }
        }.start();
    }

    private Bitmap getImageFromNet(String btp) {
        HttpURLConnection conn = null;
        try {
            URL myUri = new URL(btp); // 创建URL对象
            // 创建链接
            conn = (HttpURLConnection) myUri.openConnection();
            conn.setConnectTimeout(10000);// 设置链接超时
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");// 设置请求方法为get
            conn.connect();// 开始连接
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                // 根据流数据创建 一个Bitmap位图对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                int outWidth = options.outWidth;
                int outHeight = options.outHeight;
                String outMimeType = options.outMimeType;

                System.out.println("testBitmap outWidth: " + outWidth
                        + " outHeight: " + outHeight
                        + " outMimeType: " + outMimeType
                );

                return bitmap;
                // 访问成功
            } else {
                Log.i(TAG, "访问失败：responseCode=" + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();

            }
        }
        return null;

    }

    private byte[] inputStream2ByteArr(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
