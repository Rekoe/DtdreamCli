package com.dtdream.cli.rds.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.DescribeDBInstanceClasses;
import com.dtdream.cli.rds.DescribeRegions;
import com.dtdream.cli.rds.Help;
import com.dtdream.cli.rds.instance.CreateDBInstance;
import com.dtdream.cli.rds.instance.DeleteDBInstance;
import com.dtdream.cli.rds.instance.DescribeDBInstances;

/**
 * Created by shumeng on 2016/11/30.
 */
public class RdsCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            //RDS实例相关
            case "describeDBInstanceClasses" : return new DescribeDBInstanceClasses(this, var);
            case "describeDBInstances" : return new DescribeDBInstances(this, var);
            case "createDBInstance" : return new CreateDBInstance(this, var);
            case "deleteDBInstance" : return new DeleteDBInstance(this, var);
            //区域相关
            case "describeRegions" : return new DescribeRegions(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' rds -help '查找帮助");
                break;
        }
        return null;
    }
}
