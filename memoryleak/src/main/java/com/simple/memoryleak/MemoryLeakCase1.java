package com.simple.memoryleak;

import android.content.Context;

/**
 * 单例造成的内存泄漏
 *
 * 当调用getInstance的时候，如果传入的context是Activity的context
 * 只要这个单例没有被释放，那么这个Activity也不会被释放一直到进程退出才会释放
 *
 * 解决方案：使用Application的Context，Application的生命周期伴随着整个进程的周期
 *
 * <p>
 * Created by hych on 2017/4/13.
 */

public class MemoryLeakCase1 {

    private static MemoryLeakCase1 sInstance;
    private Context mContext;

    private MemoryLeakCase1(Context context){
        this.mContext = context;
    }

    public static MemoryLeakCase1 getInstance(Context context){
        if (sInstance == null){
            sInstance = new MemoryLeakCase1(context);
        }
        return sInstance;
    }

}
