package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.DeleteDiskRequest;
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
public class DeleteDisk extends Command implements CommandParser {
    private String diskId;
    public DeleteDisk(EcsCommandFactory factory, String [] parameters) {
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
            String status = EcsUtil.getDiskStatus(diskId);
            if(EcsUtil.getDiskStatus(diskId) != null){
                if(status.equals("Available")){
                    DeleteDiskRequest request = new DeleteDiskRequest();
                    request.setDiskId(diskId);
                    try{
                        EcsClient.getInstance().getAcsResponse(request);
                        System.out.println("Delete disk: " + diskId + " succeed!");
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } finally {
                        CommandRecord.getInstance().popLastCommand();
                    }
                }else{
                    System.out.printf("磁盘当前状态为：%s 不支持删除操作！！！\n", status);
                    CommandRecord.getInstance().popLastCommand();
                }
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    deleteDisk 删除磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 删除指定磁盘。当某个磁盘设备不再使用时，可以删除磁盘。但是只能删除独立普通云盘。\n" +
                           "       删除磁盘时磁盘的 status 必须为 Available。\n" +
                           "       如果指定 ID 的磁盘不存在，请求将被忽略。\n" +
                           "       由于用户快照会被保留，自动快照可以选择保留，请及时删除不必要的孤立用户快照和自动快照（即磁盘已经被删除的快照），以保持足够的快照额度来顺利完成周期性的自动快照策略。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --diskId, 磁盘ID。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--hlep" :
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
                        System.out.println("参数错误！请输入：deleteDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：deleteDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(diskId == null){
            System.out.println("参数错误，缺少必要参数！请输入：deleteDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
