package com.simple.designpattern.singleton;

import java.io.ObjectStreamException;

/**
 * 双检索模式
 * <p>
 * DCL方式实现单例模式的优点在于既能在需要时才初始化对象，又能保证线程安全，而且在对象初始化之后调用getInstance()不进行同步
 * 可以看到getInstance()方法中对instance进行了俩次判空，第一次是为了判断不必要的同步，第二次判断是为了在null的情况下创建实例；
 * 同时instance对象前面还添加了volatile关键字，如果不适用volatile关键字的话无法保证instance的原子性
 * <p>
 * 该模式能够在绝大多数场景下保证单例对象的唯一性，资源利用率高，只有第一次加载时反应慢，一般都能满足需求
 * <p>
 * Created by hych on 2018/6/20 09:58.
 */
public class DoubleCheckLock {

    private volatile static DoubleCheckLock instance = null;

    private DoubleCheckLock() {
    }

    public static DoubleCheckLock getInstance() {
        if (null == instance) {
            synchronized (DoubleCheckLock.class) {
                if (null == instance) {
                    instance = new DoubleCheckLock();
                }
            }
        }
        return instance;
    }

    /**
     * 避免反序列化重新生成对象
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }
}
