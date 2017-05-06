package com.fudaojun.app.frescodemo;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zyz on 16/12/3.
 */
public class BaseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //fresco初始化
        Fresco.initialize(this);
    }
}
