package com.simple.ebook.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.simple.ebook.base.BaseApplication;

/**
 * Created by Liang_Lu on 2017/11/28.
 * 尺寸工具箱，提供与Android尺寸相关的工具方法
 */

public class DimenUtils {
    /**
     * dp单位转换为px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5f);
    }

    /**
     * px单位转换为dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    private static float getDensity() {
        Resources resources = BaseApplication.getContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float density = displayMetrics.density;
        return density;
    }
}
