package com.simple.practice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.simple.practice.BezierCircleToHeart.BezierCircleToHeartActivity;
import com.simple.practice.BezierCubic.BezierCubicActivity;
import com.simple.practice.BezierQuad.BezierQuadActivity;
import com.simple.practice.pieview.PieViewActivity;
import com.simple.practice.radarview.RadarViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.PieView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieViewActivity.class));
            }
        });
        findViewById(R.id.RadarView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RadarViewActivity.class));
            }
        });

        findViewById(R.id.BezierQuadView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BezierQuadActivity.class));
            }
        });

        findViewById(R.id.BezierCubicView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BezierCubicActivity.class));
            }
        });
        findViewById(R.id.BezierCircleToHeart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BezierCircleToHeartActivity.class));
            }
        });
    }
}
