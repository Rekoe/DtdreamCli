package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.ReInitDiskRequest;
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
public class ReInitDisk extends Command implements CommandParser {
    private String diskId;
    public ReInitDisk(EcsCommandFactory factory, String [] parameters) {
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
            ReInitDiskRequest request = new ReInitDiskRequest();
            request.setDiskId(diskId);
            DescribeDisksResponse.Disk disk = EcsUtil.getDisk(diskId);
            if(disk == null){
                System.out.println("不存在ID为：" + diskId + " 的磁盘");
                CommandRecord.getInstance().popLastCommand();
                return ;
            }else{
                String diskStatus = disk.getStatus();
                String instanceId = disk.getInstanceId();
                DescribeInstanceStatusResponse.InstanceStatus.Status instanceStatus = EcsUtil.getInstanceStatus(instanceId).getStatus();
                if(diskStatus.equals("In_use") && instanceStatus.equals(DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED)){
                    try{
                        EcsClient.getInstance().getAcsResponse(request);
                        System.out.println("Disk: " + diskId + " was reinited");
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } finally {
                        CommandRecord.getInstance().popLastCommand();
                    }
                }else{
                    System.out.println("磁盘或实例状态不符合条件，请输入 reinitDisk -help 查询帮助。");
                    CommandRecord.getInstance().popLastCommand();
                }
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    reInitDisk 初始化磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    reInitDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 重新初始化磁盘到初始状态，磁盘状态必须为使用中（In_use）的状态，且实例状态必须为 Stopped 时，才能执行此操作。" +
                "但是如果实例创建后在第一次启动前，挂载在其上的磁盘也不能重新初始化。\n" +
                "调用此接口，磁盘恢复到创建时的初始状态，即：\n" +
                "如果为数据盘且是直接创建的空盘，则初始化到空盘状态\n" +
                "如果为数据盘且是通过快照创建的，则初始化到快照状态\n" +
                "如果为系统盘，则初始化到镜像的初始状态");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --diskId, 磁盘ID。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        diskId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：reInitDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：reInitDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(diskId == null){
            System.out.println("参数错误，缺少必要参数！请输入：reInitDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
