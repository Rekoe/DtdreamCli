/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2017/2/13
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2017/2/13      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ons;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 * @date 2017/2/13
 */
public class Help extends Command implements CommandParser{
    public Help(ICommandFactory factory) {
        super(factory);
    }

    @Override
    public boolean parse(String[] parameters) {
        return false;
    }

    @Override
    public boolean checkParameters() {
        return false;
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
        System.out.println("欢迎使用 DtDreamCli MQ 模块!!! ");
        System.out.println("topic管理接口：");
        System.out.println("    --mq  createTopic             创建新的topic");
        System.out.println("    --mq  listTopic               列出已有topic");
        System.out.println("    --mq  deleteTopic             删除指定topic");
    }
}
