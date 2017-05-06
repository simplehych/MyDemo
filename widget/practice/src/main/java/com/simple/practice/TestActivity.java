package com.simple.practice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hych on 2017/5/5 13:18
 */

public class TestActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = (ImageView) findViewById(R.id.test_iv);

        new myAsynTask().execute();


    }

    Bitmap getImageFromNetwork() {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png");
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
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


    class myAsynTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return getImageFromNetwork();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }

        }
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
                System.out.println("_________保存到____sd______指定目录文件夹下____________________");
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

}

