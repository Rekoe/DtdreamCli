package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.DeleteSecurityGroupRequest;
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
public class DeleteSecurityGroup extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteSecurityGroup";
    private String regionId = Config.getRegion();
    private String securityGroupId;

    public DeleteSecurityGroup(EcsCommandFactory factory, String [] parameters) {
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
            DeleteSecurityGroupRequest request = new DeleteSecurityGroupRequest();
            request.setRegionId(regionId);
            request.setSecurityGroupId(securityGroupId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("删除安全组：" + securityGroupId + " 成功!!!");
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
        System.out.println("    deleteSecurityGroup 删除安全组");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteSecurityGroup [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除一个指定的安全组。只有组内不存在实例，或者没有被其他组的安全规则引用时才可以删除。");
        System.out.println("Options [-i]");
        System.out.println("    -I");
        System.out.println("         --regionId, 区域ID。");
        System.out.println("    -i");
        System.out.println("         --securityGroupId, 安全组ID");
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
                case "-I" :
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
            System.out.println("参数错误，缺少必要参数，请输入: deleteSecurityGroup -help 查询帮助！");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
