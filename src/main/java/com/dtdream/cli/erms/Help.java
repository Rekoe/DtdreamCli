package com.dtdream.cli.erms;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/11/22.
 */
public class Help extends Command implements CommandParser{
    public Help(ICommandFactory factory) {
        super(factory);
    }

    @Override
    public boolean parse(String[] parameters) {
        return false;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public boolean checkInput(String var1) {
        return false;
    }

    @Override
    public String getCommandKey() {
        return null;
    }

    @Override
    public Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        help();
        CommandRecord.getInstance().popLastCommand();
    }

    @Override
    public void help() {
        System.out.println("欢迎使用 DtDreamCli Erms 模块!!! ");
        System.out.println("云资产管理相关：");
        System.out.println("    --erms  listResourcesForResSet            获取指定资源合集下某类云产品实例");
        System.out.println("    --erms  listResourcesForResCenter         获取指定资源中心中某类云产品实例");
    }
}
