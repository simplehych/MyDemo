package com.simple.review.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.Toast;

import com.simple.review.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hych
 * @date 2018/8/13 11:18
 */
public class MyScrollView extends ScrollView implements View.OnTouchListener {

    /**
     * 图片地址
     */
    private String[] imageUlrs;

    /**
     * 每页要加载的图片数量
     */
    public static final int IMAGE_COUNT_OF_PAGE = 15;

    /**
     * 记录当前已加载到第几页
     */
    private int page;

    /**
     * 每一列的宽度
     */
    private int columWidth;

    /**
     * 第一列的高度
     */
    private int firstColumnHeight;

    /**
     * 第二列的高度
     */
    private int secondColumnHeight;

    /**
     * 第三列的高度
     */
    private int thirdColumnHeight;

    /**
     * 是否已经加载过一次layout，这里onLayout中的初始化只需要加载一次
     */
    private boolean loadOnce;

    /**
     * 对图片进行管理的工具类
     */
    private ImageLoader imageLoader;

    /**
     * 第一列的布局
     */
    private LinearLayout firstColumn;

    /**
     * 第二列的布局
     */
    private LinearLayout secondColumn;

    /**
     * 第三列的布局
     */
    private LinearLayout thirdColumn;

    /**
     * 记录所有正在下载或等待下载的任务
     */
    private static Set<LoadImageTask> taskCollection;

    /**
     * ScrollView下的直接子布局
     */
    private static View scrollLayout;

    /**
     * ScrollView布局的高度
     */
    private static int scrollViewHeight;

    /**
     * 记录垂直方向的滚动距离
     */
    private static int lastScrollY = -1;

    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放
     */
    private List<ImageView> imageViewList = new ArrayList<>();

    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作
     */
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyScrollView scrollView = (MyScrollView) msg.obj;
            int scrollY = scrollView.getScrollY();
            if (scrollY == lastScrollY) {
                if (scrollViewHeight + scrollY >= scrollLayout.getAlpha()
                        && taskCollection.isEmpty()) {
                    scrollView.loadMoreImages();
                }
                scrollView.checkVisibility();
            } else {
                lastScrollY = scrollY;
                Message message = Message.obtain();
                message.obj = scrollView;
                handler.sendMessageDelayed(message, 5);
            }
        }
    };

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = ImageLoader.getInstance();
        taskCollection = new HashSet<>();
        setOnTouchListener(this);
    }

    /**
     * 进行一些关键性的初始化操作，获取ScrollView的高度，以及得到第一列宽度值，并在这里加载第一页的图片
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout) findViewById(R.id.first_column);
            secondColumn = (LinearLayout) findViewById(R.id.second_column);
            thirdColumn = (LinearLayout) findViewById(R.id.third_column);
            columWidth = firstColumn.getWidth();
            loadOnce = true;
            loadMoreImages();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载
     */
    private void loadMoreImages() {
        if (hasSDCard()) {
            int startIndex = page * IMAGE_COUNT_OF_PAGE;
            int endIndex = page * IMAGE_COUNT_OF_PAGE + IMAGE_COUNT_OF_PAGE;
            if (startIndex < imageUlrs.length) {
                Toast.makeText(getContext(), "正在加载中...", Toast.LENGTH_SHORT).show();
                if (endIndex > imageUlrs.length) {
                    endIndex = imageUlrs.length;
                }
                for (int i = startIndex; i < endIndex; i++) {
                    LoadImageTask task = new LoadImageTask();
                    taskCollection.add(task);
                    task.execute(imageUlrs[i]);
                }
                page++;
            } else {
                Toast.makeText(getContext(), "已经没有更多图片", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkVisibility() {
        int size = imageViewList.size();
        for (int i = 0; i < size; i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (int) imageView.getTag(R.string.border_top);
            int borderBottom = (int) imageView.getTag(R.string.border_bottom);
            if (borderBottom > getScrollY()
                    && borderTop < getScrollY() + scrollViewHeight) {
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LoadImageTask task = new LoadImageTask(imageView);
                    task.execute(imageUrl);
                }

            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }

    private boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String mImageUrl;

        /**
         * 可重复使用的ImageView
         */
        private ImageView mImageView;

        public LoadImageTask() {
        }

        public LoadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            mImageUrl = strings[0];
            Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
            if (bitmap == null) {
                bitmap = loadImage(mImageUrl);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (columWidth * 1.0);
                int scaleHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columWidth, scaleHeight);
            }
            taskCollection.remove(this);
        }

        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);
            }
            if (imageUrl != null) {
                Bitmap bitmap = ImageLoader.decodeSampleBitmapFromResource(
                        imageFile.getPath(), columWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);
                findColumnToAdd(imageView, imageHeight).addView(imageView);
                imageViewList.add(imageView);
            }
        }

        private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                if (firstColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, firstColumnHeight);
                    firstColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, firstColumnHeight);
                    return firstColumn;
                } else {
                    imageView.setTag(R.string.border_top, thirdColumnHeight);
                    thirdColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                    return thirdColumn;
                }
            } else {
                if (secondColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, secondColumnHeight);
                    return secondColumn;
                } else {
                    imageView.setTag(R.string.border_top, thirdColumnHeight);
                    thirdColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                    return thirdColumn;
                }
            }
        }

        private void downloadImage(String imageUrl) {
            HttpURLConnection conn = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try {
                URL url = new URL(imageUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5 * 1000);
                conn.setReadTimeout(5 * 1000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                if (200 == conn.getResponseCode()) {
                    InputStream inputStream = conn.getInputStream();
                    bis = new BufferedInputStream(inputStream);
                    imageFile = new File(getImagePath(imageUrl));

                    fos = new FileOutputStream(imageFile);
                    bos = new BufferedOutputStream(fos);
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = bis.read(b)) != -1) {
                        bos.write(b, 0, len);
                        bos.flush();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (imageFile != null) {
                Bitmap bitmap = ImageLoader.decodeSampleBitmapFromResource(
                        imageFile.getPath(), columWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }

        }

        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageName = imageUrl.substring(lastSlashIndex + 1);
            String imageDir = Environment.getExternalStorageDirectory().getPath()
                    + "/PhotoWallFalls/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;
            return imagePath;
        }
    }
}
