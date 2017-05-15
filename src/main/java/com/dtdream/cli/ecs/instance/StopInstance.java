package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.StopInstanceRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by thomugo on 2016/10/24.
 */
public class StopInstance extends Command implements CommandParser {
    private String instanceName;
    private String instanceId;
    private boolean force = false;
    public StopInstance(EcsCommandFactory factory, String [] parameters) {
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
            StopInstanceRequest request = new StopInstanceRequest();
            request.setForceStop(force);
            if(instanceId != null){
                request.setInstanceId(instanceId);
                try{
                    DescribeInstanceStatusResponse.InstanceStatus istatus = EcsUtil.getInstanceStatus(instanceId);
                    if(!(istatus.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED || istatus.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPING))
                    {
                        EcsClient.getInstance().getAcsResponse(request);
                        System.out.println("ECS instance: " + instanceId + " was stopped");
                    }else{
                        System.out.println("ECS instance: " + instanceId + " was already STOPPED or in STOPPING");
                    }
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                } finally {
                    CommandRecord.getInstance().popLastCommand();
                }
            }
            if(instanceId==null && instanceName!=null){
                List<DescribeInstancesResponse.Instance> list = EcsUtil.getInstance(instanceId, instanceName);
                if(list != null){
                    try{
                        for(DescribeInstancesResponse.Instance instance : list){
                            DescribeInstanceStatusResponse.InstanceStatus status = EcsUtil.getInstanceStatus(instance.getInstanceId());
                            //System.out.println(status.getStatus());
                            //System.out.println(DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED);
                            if(!(status.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED || status.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPING)){
                                request.setInstanceId(instance.getInstanceId());
                                EcsClient.getInstance().getAcsResponse(request);
                                System.out.println("ECS instance: " + instance.getInstanceId() + "/" + instanceName + " was stopped");
                            }
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

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    stopInstance");
        System.out.println("SYNOPSIS");
        System.out.println("    stopInstance [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 停止ECS实例");
        System.out.println("Options");
        System.out.println("    -n");
        System.out.println("         --instanceName, 实例的显示名称。");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID。");
        System.out.println("    -f");
        System.out.println("         --forceStop, 关机策略，默认值为 false。\n" +
                "若为 false 则走正常关机流程；若为 true 则强制关机。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'stopInstance' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=7){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-f" :
                    index++;
                    if(parameters.length > index){
                        force = Boolean.parseBoolean(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'stopInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'stopInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        instanceName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'stopInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误");
                    System.out.println("请输入 'stopInstance -help' 查看相关指令");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
