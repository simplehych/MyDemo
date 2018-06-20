package com.simple.designpattern.singleton;

/**
 * 静态内部类
 *
 * 在某些情况下DCL模式会出现时效的问题，于是就有了静态内部类的实现方式
 *
 * 当第一次加载StaticInnerSingleton类时并不会初始化instance，只有在第一次调用getInstance()方法才初始化。
 * 第一次调用getInstance()方法导致虚拟机加载SingletonHolder类，这种方式能确保线程安全，也能确保单例对象的唯一性，
 * 同时也延迟了单例对象的实例化
 * <p>
 * Created by hych on 2018/6/20 10:18.
 */
public class StaticInnerSingleton {
    private StaticInnerSingleton() {
    }

    public static StaticInnerSingleton getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        public static final StaticInnerSingleton instance = new StaticInnerSingleton();
    }
}
