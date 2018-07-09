package com.simple.designpattern.mediator.base;

/**
 * @author hych
 * @date 2018/6/26 09:33
 */
public abstract class Mediator {

    /**
     * @param person
     * @param message
     */
    public abstract void contact(Person person, String message);
}
