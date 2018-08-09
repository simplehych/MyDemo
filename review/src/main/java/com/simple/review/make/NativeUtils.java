package com.simple.review.make;

/**
 * @author hych
 * @date 2018/8/3 11:05
 */
public class NativeUtils {

    public static native void methodNative();

    public static void methodNotNative() {
        String logMessage = "this is NativeUtils not native method";
        logMessage = logMessage.toLowerCase();
        System.out.println(logMessage);
    }
}
