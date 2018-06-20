package com.simple.designpattern.singleton;

/**
 * 懒汉式
 * <p>
 * 该方式是声明一个静态对象，在第一次调用getInstance()时进行初始化
 * <p>
 * 与饿汉式不同的地方不仅仅是单例对象初始化的时机，会发现在getInstance()方法前添加了synchronize关键字，
 * 也就是getInstance()是一个同步方法，以此来保证多线程情况下单例对象的唯一
 * <p>
 * 相对的，每次调用getInstance()都会进行同步，就会消耗不必要的资源，也是该方式存在的最大问题
 * <p>
 * Created by hych on 2018/6/20 09:44.
 */
public class LazySingleton {

    private static LazySingleton instance = null;

    private LazySingleton() {
    }

    public static synchronized LazySingleton getInstance() {
        if (null == instance) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
