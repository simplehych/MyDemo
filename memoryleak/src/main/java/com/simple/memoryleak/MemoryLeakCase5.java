package com.simple.memoryleak;

/**
 * Created by hych on 2017/4/13.
 */

public class MemoryLeakCase5 {


    /**
     * 1、资源未关闭造成的内存泄漏
     *
     * 对于使用了BraodcastReceiver，ContentObserver，File，Cursor，Stream，Bitmap等资源的使用，
     * 应该在Activity销毁时及时关闭或者注销，否则这些资源将不会被回收，造成内存泄漏
     *
     * 解决方案：在Activity销毁时及时关闭或者注销
     */

    /**
     * 2、使用了静态的Activity和View
     *
     * 解决方案：应该及时将静态的应用 置为null，而且一般不建议将View及Activity设置为静态
     */

    /**
     * 3、注册了系统的服务，但onDestory未注销
     *
     */

    /**
     *  4、不需要用的监听未移除会发生内存泄露
     *  tv.setOnClickListener();//监听执行完回收对象，不用考虑内存泄漏
     *  tv.getViewTreeObserver().addOnWindowFocusChangeListene,add监听，放到集合里面，需要考虑内存泄漏
     */
}
