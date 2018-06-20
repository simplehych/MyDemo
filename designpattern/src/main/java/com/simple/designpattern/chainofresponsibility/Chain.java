package com.simple.designpattern.chainofresponsibility;

/**
 * 责任链模式
 * 行为型设计模式之一，什么是责任链呢？这个链的形式更像是数据结构中的单链表，链中的每个节点都有自己的职责，
 * 同时也持有下一个节点的引用，属于自己职责范围内的请求就自行处理，并完成请求的处理，而不属于的职责就传递给下一个节点。
 * 每个节点都是如此循环，直至请求被处理或者已经没有处理节点
 *
 * Created by hych on 2018/6/20 14:46.
 */
public class Chain {
}
