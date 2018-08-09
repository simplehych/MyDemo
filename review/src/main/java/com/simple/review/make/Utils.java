package com.simple.review.make;

/**
 * @author hych
 * @date 2018/8/3 11:01
 */
public class Utils {

    public void methodNormal() {
        String logMessage = "this id Util normal method";
        logMessage = logMessage.toLowerCase();
        System.out.println(logMessage);
    }

    public void methodUnused() {
        String logMessage = "this is Util unused method";
        logMessage = logMessage.toLowerCase();
        System.out.println(logMessage);
    }
}
