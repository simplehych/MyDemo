package com.simple.practice.radarview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.simple.practice.R;

/**
 * Created by hych on 2017/5/4 17:29
 */

public class RadarViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView2 = (ImageView) findViewById(R.id.test_iv2);

//        Bundle bundle = getIntent().getBundleExtra("bundle");
//        Bitmap bitmap = bundle.getParcelable("bitmap");


        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        imageView2.setImageBitmap(bitmap);

//        setContentView(new RadarView(this));
    }
}
