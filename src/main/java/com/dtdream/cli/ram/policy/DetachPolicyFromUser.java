package com.dtdream.cli.ram.policy;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.DetachPolicyFromUserRequest;
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
public class DetachPolicyFromUser extends Command implements CommandParser{
    private final String COMMAND_NAME = "detachPolicyFromUser";
    private String userName;
    private String policyName;
    private String policyType;

    public DetachPolicyFromUser(RamCommandFactory factory, String [] parameters) {
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
            DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
            request.setUserName(userName);
            request.setPolicyName(policyName);
            request.setPolicyType(policyType);
            try{
                RamClient.getInstance().getAcsResponse(request);
                System.out.println("Detach policy: " + policyName + " from user: " + userName + " succeed!!!");
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
        System.out.println("    detachPolicyFromUser");
        System.out.println("SYNOPSIS");
        System.out.println("    detachPolicyFromUser [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --为用户撤销指定的授权");
        System.out.println("Options [-n -p -t]");
        System.out.println("    -n");
        System.out.println("         --userName");
        System.out.println("    -p");
        System.out.println("         --policyName");
        System.out.println("    -t");
        System.out.println("         --policyType");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=7){
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
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        policyName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        policyType = parameters[index];
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
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(policyName) || StringUtils.isBlank(policyType)){
            System.out.println("参数错误，缺少必要参数：userName, policyName, policyType 不能为空，请输入：detachPolicyFromUser -help " +
                    "查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
