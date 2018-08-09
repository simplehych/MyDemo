package com.simple.review.make;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.simple.review.R;

import org.litepal.tablemanager.Connector;

/**
 * @author hych
 * @date 2018/8/3 11:09
 */
public class MakeActivity extends AppCompatActivity {

    private String toastTip = "toast in MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        MakeFragment makeFragment = new MakeFragment();
        fragmentTransaction.add(R.id.fg_make_container, makeFragment);

        findViewById(R.id.fg_make_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodWithGlobalVariable();
                methodWithLocalVariable();
                Utils utils = new Utils();
                utils.methodNormal();
                NativeUtils.methodNative();
                NativeUtils.methodNotNative();
                Connector.getDatabase();
            }
        });
    }

    public void methodWithGlobalVariable() {
        Toast.makeText(this, toastTip, Toast.LENGTH_SHORT).show();
    }

    public void methodWithLocalVariable() {
        String logMessage = "log in MakeActivity methodWithLocalVariable";
        logMessage = logMessage.toLowerCase();
        System.out.println(logMessage);
    }
}
