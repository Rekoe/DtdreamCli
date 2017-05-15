package com.dtdream.cli.util;

import com.dtdream.cli.Help;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/11/29.
 */
public class DtdreamCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--Help" :
            case "-h" :
            case "-H" : return new Help(this);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' dtdream -help '查找帮助");
                System.out.println();
                break;
        }
        return null;
    }
}
