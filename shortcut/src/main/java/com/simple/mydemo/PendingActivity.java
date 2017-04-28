package com.simple.mydemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by hych on 2017/4/28 13:20.
 */

public class PendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        final String data = getIntent().getStringExtra("携带参数");

        final Button btn = (Button) findViewById(R.id.i_am_a_pending_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(data);
            }
        });
    }

}
