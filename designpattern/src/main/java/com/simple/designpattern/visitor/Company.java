package com.simple.designpattern.visitor;

import com.simple.designpattern.visitor.staff.Staff;
import com.simple.designpattern.visitor.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hych on 2018/6/26 08:36.
 */
public class Company {

    private List<Staff> staffList = new ArrayList<>();

    public void action(Visitor visitor) {
        for (Staff staff : staffList) {
            staff.accept(visitor);
        }
    }

    public void addStaff(Staff staff) {
        staffList.add(staff);
    }
}
