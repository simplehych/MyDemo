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

import com.simple.practice.BezierCubic.BezierCubicActivity;
import com.simple.practice.BezierQuad.BezierQuadActivity;
import com.simple.practice.pieview.PieViewActivity;
import com.simple.practice.radarview.RadarViewActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv = (ImageView) findViewById(R.id.test_iv);
//        iv.setDrawingCacheEnabled(true);
//        final Bitmap bitmap = iv.getDrawingCache();
//        iv.setDrawingCacheEnabled(false);

        Drawable drawable = iv.getDrawable();

        final Bitmap bitmap = drawableToBitmap(drawable);

        findViewById(R.id.PieView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PieViewActivity.class));
            }
        });
        findViewById(R.id.RadarView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RadarViewActivity.class);
                intent.putExtra("bitmap", bitmap);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("bitmap", bitmap);
//                intent.putExtra("bundle",bundle);
                startActivity(intent);
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
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
