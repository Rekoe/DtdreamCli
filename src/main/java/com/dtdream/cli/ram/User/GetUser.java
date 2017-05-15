/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product: DtdreamCli
 * Module Name: ram
 * Date Created: 2016/12/8
 * Description: 获取用户的详细信息
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2016/12/8      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ram.User;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.GetUserRequest;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.dtdream.cli.annotation.AnnotatedCommand;
import com.dtdream.cli.annotation.Option;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.util.FormatUtil;
import com.dtdream.cli.util.RamClient;
import org.apache.commons.lang.StringUtils;

import static com.dtdream.cli.util.Config.outputFormat;

/**
 * Description  获取用户的详细信息
 * @author thomugo
 * @since 1.0.0
 * @date 2016/12/8
 */
@com.dtdream.cli.annotation.Command(name = "getUser", description = "获取用户的详细信息")
public class GetUser extends AnnotatedCommand{
    @Option(paraTag = "-n", comment = "用户名", required = true)
    private String userName;
    @Option(paraTag = "-F", comment = "命令输出格式", required = true)
    private String outputFormater = outputFormat;
    private GetUserResponse.User user;

    public GetUser(ICommandFactory factory, String[] parameters) {
        super(factory, parameters);
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(outputFormater)){
            System.out.println("命令行结果输出格式尚未设置，请输入：getUser -help 查询帮助");
        }
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
            GetUserRequest request = new GetUserRequest();
            request.setUserName(userName);
            try{
                GetUserResponse response = RamClient.getInstance().getAcsResponse(request);
                user = response.getUser();
                if(StringUtils.equals(outputFormater, "text")){
                    printText();
                }else{
                    printJson();
                }
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
        if(user != null){
            System.out.println("UserID\t\t\t\tUserName:\t\t\t\tDisplayName\t\t\t\tMobilePhone\t\tEmail\t\t" +
                    "\t\t\tCreateDate\t\t\t\tUpdateDate\t\t\t\tComments");
            System.out.printf("%-18s\t%-20s\t%-20s\t%-12s\t%-20s\t%-20s\t%-20s\t%s\n",
                    user.getUserId(),
                    user.getUserName(),
                    user.getDisplayName(),
                    user.getMobilePhone(),
                    user.getEmail(),
                    user.getCreateDate(),
                    user.getUpdateDate(),
                    user.getComments());
        }
    }

    @Override
    public void printJson() {
        String jsonStr = JSON.toJSONString(user);
        FormatUtil.printJson(jsonStr);
    }
}
