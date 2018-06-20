package com.simple.designpattern.builder;

/**
 * 构建模式
 * 通常作为配置类的构建器，将配置的构建和表示分离，同时也是将配置从使用中隔离出来，避免过多的setter方法。
 * 使用链式调用实现，达到通俗易懂的目的
 *
 * 优点：
 * 良好的封装性，使用者不知道内部的实现细节
 * 容易扩展，由于Builder的独立存在扩展不会影响原有逻辑
 * 缺点：
 * 会产生多余的Builder对象，额能还有Director对象，占用内存
 * Created by hych on 2018/6/20 10:58.
 */
public class Build {
}
