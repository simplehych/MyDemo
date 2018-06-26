package com.simple.designpattern.mediator.base;

import com.simple.designpattern.mediator.base.Mediator;

/**
 * @author hych
 * @date 2018/6/26 09:34
 */
public abstract class Person {
    protected String name;
    protected Mediator mediator;

    public Person(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }
}

