package com.simple.memoryleak;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Handler造成的内存泄漏
 * <p>
 * mHandler若是Handler的非静态匿名内部类的实例，那么它将持有外部类的引用，我们知道
 * 消息队列是在一个Looper线程中不断轮询处理消息，那么当这个外部类退出时，消息队列中海油未处理的消息或者正在处理消息，
 * 而消息队列中的Message持有mHandler实例的引用，mHandler又持有外部类的引用，所以会导致外部类的内存资源无法及时回收引发内存泄漏。
 *
 * 解决方案：Handler使用静态内部类，然后对Handler持有外部类的弱引用，这样再回收时也可以回收Handler持有的对象，这样
 * 虽然避免了内存泄漏，不过Looper线程的消息队列中可能会有待处理的消息，所以我们的Destroy时应该移除消息队列
 * <p>
 * Created by hych on 2017/4/13.
 */

public class MemoryLeakCase3 extends AppCompatActivity {

    private MyHandler mHandler = new MyHandler(this);
    private TextView mCase3Tv;

    private static class MyHandler extends Handler {
        private WeakReference<Context> weakReference;

        public MyHandler(Context context) {
            weakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MemoryLeakCase3 case3 = (MemoryLeakCase3) weakReference.get();
            if (case3 != null){
                case3.mCase3Tv.setText("11");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_3);
        mCase3Tv = (TextView) findViewById(R.id.case3_tv);
        loadData();
    }

    private void loadData() {
        Message message = Message.obtain();
        mHandler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
