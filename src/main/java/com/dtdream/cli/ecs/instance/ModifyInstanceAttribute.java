package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceAttributeRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/10/26.
 */
public class ModifyInstanceAttribute extends Command implements CommandParser {
    private String instanceId;
    private String instanceName;
    private String description;
    private String password;
    private String hostName;
    public ModifyInstanceAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest();
            request.setInstanceId(instanceId);
            request.setDescription(description);
            request.setHostName(hostName);
            request.setPassword(password);
            request.setInstanceName(instanceName);
            try{
                EcsClient.getInstance().getAcsResponse(request);
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
        System.out.println("    modifyInstanceAttribute");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyInstanceAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 停止ECS实例");
        System.out.println("Options");
        System.out.println("    -n");
        System.out.println("         --instanceName, 实例的显示名称，[2, 128] 英文或中文字符，必须以大小字母或中文开头，可包含数字，”.”，”_“或”-“。不能以 http:// 和 https:// 开头。");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID");
        System.out.println("    -d");
        System.out.println("         --description, 实例的描述，[2, 256] 个字符。实例描述会显示在控制台。默认为空。不能以 http:// 和 https:// 开头。");
        System.out.println("    -p");
        System.out.println("         --password, 重置为用户指定的密码。8-30个字符，必须同时包含三项（大、小写字母，数字和特殊符号）。支持以下特殊字符：( ) ` ~ ! @ # $ % ^ & * - + = | { } [ ] : ; ‘ < > , . ? / ");
        System.out.println("    -h");
        System.out.println("         --hostName, 操作系统内部的计算机名，最少 2 字符，”.”和”-“是不能作为 hostname 的首尾字符，不能连续使用。\n" +
                "Windows 平台最长为 15 字符，允许字母（不限制大小写）、数字和”-“组成，不支持点号（”.”），不能全是数字。\n" +
                "其他（Linux 等）平台最长为 30 字符，允许支持多个点号，点之间为一段，每段允许字母（不限制大小写）、数字和”-“组成。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while(parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        instanceName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
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
                        System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
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
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        password = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-h" :
                    index++;
                    if(parameters.length > index){
                        hostName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误");
                    System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        if(instanceId == null){
            System.out.println("缺少必要参数");
            System.out.println("请输入 'modifyInstanceAttribute -help' 查看相关指令");
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
