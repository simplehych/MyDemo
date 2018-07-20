package com.simple.ebook.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Liang_Lu on 2017/9/7.
 */

public class ToastUtils {
    private static Toast toast;

    public static void show(Context context, @StringRes int resId) {
        show(context, context.getResources().getString(resId));
    }

    public static void show(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
