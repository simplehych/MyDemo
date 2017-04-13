package com.simple.memoryleak;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * 线程造成的内存泄漏
 * <p>
 * 异步任务和Runnable都是一个匿名内部类，因此它们对当前Activity都有一个隐式引用，如果Activity在销毁之前任务还没有完成，
 * 那么将导致Activity的内存资源无法回收，造成内存泄漏
 * <p>
 * 解决方案：使用静态内部类，避免Activity的内存资源泄漏，当然在Activity销毁的时候也应该取消相应的任务AsyncTask cancel(),避免任务在后台执行浪费资源
 * <p>
 * Created by hych on 2017/4/13.
 */

public class MemoryLeakCase4 extends AppCompatActivity {

    private AsyncTask<Void, Void, Void> task;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new MyRunnable()).start();
        task = new MyAsyncTask(this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    static class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<Context> weakReference;

        public MyAsyncTask(Context context) {
            weakReference = new WeakReference<Context>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(10000);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            MemoryLeakCase4 case4 = (MemoryLeakCase4) weakReference.get();
            if (case4 != null) {
                // TODO
            }
        }
    }

    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            SystemClock.sleep(10000);
        }
    }

}
