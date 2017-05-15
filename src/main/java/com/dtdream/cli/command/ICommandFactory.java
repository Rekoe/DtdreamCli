package com.dtdream.cli.command;

/**
 * Created by thomugo on 2016/8/29.
 */
public interface ICommandFactory {
    public Command getCommand(String [] var);
}
