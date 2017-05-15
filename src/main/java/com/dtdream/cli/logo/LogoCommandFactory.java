package com.dtdream.cli.logo;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/12/2.
 */
public class LogoCommandFactory implements ICommandFactory{
    Logo logo = null;
    public LogoCommandFactory(Logo logo){
        this.logo = logo;
    }
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--Help" :
            case "-h" :
            case "-H" : return new Help(this);
            case "setLogo" : return new SetLogo(this, logo, var);
            case "show" : return new Show(this, logo, var);
            case "getLogo" : return new GetLogo(this, logo, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' logo -help '查找帮助");
                System.out.println();
                break;
        }
        return null;
    }
}
