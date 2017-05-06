package com.simple.practice.MagicCircle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.simple.practice.R;

/**
 * Created by hych on 2017/5/6 14:15
 */

public class MagicCircleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_circle);
        final MagicCircle magicCircle = (MagicCircle) findViewById(R.id.magic_circle_view);
        findViewById(R.id.magic_circle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicCircle.startAnimation();
            }
        });


    }
}
