package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.ModifyInstanceVpcAttributeRequest;
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
public class ModifyInstanceVpcAttribute extends Command implements CommandParser {
    private String instanceId;
    private String vSwitchId;
    private String privateIpAddr;

    public ModifyInstanceVpcAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifyInstanceVpcAttributeRequest request = new ModifyInstanceVpcAttributeRequest();
            request.setInstanceId(instanceId);
            request.setVSwitchId(vSwitchId);
            request.setPrivateIpAddress(privateIpAddr);
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
        System.out.println("    modifyInstanceVpcAttribute");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyInstanceVpcAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改云服务器实例的 VPC 属性。");
        System.out.println("Options");
        System.out.println("    -v");
        System.out.println("         --vSwitchId, 新的交换机 ID，不能跨可用区修改实例的交换机。");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID");
        System.out.println("    -a");
        System.out.println("         --ipAddress, 新的私网 IP 地址，不能单独指定");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if (parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length <=7){
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
                        CommandRecord.getInstance().popLastCommand();
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        vSwitchId = parameters[index];
                        CommandRecord.getInstance().popLastCommand();
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        privateIpAddr = parameters[index];
                        CommandRecord.getInstance().popLastCommand();
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default :
                    System.out.println("参数错误");
                    System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        if(instanceId==null || vSwitchId==null){
            System.out.println("参数不足，缺少必要参数");
            System.out.println("请输入 'modifyInstanceVpcAttribute -help' 查看相关指令");
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
