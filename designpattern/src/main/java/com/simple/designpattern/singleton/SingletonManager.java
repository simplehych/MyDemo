package com.simple.designpattern.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用容器实现单例模式
 * <p>
 * Created by hych on 2018/6/20 10:28.
 */
public class SingletonManager {

    private static Map<String, Object> objectMap = new HashMap<>();

    private SingletonManager() {
    }

    public static void registerService(String key, Object instance) {
        if (!objectMap.containsKey(key)) {
            objectMap.put(key, instance);
        }
    }

    public static Object getService(String key) {
        return objectMap.get(key);
    }
}
