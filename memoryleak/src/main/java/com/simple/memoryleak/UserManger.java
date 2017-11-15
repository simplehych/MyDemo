package com.simple.memoryleak;

import android.content.Context;

/**
 * Created by hych on 2017/11/13.
 */

public class UserManger {

    private static UserManger instance;

    private Context context;

    private UserManger(Context context) {
        this.context = context;
    }

    public static UserManger getInstance(Context context) {
        if (instance == null) {
            instance = new UserManger(context);
        }
        return instance;
    }
}
