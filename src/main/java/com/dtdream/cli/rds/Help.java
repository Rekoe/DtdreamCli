package com.dtdream.cli.rds;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;

/**
 * Created by shumeng on 2016/11/30.
 */
public class Help extends Command implements CommandParser{
    public Help(RdsCommandFactory factory) {
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
        System.out.println("欢迎使用 DtDreamCli RDS 模块!!! ");
        System.out.println("区域管理：");
        System.out.println("    --rds  describeRegions              查看区域信息");
        System.out.println("实例管理：");
        System.out.println("    --rds  describeDBInstanceClasses    查询RDS实例类型");
        System.out.println("    --rds  describeDBInstances          查看RDS实例列表");
        System.out.println("    --rds  createDBInstance             创建RDS实例");
        System.out.println("    --rds  deleteDBInstance             删除指定RDS实例");
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
