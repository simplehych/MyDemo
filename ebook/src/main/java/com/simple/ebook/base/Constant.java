package com.simple.ebook.base;


import com.simple.ebook.BuildConfig;

/**
 * Created by Liang_Lu on 2017/11/22.
 */

public class Constant {
    public static String BASE_URL;

    static {
        if (BuildConfig.DEBUG) {
            BASE_URL = "http://www.luliangdev.cn";
        } else {
            BASE_URL = "http://www.luliangdev.cn";
        }
    }

    //Book Date Convert Format
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
}
