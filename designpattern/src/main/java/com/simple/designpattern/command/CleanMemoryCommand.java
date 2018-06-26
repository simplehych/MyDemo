package com.simple.designpattern.command;

/**
 * Created by hych on 2018/6/21 08:45.
 */
public class CleanMemoryCommand implements Command {

    Receiver receiver;

    public CleanMemoryCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.action();
    }
}
