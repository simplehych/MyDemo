package com.simple.designpattern.mediator;

import com.simple.designpattern.mediator.base.Mediator;
import com.simple.designpattern.mediator.base.Person;

/**
 * @author hych
 * @date 2018/6/26 09:44
 */
public class MediatorStructure extends Mediator {

    private HouseOwner houseOwner;
    private Tenant tenant;

    @Override
    public void contact(Person person, String message) {
        if (person == houseOwner) {
            tenant.getMessage(message);
        } else {
            houseOwner.getMessage(message);
        }
    }

    public HouseOwner getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(HouseOwner houseOwner) {
        this.houseOwner = houseOwner;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
