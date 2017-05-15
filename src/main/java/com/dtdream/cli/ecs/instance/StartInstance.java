package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.ecs.model.v20140526.StartInstanceRequest;
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
 * Created by thomugo on 2016/10/18.
 */
public class StartInstance extends Command implements CommandParser {

    private String instanceId;
    private String instanceName;

    public StartInstance(EcsCommandFactory factory, String [] parameters) {
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
            StartInstanceRequest request = new StartInstanceRequest();
            if(instanceId != null){
                request.setInstanceId(instanceId);
                try{
                    DescribeInstanceStatusResponse.InstanceStatus istatus = EcsUtil.getInstanceStatus(instanceId);
                    if(!(istatus.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STARTING || istatus.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.RUNNING))
                    {
                        EcsClient.getInstance().getAcsResponse(request);
                        System.out.println("ECS instance: " + instanceId + " was started");
                    }else{
                        System.out.println("ECS instance: " + instanceId + " was already STARTED or in STARTING");
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
                            if(!(status.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.STARTING || status.getStatus()==DescribeInstanceStatusResponse.InstanceStatus.Status.RUNNING)){
                                request.setInstanceId(instance.getInstanceId());
                                EcsClient.getInstance().getAcsResponse(request);
                                System.out.println("instance: " + instance.getInstanceId() + "/" + instanceName + " was started. ");
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
        System.out.println("    startInstance");
        System.out.println("SYNOPSIS");
        System.out.println("    startInstance [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 启动ECS实例");
        System.out.println("Options");
        System.out.println("    -n");
        System.out.println("         --instanceName, 实例的显示名称。");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'startInstance' 查看相关指令");
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
                    if (parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入：'startInstance -help' 查看相关指令");
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
                        System.out.println("请输入：'startInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误");
                    System.out.println("请输入：'startInstance -help' 查看相关指令");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        if(instanceName==null && instanceId==null){
            System.out.println("缺少必要参数");
            System.out.println("请输入：'startInstance -help' 查看相关指令");
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
