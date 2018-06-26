package com.simple.designpattern.command;

/**
 * 命令模式
 * 将一个请求封装成一个对象或者封装到一个对象中，从而使用户可用不同的请求把客户端参数化
 *
 *
 * Created by hych on 2018/6/20 15:27.
 */
public interface Command {
    public void execute();
}
