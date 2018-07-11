package com.simple.app;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;

import java.util.Set;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        sharedPreferences.getAll();

        getSharedPreferences("name", MODE_PRIVATE);

        System.out.println("qqq");


        float pivotX = 10f;
        float pivotY = 10f;
        float angle = 30f;
        // 第一段 pre  顺序执行，先平移(T)后旋转(R)
        Matrix matrix1 = new Matrix();
        matrix1.preTranslate(pivotX, pivotY);
        matrix1.preRotate(angle);
        Log.e("Simple", matrix1.toShortString());

// 第二段 post 逆序执行，先平移(T)后旋转(R)
        Matrix matrix2 = new Matrix();
        matrix2.postRotate(angle);
        matrix2.postTranslate(pivotX, pivotY);
        Log.e("Simple", matrix2.toShortString());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = 960 / 2;
        options.outHeight = 960 / 2;
    }

}
