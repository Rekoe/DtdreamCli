package com.dtdream.cli.logo;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Created by shumeng on 2016/12/2.
 */
public class Show extends Command implements CommandParser{
    private final String COMMAND_NAME = "show";
    private Logo logo = null;

    public Show(LogoCommandFactory factory, Logo logo, String [] parameters) {
        super(factory);
        this.logo = logo;
        this.parameters = parameters;
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
        if(parse(parameters) && logo!=null){
            logo.show();
            CommandRecord.getInstance().popLastCommand();
        }
    }

    @Override
    public void help() {
        System.out.println("NAME:");
        System.out.println("    show 展示当前产品logo");
        System.out.println("SYNOPSIS:");
        System.out.println("    show");
        System.out.println("DESCRIPTION:");
        System.out.println("    --展示当前产品logo");
    }

    @Override
    public boolean parse(String[] parameters) {
        if(parameters.length == 1){
            return true;
        }else if(parameters.length == 2) {
            switch (parameters[1]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                default:
                    advice(COMMAND_NAME);
                    return false;
            }
        }else{
            System.out.printf("参数错误，输入参数过多！");
        }
        return false;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
