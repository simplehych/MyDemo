package com.simple.designpattern.visitor.staff;

import com.simple.designpattern.visitor.visitor.Visitor;

import java.util.Random;

/**
 * Created by hych on 2018/6/26 08:20.
 */
public abstract class Staff {

    /** women */
    public String name;
    public int kpi;

    public Staff(String name){
        this.name = name;
        this.kpi = new Random().nextInt();
    }

    public abstract void accept(Visitor visitor);
}
