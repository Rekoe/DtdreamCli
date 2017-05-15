/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product: DtdreamCli
 * Module Name: ram
 * Date Created: 2016/12/8
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2016/12/8      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ram.User;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.UpdateUserRequest;
import com.dtdream.cli.annotation.AnnotatedCommand;
import com.dtdream.cli.annotation.Option;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.RamClient;

/**
 * Description 更新用户的基本信息
 *
 * @author thomugo
 * @since 1.0.0
 * @date 2016/12/8
 */
@com.dtdream.cli.annotation.Command(name = "updateUser", description = "更新用户的基本信息")
public class UpdateUser extends AnnotatedCommand{
    private String outputFormater = Config.outputFormat;
    @Option(paraTag = "-n", comment = "用户名", required = true)
    private String userName;
    @Option(paraTag = "-N", comment = "新用户名", required = true)
    private String newUserName;
    @Option(paraTag = "-d", comment = "新显示名")
    private String newDisplayName;
    @Option(paraTag = "-m", comment = "新电话号码")
    private String newMobilePhone;
    @Option(paraTag = "-e", comment = "新电子邮箱")
    private String newEmail;
    @Option(paraTag = "-c", comment = "新备注")
    private String newComments;

    public UpdateUser(ICommandFactory factory, String[] parameters) {
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
            UpdateUserRequest request = new UpdateUserRequest();
            request.setUserName(userName);
            request.setNewUserName(newUserName);
            request.setNewComments(newComments);
            request.setNewDisplayName(newDisplayName);
            request.setNewEmail(newEmail);
            request.setNewMobilePhone(newMobilePhone);
            try{
                RamClient.getInstance().getAcsResponse(request);
                System.out.println("User: " + userName + "'s information was updated!!");
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void printText() {

    }

    @Override
    public void printJson() {

    }
}
