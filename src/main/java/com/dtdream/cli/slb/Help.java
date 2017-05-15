package com.dtdream.cli.slb;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.slb.util.SlbCommandFactory;

/**
 * Created by shumeng on 2016/11/30.
 */
public class Help extends Command implements CommandParser{
    public Help(SlbCommandFactory factory) {
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

    }

    @Override
    public void help() {
        System.out.println("欢迎使用 DtDreamCli SLB 模块!!! ");
        System.out.println("loadBalancer相关：");
        System.out.println("    --slb  DescribeLoadBalancers           查询用户创建的所有LoadBalancer列表。");
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
