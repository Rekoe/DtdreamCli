package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.ModifyVpcAttributeRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/11.
 */
public class ModifyVpcAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "modifyVpcAttribute";
    private String vpcId;
    private String vpcName;
    private String description;
    private String userCidr;
    public ModifyVpcAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifyVpcAttributeRequest request = new ModifyVpcAttributeRequest();
            request.setVpcId(vpcId);
            request.setVpcName(vpcName);
            request.setDescription(description);
            request.setUserCidr(userCidr);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("VPC: " + vpcId + "'s attribute was modified!");
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
        System.out.println("    modifyVpcAttribute 修改VPC属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyVpcAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改指定 VPC 的属性");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vpcId, VpcID");
        System.out.println("    -n");
        System.out.println("         --vpcName");
        System.out.println("    -d");
        System.out.println("         --description vpc描述信息");
        System.out.println("    -c");
        System.out.println("         --userCidr 用户自定义私网网段（多个userCIdr使用用半角逗号字符隔开，最多3个）,当需要全部清空时，传入 ”－1”");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        vpcId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        vpcName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        userCidr = parameters[index];
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
        if(StringUtils.isBlank(vpcId)){
            System.out.println("参数错误，vpcId 不能为空。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
