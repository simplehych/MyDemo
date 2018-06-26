package com.simple.designpattern.visitor.staff;

import com.simple.designpattern.visitor.visitor.Visitor;

import java.util.Random;

/**
 * Created by hych on 2018/6/26 08:22.
 */
public class Developer extends Staff {

    public Developer(String name) {
        super(name);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public int getCodeLines() {
        return new Random().nextInt(10 * 1000);
    }
}
