package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.ModifySecurityGroupAttributeRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/17.
 */
public class ModifySecurityGroupAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "modifySecurityGroupAttribute";
    private String regionId = Config.getRegion();
    private String securityGroupId;
    private String description;
    private String securityGroupName;
    public ModifySecurityGroupAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifySecurityGroupAttributeRequest request = new ModifySecurityGroupAttributeRequest();
            request.setRegionId(regionId);
            request.setSecurityGroupId(securityGroupId);
            request.setSecurityGroupName(securityGroupName);
            request.setDescription(description);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("ModifySecurityGroup: " + securityGroupId + "'s attribute succeed!");
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
        System.out.println("    modifySecurityGroupAttribute 修改安全组属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifySecurityGroupAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改指定安全组的属性，包括修改安全组名称和描述。");
        System.out.println("Options [-i]");
        System.out.println("    -r");
        System.out.println("         --regionId, 区域ID");
        System.out.println("    -i");
        System.out.println("         --securityGroupId, 安全组ID");
        System.out.println("    -n");
        System.out.println("         --securityGroupName, 安全组名称");
        System.out.println("    -d");
        System.out.println("         --description, 安全组描述信息");
    }

    @Override
    public boolean parse(String[] parameters) {
        int index = 1;
        while (parameters.length>index && parameters.length<=9){
            switch (parameters[index]){
                case "-help" :
                case "--hlep" :
                    advice(null);
                    return false;
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        regionId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        securityGroupId = parameters[index];
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
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        securityGroupName = parameters[index];
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
        if(StringUtils.isBlank(regionId) || StringUtils.isBlank(securityGroupId)){
            System.out.println("参数错误，缺少必要参数，请输入: modifySecurityGroupAttribute -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
