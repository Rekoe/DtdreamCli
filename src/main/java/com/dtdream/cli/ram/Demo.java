/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2016/12/7
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2016/12/7      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ram;

import com.dtdream.cli.annotation.AnnotatedCommand;
import com.dtdream.cli.annotation.Option;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.util.FormatUtil;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 */
@com.dtdream.cli.annotation.Command(name = "demo", description = "A annotation Demo!")
public class Demo extends AnnotatedCommand{
    @Option(value = "haohao", paraTag = "-n", comment = "用户名", required = true)
    private String userName;
    @Option(paraTag = "-N", comment = "分页好", required = false)
    private int pageNum = -1;

    public Demo(RamCommandFactory factory, String[] parameters) {
        super(factory, parameters);
    }

    @Override
    public boolean checkParameters() {
        return true;
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
        if(parse(parameters)){
            String jsonStr = "";
            FormatUtil.printJson(jsonStr);
            System.out.println("UserName:" + userName);
            System.out.println("pageNum:" + pageNum);
            CommandRecord.getInstance().popLastCommand();
        }
    }

    @Override
    public void printText() {

    }

    @Override
    public void printJson() {

    }
}
