package com.simple.practice.BezierCircleToHeart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hych on 2017/5/6 11:46
 */

public class BezierCircleToHeartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new BezierCircleToHeart(this));
    }
}
