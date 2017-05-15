package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.DeleteSnapshotRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/2.
 */
public class DeleteSnapshot extends Command implements CommandParser {
    private String snapshotId;
    public DeleteSnapshot(EcsCommandFactory factory, String [] parameters) {
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
            DeleteSnapshotRequest request = new DeleteSnapshotRequest();
            request.setSnapshotId(snapshotId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Snapshot: " + snapshotId + " was deleted!");
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
        System.out.println("    deleteSnapshot 删除指定快照");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteSnapshot [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定的快照。如果需要取消创建中的快照（即快照进度尚未达到 100%），也可以调用该接口将快照删除（即取消快照创建）。\n" +
                "      如果指定 ID 的快照不存在，请求将被忽略。\n" +
                "      如果快照已经被用于创建自定义镜像，则这个快照不能被删除。需要先删除相应的自定义镜像，快照才能被删除。");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --snapshotId, 快照ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length <=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        snapshotId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：deleteSnapshot -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：deleteSnapshot -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(snapshotId == null){
            System.out.println("参数错误, 缺少必要参数！请输入：deleteSnapshot -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
