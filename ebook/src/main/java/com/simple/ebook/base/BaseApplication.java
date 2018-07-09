package com.simple.ebook.base;

import android.app.Application;
import android.content.Context;

/**
 * @author hych
 * @date 2018/7/9 14:26
 */
public class BaseApplication extends Application {
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Context getContext() {
        return app;
    }
}
