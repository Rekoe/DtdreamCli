package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.AttachDiskRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
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
public class AttachDisk extends Command implements CommandParser {
    private String instanceId;
    private String diskId;
    private boolean deleteWithInstance = false;
    public AttachDisk(EcsCommandFactory factory, String [] parameters) {
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
            AttachDiskRequest request = new AttachDiskRequest();
            request.setInstanceId(instanceId);
            request.setDiskId(diskId);
            request.setDeleteWithInstance(deleteWithInstance);
            try{
                DescribeInstanceStatusResponse.InstanceStatus status = EcsUtil.getInstanceStatus(instanceId);
                switch (status.getStatus()){
                    case STOPPED :
                    case RUNNING :
                        EcsClient.getInstance().getAcsResponse(request);
                        System.out.printf("Attach disk: %s to ecs instance: %s succeed!", diskId, instanceId);
                    break;
                    default:
                        System.out.printf("ECS instance: %s 当前状态不支持挂载磁盘操作，请先将实例状态置为:stopped或running状态");
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
        System.out.println("    attachDisk 挂载磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    attachDisk [options -i -I] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 实例的状态必须为 running 或者 stopped，且实例的 OperationLocks 中没有标记 \"LockReason\" : \"security\" 的锁定状态，且不欠费。");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例ID。");
        System.out.println("    -I");
        System.out.println("         --diskId, 磁盘ID。");
        System.out.println("    -d");
        System.out.println("         --deleteWithInstance,磁盘随实例释放。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=7){
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
                        System.out.println("参数错误！请输入：attachDisk -help 查询帮助");
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
                        System.out.println("参数错误！请输入：attachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        deleteWithInstance = Boolean.parseBoolean(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：attachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：attachDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(instanceId == null){
            System.out.println("参数错误,instanceId 不能为空！请输入：attachDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(diskId == null){
            System.out.println("参数错误,diskId 不能为空！请输入：attachDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
