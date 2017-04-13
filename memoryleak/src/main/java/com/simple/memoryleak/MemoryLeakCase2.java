package com.simple.memoryleak;

/**
 * 非静态内部类创建静态实例造成的内存泄漏
 * <p>
 * 解决方案：将非静态内部类修改为静态内部类，静态内部类不会隐式持有外部类
 * <p>
 * Created by hych on 2017/4/13.
 */

public class MemoryLeakCase2 {

    private static TestCase2 mTestCase2 = null;

    public void case2Method() {
        if (mTestCase2 == null) {
            mTestCase2 = new TestCase2();
        }
    }

    class TestCase2 {

    }
}
