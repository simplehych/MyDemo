package com.simple.designpattern.singleton;

/**
 * 饿汉式
 * 该方式在声明静态对象时就已经初始化了，如果没有使用单例对象的情况下，就会造成不必要的内存开销
 * <p>
 * Created by hych on 2018/6/20 09:38.
 */
public class HungrySingleton {

    private static HungrySingleton instance = new HungrySingleton();

    /**
     * 私有化构造器
     */
    private HungrySingleton() {
    }

    /**
     * 公有静态方法，对外暴露获取单例对象
     *
     * @return
     */
    public static HungrySingleton getInstance() {
        return instance;
    }

}
