package com.fudaojun.app.frescodemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by hych on 2017/5/5 15:58
 */

public class testActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SimpleDraweeView tmp = (com.facebook.drawee.view.SimpleDraweeView) findViewById(R.id.sdvNew);
        tmp.setImageDrawable(MainActivity.mTmpDrawable);
    }
}
