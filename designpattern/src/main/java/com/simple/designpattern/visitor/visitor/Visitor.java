package com.simple.designpattern.visitor.visitor;

import com.simple.designpattern.visitor.staff.Developer;
import com.simple.designpattern.visitor.staff.Operator;

/**
 * Created by hych on 2018/6/26 08:22.
 */
public interface Visitor {

    /**
     * 访问开发人员
     *
     * @param develop
     */
    public void visit(Developer develop);

    /**
     * 访问运营人员
     *
     * @param operator
     */
    public void visit(Operator operator);
}
