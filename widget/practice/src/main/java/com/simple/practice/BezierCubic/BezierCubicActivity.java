package com.simple.practice.BezierCubic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.simple.practice.R;

/**
 * Created by hych on 2017/5/5 12:38
 */

public class BezierCubicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazier_cubic);
        final BezierCubicView bezierCubicView = (BezierCubicView) findViewById(R.id.bezier_cubic_view);
        findViewById(R.id.control_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezierCubicView.selectControlPoint(0);
            }
        });
        findViewById(R.id.control_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezierCubicView.selectControlPoint(1);
            }
        });


    }
}
