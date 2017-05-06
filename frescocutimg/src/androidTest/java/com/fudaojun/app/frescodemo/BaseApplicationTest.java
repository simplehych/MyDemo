package com.fudaojun.app.frescodemo;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BaseApplicationTest extends ApplicationTestCase<Application> {
    public BaseApplicationTest() {
        super(Application.class);
    }
}