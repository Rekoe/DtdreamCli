package com.dtdream.cli.ram.group;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.RemoveUserFromGroupRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.util.RamClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/12/6.
 */
public class RemoveUserFromGroup extends Command implements CommandParser{
    private final String COMMAND_NAME ="removeUserFromGroup";
    private String userName;
    private String groupName;

    public RemoveUserFromGroup(RamCommandFactory factory, String [] parameters) {
        super(factory);
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
        if(parse(parameters)){
            RemoveUserFromGroupRequest request = new RemoveUserFromGroupRequest();
            request.setUserName(userName);
            request.setGroupName(groupName);
            try{
                RamClient.getInstance().getAcsResponse(request);
                System.out.println("Delete ramUser: " + userName + " from group: " + groupName + " succeed!!!");
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
    public void help() {
        System.out.println("NAME");
        System.out.println("    removeUserFromGroup");
        System.out.println("SYNOPSIS");
        System.out.println("    removeUserFromGroup [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --将子用户从用户组中移除");
        System.out.println("Options [-n -g]");
        System.out.println("    -n");
        System.out.println("         --userName");
        System.out.println("    -g");
        System.out.println("         --groupName");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=5){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        userName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-g" :
                    index++;
                    if(parameters.length > index){
                        groupName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                default:
                    advice(COMMAND_NAME);
                    return false;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(groupName)){
            System.out.println("参数错误，缺少必要参数：userName 或 groupName 不能为空，请输入：removeUserFromGroup -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
