package com.simple.designpattern.command;

/**
 * Created by hych on 2018/6/21 08:48.
 */
public class Client {

    public static void main(String[] args) {
        Receiver receiver = new Receiver();
        CleanMemoryCommand cleanMemoryCommand = new CleanMemoryCommand(receiver);
        Invoker invoker = new Invoker(cleanMemoryCommand);
        invoker.action();
    }
}
