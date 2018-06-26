package com.simple.designpattern.command;

/**
 * Created by hych on 2018/6/21 08:47.
 */
public class Invoker {

    Command command;

    public Invoker(Command command) {
        this.command = command;
    }

    public void action(){
        command.execute();
    }
}
