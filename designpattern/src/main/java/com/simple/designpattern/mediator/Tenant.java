package com.simple.designpattern.mediator;

import com.simple.designpattern.mediator.base.Mediator;
import com.simple.designpattern.mediator.base.Person;

/**
 * @author hych
 * @date 2018/6/26 09:41
 */
public class Tenant extends Person {
    public Tenant(String name, Mediator mediator) {
        super(name, mediator);
    }

    public void constact(String message) {
        mediator.contact(this, message);
    }

    public void getMessage(String message) {
        System.out.println("Tenant: " + name + " ,获得信息: " + message);
    }
}
