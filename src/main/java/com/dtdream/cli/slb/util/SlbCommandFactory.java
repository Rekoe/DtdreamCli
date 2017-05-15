package com.dtdream.cli.slb.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.slb.Help;
import com.dtdream.cli.slb.loadbalancer.DescribeLoadBalancers;

/**
 * Created by shumeng on 2016/11/30.
 */
public class SlbCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            case "describeLoadBalancers" : return new DescribeLoadBalancers(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' slb -help '查找帮助");
                break;
        }
        return null;
    }
}
