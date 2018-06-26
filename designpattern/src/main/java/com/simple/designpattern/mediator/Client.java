package com.simple.designpattern.mediator;

/**
 * @author hych
 * @date 2018/6/26 09:50
 */
public class Client {

    public static void main(String[] args) {
        MediatorStructure mediatorStructure = new MediatorStructure();
        HouseOwner houseOwner = new HouseOwner("包租婆", mediatorStructure);
        Tenant tenant = new Tenant("酱爆", mediatorStructure);

        mediatorStructure.setHouseOwner(houseOwner);
        mediatorStructure.setTenant(tenant);

        tenant.constact("怎么停水了？");
        houseOwner.constact("打死你丫的！");
    }
}
