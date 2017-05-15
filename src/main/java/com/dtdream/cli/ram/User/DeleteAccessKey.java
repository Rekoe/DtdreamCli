package com.dtdream.cli.ram.User;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.DeleteAccessKeyRequest;
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
public class DeleteAccessKey extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteAccessKey";
    private String userName;
    private String accessKeyID;

    public DeleteAccessKey(RamCommandFactory factory, String [] parameters) {
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
            DeleteAccessKeyRequest request = new DeleteAccessKeyRequest();
            request.setUserName(userName);
            request.setUserAccessKeyId(accessKeyID);
            try{
                RamClient.getInstance().getAcsResponse(request);
                System.out.println("Delete accessKeyID: " + accessKeyID + " from user: " + userName + " succeed!!");
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
        System.out.println("    deleteAccessKey");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteAccessKey [options]");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除RAM子用户的AccessKey");
        System.out.println("Options [-n -a]");
        System.out.println("    -n");
        System.out.println("         --userName");
        System.out.println("    -a");
        System.out.println("         --accessKeyID");
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
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        accessKeyID = parameters[index];
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
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(accessKeyID)){
            System.out.println("参数错误，缺少必要参数：userName, accessKeyID 不能为空，请输入：deleteAccessKey -help " +
                    "查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
