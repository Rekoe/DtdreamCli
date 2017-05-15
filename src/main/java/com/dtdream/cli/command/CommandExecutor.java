package com.dtdream.cli.command;

/**
 * Created by thomugo on 2016/8/29.
 */
public class CommandExecutor {
    public CommandExecutor(ICommandFactory factory, String [] stepName) {
        CommandRecord.getInstance().cleanAllCommands();
        CommandRecord.getInstance().setNextCommand(factory.getCommand(stepName));
    }

    public void execute() {
        while(CommandRecord.getInstance().hasNext()) {
            CommandRecord.getInstance().getNext().execute();
        }

    }
}
