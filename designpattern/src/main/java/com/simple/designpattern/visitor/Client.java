package com.simple.designpattern.visitor;

import com.simple.designpattern.visitor.staff.Developer;
import com.simple.designpattern.visitor.staff.Operator;
import com.simple.designpattern.visitor.visitor.CEOVisitor;
import com.simple.designpattern.visitor.visitor.CTOVisitor;

/**
 * Created by hych on 2018/6/26 08:38.
 */
public class Client {

    public static void main(String[] args) {
        Company company = new Company();

        company.addStaff(new Developer("Bruce"));
        company.addStaff(new Developer("Clark"));
        company.addStaff(new Developer("Barry"));

        company.addStaff(new Operator("Diana"));
        company.addStaff(new Operator("Oliver"));
        company.addStaff(new Operator("Dinah"));

        CEOVisitor ceoVisitor = new CEOVisitor();
        company.action(ceoVisitor);

        CTOVisitor ctoVisitor = new CTOVisitor();
        company.action(ctoVisitor);
    }
}
