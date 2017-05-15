package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.DetachDiskRequest;
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
public class DetachDisk extends Command implements CommandParser{
    private String instanceId;
    private String diskId;
    public DetachDisk(EcsCommandFactory factory, String [] parameters) {
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
            if(EcsUtil.getDiskPortable(diskId)){
                DetachDiskRequest request = new DetachDiskRequest();
                request.setInstanceId(instanceId);
                request.setDiskId(diskId);
                try{
                    DescribeInstanceStatusResponse.InstanceStatus status = EcsUtil.getInstanceStatus(instanceId);
                    switch (status.getStatus()){
                        case STOPPED :
                        case RUNNING :
                            EcsClient.getInstance().getAcsResponse(request);
                            System.out.printf("Detach disk: %s of ECS instance: %s succeed!\n", diskId, instanceId);
                            break;
                        default:
                            System.out.printf("ECS instance: %s 当前状态不支持挂载磁盘操作，请先将实例状态置为:stopped或running状态");
                    }
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                }finally {
                    CommandRecord.getInstance().popLastCommand();
                }
            }else{
                System.out.printf("Disk ID 为: %s 的磁盘不支持卸载操作！！！");
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    detachDisk 创建磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    detachDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- Portable 属性为 True 的磁盘，且磁盘所挂载的实例的状态必须为 running 或者 stopped，且实例的 OperationLocks 中没有标记 \"LockReason\" : \"security\" 的锁定状态，磁盘可以执行此操作。\n" +
                "当一块独立普通云盘通过这个接口从一台实例上卸载后，DeleteWithInstance 会被置为 False。\n" +
                "如果 DiskId 的磁盘不挂载在 InstanceId 的实例上，该操作失败。");
        System.out.println("Options [-i -I]");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例ID");
        System.out.println("    -I");
        System.out.println("         --diskId, 磁盘ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=5){
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
                        System.out.println("参数错误！请输入：detachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-I" :
                    index++;
                    if(parameters.length > index){
                        diskId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：detachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：detachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(instanceId==null || diskId==null){
            System.out.println("参数错误,缺少必要参数，请输入：detachDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

}
