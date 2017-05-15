package com.dtdream.cli.ram;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/12/5.
 */
public class Help extends Command implements CommandParser{
    public Help(ICommandFactory factory) {
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
        System.out.println("欢迎使用 DtDreamCli RAM 模块!!! ");
        System.out.println("用户管理接口：");
        System.out.println("    --ram  createUser             创建新的RAM用户");
        System.out.println("    --ram  updateUser             更新用户的基本信息");
        System.out.println("    --ram  listUsers              列出当前云账号下所有RAM用户");
        System.out.println("    --ram  deleteUser             删除指定名称的RAM用户");
        System.out.println("    --ram  getUser                获取用户的详细信息");
        System.out.println("    --ram  listAccessKeys         列出指定用户的AccessKeys");
        System.out.println("    --ram  deleteAccessKey        删除RAM子用户的AccessKey");
        System.out.println("组管理接口：");
        System.out.println("    --ram  listGroupsForUser      列出指定子用户所加入的组信息");
        System.out.println("    --ram  removeUserFromGroup    将子用户从用户组中移除");
        System.out.println("策略管理接口");
        System.out.println("    --ram  listPoliciesForUser    列出指定用户被授予的授权策略");
        System.out.println("    --ram  detachPolicyFromUser   为用户撤销指定的授权");
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
