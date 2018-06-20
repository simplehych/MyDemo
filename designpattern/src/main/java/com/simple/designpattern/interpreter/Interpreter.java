package com.simple.designpattern.interpreter;

import java.util.Stack;

/**
 * 解释器模式
 *
 * Created by hych on 2018/6/20 14:56.
 */
public class Interpreter {
    protected Stack<Interpreter> mArithmeticExpressionStack = new Stack<>();

    public void test(){
        mArithmeticExpressionStack.pop().test();
    }
}
