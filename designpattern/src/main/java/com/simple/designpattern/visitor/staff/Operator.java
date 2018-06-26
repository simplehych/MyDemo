package com.simple.designpattern.visitor.staff;

import com.simple.designpattern.visitor.staff.Staff;
import com.simple.designpattern.visitor.visitor.Visitor;

import java.util.Random;

/**
 * Created by hych on 2018/6/26 08:24.
 */
public class Operator extends Staff {

    public Operator(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getNewUserNum() {
        return new Random().nextInt(10 * 10000);
    }
}
