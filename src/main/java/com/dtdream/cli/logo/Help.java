package com.dtdream.cli.logo;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/12/2.
 */
public class Help extends Command implements CommandParser{
    public Help(LogoCommandFactory factory) {
        super(factory);
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
        System.out.println("欢迎使用 DtDreamCli Logo 模块!!! ");
        System.out.println("Logo相关：");
        System.out.println("    --logo  show            展示产品logo");
        System.out.println("    --logo  getLogo         获取当前所用logo名称");
        System.out.println("    --logo  setLogo         设置产品logo");
    }

    @Override
    public boolean parse(String[] parameters) {
        return false;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
