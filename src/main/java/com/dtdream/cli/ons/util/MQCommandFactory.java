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

package com.dtdream.cli.ons.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ons.Help;
import com.dtdream.cli.ons.topic.CreateTopic;
import com.dtdream.cli.ons.topic.DeleteTopic;
import com.dtdream.cli.ons.topic.ListTopics;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 * @date 2017/2/13
 */
public class MQCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]){
            case "-help" :
            case "--help" : return new Help(this);
            //topic管理
            case "createTopic" : return new CreateTopic(this, var);
            case "listTopic" : return new ListTopics(this, var);
            //case "getTopic" : return new GetTopic(this, var);
            case "deleteTopic" : return new DeleteTopic(this, var);
            default :
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' ram -help '查找帮助");
                break;
        }
        return null;
    }
}
