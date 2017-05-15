package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.ModifyDiskAttributeRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/1.
 */
public class ModifyDiskAttribute extends Command implements CommandParser {
    private String diskId;
    private String diskName;
    private String description;
    private boolean deleteWithInstance;
    private boolean deleteAutoSnapShot;
    private boolean enableAutoSnapShot;
    ModifyDiskAttributeRequest request = new ModifyDiskAttributeRequest();
    public ModifyDiskAttribute(EcsCommandFactory factory, String [] parameters) {
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
            request.setDiskId(diskId);
            request.setDiskName(diskName);
            request.setDescription(description);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Modify disk attribute succeed!");
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
        System.out.println("    modifyDiskAttribute 修改磁盘属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyDiskAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 修改磁盘属性");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --diskId, 磁盘ID。");
        System.out.println("    -n");
        System.out.println("         --diskName, 磁盘名称，不填则原值，默认值为空，[2, 128] 英文或中文字符，必须以大小字母或中文开头，可包含数字，”.”，”_”或”-”，磁盘名称会展示在控制台。不能以 http:// 和 https:// 开头。");
        System.out.println("    -d");
        System.out.println("         --description, 磁盘描述，不填则保持原值，默认值为空，[2, 256] 个字符，磁盘描述会展示在控制台。不能以 http:// 和 https:// 开头。");
        System.out.println("    -D");
        System.out.println("         --deleteWithInstance磁盘是否随实例释放：\n" +
                "true 表示 Instance 释放时，这块磁盘随 Instance 一起释放\n" +
                "false 表示 Instance 释放时，这块磁盘保留不释放");
        System.out.println("    -r");
        System.out.println("         --deleteAutoSnapshot 删除磁盘时，是否同时删除自动快照。true(表示同时删除自动快照);false(表示保留自动快照)");
        System.out.println("    -e");
        System.out.println("         --enableAutoSnapshot 磁盘是否执行自动快照策略（前提是用户整体的自动快照策略已经开启）：\n" +
                "true 表示这块磁盘执行自动快照策略\n" +
                "false 表示这块磁盘不执行自动快照策略");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=13){
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
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        diskName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-D" :
                    index++;
                    if(parameters.length > index){
                        deleteWithInstance = Boolean.parseBoolean(parameters[index]);
                        request.setDeleteWithInstance(deleteWithInstance);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        deleteAutoSnapShot = Boolean.parseBoolean(parameters[index]);
                        request.setDeleteAutoSnapshot(deleteAutoSnapShot);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        enableAutoSnapShot = Boolean.parseBoolean(parameters[index]);
                        request.setEnableAutoSnapshot(enableAutoSnapShot);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误！请输入：modifyDiskAttribute -help 查询帮助");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(diskId == null){
            System.out.println("参数错误,缺少必要参数！请输入：modifyDiskAttribute -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
