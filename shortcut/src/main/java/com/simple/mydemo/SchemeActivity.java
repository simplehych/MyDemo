package com.simple.mydemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.simple.mydemo.R;

/**
 * Created by hych on 2017/4/28 13:20.
 */

public class SchemeActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_scheme);
        final String data = getIntent().getStringExtra("携带参数");

        final Button btn = (Button) findViewById(R.id.i_am_a_scheme_activity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText(data);
            }
        });

    }
}
