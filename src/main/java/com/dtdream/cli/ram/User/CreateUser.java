package com.dtdream.cli.ram.User;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.CreateUserRequest;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
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
public class CreateUser extends Command implements CommandParser{
    private final String COMMAND_NAME = "createUser";
    private String userName;
    private String displayName;
    private String mobilePhone;
    private String email;
    private String comments;

    public CreateUser(RamCommandFactory factory, String [] parameters) {
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
            CreateUserRequest request = new CreateUserRequest();
            request.setUserName(userName);
            request.setDisplayName(displayName);
            request.setMobilePhone(mobilePhone);
            request.setEmail(email);
            request.setComments(comments);
            try{
                CreateUserResponse response = RamClient.getInstance().getAcsResponse(request);
                System.out.println("A new ram user was created!!!");
                System.out.println("UserID\t\t\t\tUserName:\t\t\t\tDisplayName\t\t\t\tMobilePhone\t\t\tEmail\t\t" +
                        "\t\t\tCreateDate\t\t\t\tComments");
                CreateUserResponse.User user = response.getUser();
                System.out.printf("%-18s\t%-20s\t%-20s\t%-16s\t%-20s\t%-20s\t%s\n",
                        user.getUserId(),
                        user.getUserName(),
                        user.getDisplayName(),
                        user.getMobilePhone(),
                        user.getEmail(),
                        user.getCreateDate(),
                        user.getComments());
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
        System.out.println("    createUser");
        System.out.println("SYNOPSIS");
        System.out.println("    createeUser [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --创建一个ram用户");
        System.out.println("Options [-n]");
        System.out.println("    -n");
        System.out.println("         --userName, 用户名");
        System.out.println("    -d");
        System.out.println("         --displayName, 显示名");
        System.out.println("    -m");
        System.out.println("         --mobilePhone, 电话");
        System.out.println("    -e");
        System.out.println("         --email, 电子邮箱");
        System.out.println("    -c");
        System.out.println("         --comments, 备注");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=11){
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
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        displayName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-m" :
                    index++;
                    if(parameters.length > index){
                        mobilePhone = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        email = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        comments = parameters[index];
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
        if(StringUtils.isBlank(userName)){
            System.out.println("参数错误，缺少必要参数：userName 不能为空，请输入：createUser -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
