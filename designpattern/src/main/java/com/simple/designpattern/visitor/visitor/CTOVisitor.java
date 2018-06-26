package com.simple.designpattern.visitor.visitor;

import com.simple.designpattern.visitor.staff.Developer;
import com.simple.designpattern.visitor.staff.Operator;

/**
 * Created by hych on 2018/6/26 08:30.
 */
public class CTOVisitor implements Visitor {

    @Override
    public void visit(Developer develop) {
        System.out.println("CTOVisitor Developer " + develop.name);
    }

    @Override
    public void visit(Operator operator) {
        System.out.println("CTOVisitor Operator " + operator.name);
    }
}
