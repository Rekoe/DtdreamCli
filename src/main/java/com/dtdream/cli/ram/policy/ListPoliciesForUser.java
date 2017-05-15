package com.dtdream.cli.ram.policy;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserRequest;
import com.aliyuncs.ram.model.v20150501.ListPoliciesForUserResponse;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.util.RamClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/12/6.
 */
public class ListPoliciesForUser extends Command implements CommandParser{
    private final String COMMAND_NAME ="listPoliciesForUser";
    private String userName;

    public ListPoliciesForUser(RamCommandFactory factory, String [] parameters) {
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
            ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
            request.setUserName(userName);
            try{
                ListPoliciesForUserResponse response = RamClient.getInstance().getAcsResponse(request);
                List<ListPoliciesForUserResponse.Policy> policylist = response.getPolicies();
                System.out.println("Find: " + policylist.size() + " policies");
                System.out.println("PolicyName\t\t\t\t\tPolicyType\t\tDefaultVersion\tAttachDate" +
                        "\t\t\t\tdescription");
                for(ListPoliciesForUserResponse.Policy policy : policylist){
                    System.out.printf("%-26s\t%-12s\t%-10s\t\t%-20s\t%s\n",
                            policy.getPolicyName(),
                            policy.getPolicyType(),
                            policy.getDefaultVersion(),
                            policy.getAttachDate(),
                            policy.getDescription());
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
    public void help() {
        System.out.println("NAME");
        System.out.println("    listPoliciesForUser");
        System.out.println("SYNOPSIS");
        System.out.println("    listPoliciesForUser [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --列出指定用户被授予的授权策略");
        System.out.println("Options [-n]");
        System.out.println("    -n");
        System.out.println("         --userName");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
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
            System.out.println("参数错误，缺少必要参数：userName 不能为空，请输入：listPoliciesForUser -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
