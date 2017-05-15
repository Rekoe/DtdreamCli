package com.dtdream.cli.command;/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product: DtDreamCli
 * Module Name: command
 * Date Created: 2016/12/8
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2016/12/8      thomugo
 * -----------------------------------------------------------------------------
 */


/**
 * Description  命令输出接口,对命令行的输出结果进行格式化，提供多种格式的输出结果
 *
 * @author thomugo
 * @since 1.0.0
 */
public interface CommandOutPuter {
    public void printText();
    public void printJson();
}
