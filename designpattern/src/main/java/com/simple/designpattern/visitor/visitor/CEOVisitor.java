package com.simple.designpattern.visitor.visitor;

import com.simple.designpattern.visitor.staff.Developer;
import com.simple.designpattern.visitor.staff.Operator;

/**
 * Created by hych on 2018/6/26 08:32.
 */
public class CEOVisitor implements Visitor {

    @Override
    public void visit(Developer develop) {
        System.out.println("CEOVisitor Developer "+ develop.name);
    }

    @Override
    public void visit(Operator operator) {
        System.out.println("CEOVisitor Operator " + operator.name);
    }
}
