package com.simple.designpattern.mediator;

import com.simple.designpattern.mediator.base.Mediator;
import com.simple.designpattern.mediator.base.Person;

/**
 * @author hych
 * @date 2018/6/26 09:37
 */
public class HouseOwner extends Person {

    public HouseOwner(String name, Mediator mediator) {
        super(name, mediator);
    }

    public void constact(String message) {
        mediator.contact(this, message);
    }

    public void getMessage(String message) {
        System.out.println("HouseOwner: " + name + " ,获取信息: " + message);
    }
}
