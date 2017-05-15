package com.dtdream.cli.erms.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.erms.Help;
import com.dtdream.cli.erms.ListResourcesForResCenter;
import com.dtdream.cli.erms.ListResourcesForResSet;

/**
 * Created by shumeng on 2016/11/22.
 */
public class ErmsCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            case "listResourcesForResSet" : return new ListResourcesForResSet(this, var);
            case "listResourcesForResCenter" : return new ListResourcesForResCenter(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' erms -help '查找帮助");
                break;
        }
        return null;
    }
}
