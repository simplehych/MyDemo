package com.simple.review.view.inflate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simple.review.R;

/**
 * @author hych
 * @date 2018/7/30 10:36
 */
public class InflateActivity extends AppCompatActivity {

    private final String TAG = "InflateActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inflate);
        LayoutInflater layoutInflater1 = LayoutInflater.from(this);
        LayoutInflater layoutInflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutInflater1.inflate(R.layout.activity_inflate, null);

        LinearLayout wholeLayout = (LinearLayout) findViewById(R.id.act_inflate_whole_layout);
        ViewParent parent = wholeLayout.getParent();
        Log.i(TAG, String.format(TAG + " %1$s", parent));
    }
}
