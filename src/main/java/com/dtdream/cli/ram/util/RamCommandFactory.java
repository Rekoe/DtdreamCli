package com.dtdream.cli.ram.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ram.Demo;
import com.dtdream.cli.ram.Help;
import com.dtdream.cli.ram.User.*;
import com.dtdream.cli.ram.group.ListGroupsForUser;
import com.dtdream.cli.ram.group.RemoveUserFromGroup;
import com.dtdream.cli.ram.policy.DetachPolicyFromUser;
import com.dtdream.cli.ram.policy.ListPoliciesForUser;

/**
 * Created by shumeng on 2016/12/5.
 */
public class RamCommandFactory implements ICommandFactory {
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            //用户管理
            case "getUser" : return new GetUser(this, var);
            case "listUsers" : return new ListUsers(this, var);
            case "createUser" : return new CreateUser(this, var);
            case "updateUser" : return new UpdateUser(this, var);
            case "deleteUser" : return new DeleteUser(this, var);
            case "listAccessKeys" : return new ListAccessKeys(this, var);
            case "deleteAccessKey" : return new DeleteAccessKey(this, var);
            //组管理
            case "listGroupsForUser" : return new ListGroupsForUser(this, var);
            case "removeUserFromGroup" : return new RemoveUserFromGroup(this, var);
            //策略管理
            case "listPoliciesForUser" : return new ListPoliciesForUser(this, var);
            case "detachPolicyFromUser" : return new DetachPolicyFromUser(this, var);
            case "demo" : return new Demo(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' ram -help '查找帮助");
                break;
        }
        return null;
    }
}
