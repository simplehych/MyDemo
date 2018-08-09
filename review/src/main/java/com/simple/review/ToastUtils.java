package com.simple.review;

import android.content.Context;
import android.widget.Toast;

/**
 * @author hych
 * @date 2018/8/2 11:09
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showShort(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
