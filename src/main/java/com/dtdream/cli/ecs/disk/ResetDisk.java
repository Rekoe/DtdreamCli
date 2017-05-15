package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeSnapshotsResponse;
import com.aliyuncs.ecs.model.v20140526.ResetDiskRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by thomugo on 2016/11/1.
 */
public class ResetDisk extends Command implements CommandParser {
    private String diskId;
    private String snapShotId;
    public ResetDisk(EcsCommandFactory factory, String [] parameters) {
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
            ResetDiskRequest request = new ResetDiskRequest();
            request.setDiskId(diskId);
            request.setSnapshotId(snapShotId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Rest Disk: " + diskId + " succeed!");
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
        System.out.println("    resetDisk 回滚磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    resetDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --使用指定磁盘自身的快照回滚磁盘内容。\n" +
                "实例状态必须为 Stopped 时，才可以执行此操作。\n" +
                "磁盘状态必须为使用中（In_use）的状态，才能执行此操作。\n" +
                "指定的 SnapshotId 必须是由 DiskId 创建的快照，否则报错。\n" +
                "实例的 OperationLocks 中标记了 \"LockReason\" : \"security\" 的锁定状态，则报错。");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --diskId, 磁盘ID");
        System.out.println("    -s");
        System.out.println("         --snapShotId, 快照ID");
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
                        diskId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：resetDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        snapShotId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：resetDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：resetDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(diskId) || StringUtils.isBlank(snapShotId)){
            System.out.println("参数错误！请输入：resetDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        DescribeDisksResponse.Disk disk = EcsUtil.getDisk(diskId);
        DescribeSnapshotsResponse.Snapshot snapshot = EcsUtil.getSnapshot(snapShotId);
        if(!StringUtils.equals(snapshot.getSourceDiskId(),diskId)){
            System.out.println("当前快照：" + snapShotId + " 不是由磁盘：" + diskId + " 创建的，不支持此操作");
            System.out.println("请输入：resetDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(!disk.getStatus().equals("In_use")){
            System.out.println("当前磁盘状态不支持此操作，输入：resetDisk -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        DescribeInstanceStatusResponse.InstanceStatus status = EcsUtil.getInstanceStatus(disk.getInstanceId());
        if(!status.getStatus().equals(DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED)){
            System.out.println("当前ECS实例状态不支持此操作，输入：resetDisk -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
