package com.simple.practice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.simple.practice.BezierCircleToHeart.BezierCircleToHeartActivity;
import com.simple.practice.BezierCubic.BezierCubicActivity;
import com.simple.practice.BezierQuad.BezierQuadActivity;
import com.simple.practice.MagicCircle.MagicCircleActivity;
import com.simple.practice.PieView.PieViewActivity;
import com.simple.practice.RadarView.RadarViewActivity;
import com.simple.practice.RegionClickView.RegionClickViewActivity;
import com.simple.practice.SearchView.SearchViewActivity;

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

        findViewById(R.id.MagicCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MagicCircleActivity.class));
            }
        });

        findViewById(R.id.SearchView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchViewActivity.class));
            }
        });

        findViewById(R.id.RegionClickView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegionClickViewActivity.class));
            }
        });
    }
}
