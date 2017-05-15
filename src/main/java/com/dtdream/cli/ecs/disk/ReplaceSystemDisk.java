package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.ReplaceSystemDiskRequest;
import com.aliyuncs.ecs.model.v20140526.ReplaceSystemDiskResponse;
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
 * Created by thomugo on 2016/11/1.
 */
public class ReplaceSystemDisk extends Command implements CommandParser {
    private String instanceId;
    private String imageId;
    public ReplaceSystemDisk(EcsCommandFactory factory, String [] parameters) {
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
            ReplaceSystemDiskRequest request = new ReplaceSystemDiskRequest();
            request.setInstanceId(instanceId);
            request.setImageId(imageId);
            DescribeInstanceStatusResponse.InstanceStatus.Status status = EcsUtil.getInstanceStatus(instanceId).getStatus();
            if(status.equals(DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED)){
                try{
                    ReplaceSystemDiskResponse response = EcsClient.getInstance().getAcsResponse(request);
                    System.out.println("The system disk was replaced!");
                    System.out.println("The new system disk is: " + response.getDiskId());
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                } finally {
                    CommandRecord.getInstance().popLastCommand();
                }
            }else{
                System.out.println("当前实例状态不对，只有当实例状态为stopped时才可执行更换系统盘操作！");
                CommandRecord.getInstance().popLastCommand();
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    replaceDisk 更换系统磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    replaceDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --更换系统盘会改变这台云服务器的系统盘的磁盘 ID。更换系统盘时，不改变系统盘的磁盘种类和付费方式，同时原系统盘将被删除；\n" +
                "更换系统盘时可以重新指定系统盘的容量大小（参数SystemDiskSize），SystemDisksSize必须大于等于max{40,更换前的系统盘容量}；" +
                "实例的状态必须为 Stopped 状态，且实例的 OperationLocks 中没有标记 \"LockReason\" : \"security\" 的锁定状态，且不欠费;");
        System.out.println("Options [-i -I]");
        System.out.println("    -i");
        System.out.println("         --instanceId, ECS实例ID");
        System.out.println("    -I");
        System.out.println("         --imageId, 镜像ID");
    }
    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
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
                    }else {
                        System.out.println("参数错误！请输入：replaceSystemDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-I" :
                    index++;
                    if(parameters.length > index){
                        imageId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：replaceSystemDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：replaceSystemDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(instanceId==null || imageId==null){
            System.out.println("参数错误，缺少必要参数！请输入：replaceSystemDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
