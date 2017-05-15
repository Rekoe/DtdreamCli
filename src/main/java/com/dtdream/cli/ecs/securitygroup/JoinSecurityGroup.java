package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.JoinSecurityGroupRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/10/27.
 */
public class JoinSecurityGroup extends Command implements CommandParser {
    private String instanceId;
    private String securityGroupId;
    public JoinSecurityGroup(EcsCommandFactory factory, String [] parameters) {
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
            JoinSecurityGroupRequest request = new JoinSecurityGroupRequest();
            request.setInstanceId(instanceId);
            request.setSecurityGroupId(securityGroupId);
            DescribeInstanceStatusResponse.InstanceStatus.Status status = EcsUtil.getInstanceStatus(instanceId).getStatus();
            if(status==DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED || status==DescribeInstanceStatusResponse.InstanceStatus.Status.RUNNING){
                try{
                    EcsClient.getInstance().getAcsResponse(request);
                    System.out.println("ECS instance: " + instanceId + "joinned security group: " + securityGroupId);
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                } finally {
                    CommandRecord.getInstance().popLastCommand();
                }
            }else{
                System.out.println("实例状态为 Stopped 或 Running 状态下才可以执行该操作。当前实例状态不支持此操作！！！");
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    joinSecurityGroup 只有在实例状态为 Stopped 或 Running 状态下才可以执行该操作。\n" +
                "每个实例最多属于 5 个安全组\n" +
                "每个安全组最多拥有个 1000 个实例");
        System.out.println("SYNOPSIS");
        System.out.println("    joinSecurityGroup [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 停止ECS实例");
        System.out.println("Options");
        System.out.println("    -s");
        System.out.println("         --securityGroupId, 安全组ID。");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'joinSecurityGroup -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=5){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'joinSecurityGroup -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-g" :
                    index++;
                    if(parameters.length > index){
                        securityGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'joinSecurityGroup -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误");
                        System.out.println("请输入 'joinSecurityGroup -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }
        if(instanceId==null || securityGroupId==null){
            System.out.println("参数错误,缺少必要参数");
            System.out.println("请输入 'joinSecurityGroup -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
