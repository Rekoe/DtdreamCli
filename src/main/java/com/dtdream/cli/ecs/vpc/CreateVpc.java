package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.CreateVpcRequest;
import com.aliyuncs.ecs.model.v20140526.CreateVpcResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by shumeng on 2016/11/11.
 */
public class CreateVpc extends Command implements CommandParser{
    private final String COMMAND_NAME = "createVpc";
    private String cidrBlock;
    //private String userCidr;
    private String vpcName;
    private String description;
    public CreateVpc(EcsCommandFactory factory, String [] parameters) {
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
            CreateVpcRequest request = new CreateVpcRequest();
            request.setDescription(description);
            request.setCidrBlock(cidrBlock);
            //request.setUserCidr(userCidr);
            request.setVpcName(vpcName);
            try{
                CreateVpcResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("A new VPC: " + response.getVpcId() + " was created!");
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
        System.out.println("    createVpc 创建安全组");
        System.out.println("SYNOPSIS");
        System.out.println("    createVpc [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --在指定的地域创建 1 个 VPC\n" +
                "      VPC 只能指定 1 个 CIDRBlock，CIDRBlock 的范围包括 10.0.0.0/8、172.16.0.0/12 和 192.168.0.0/16 " +
                "及它们的子网，CIDRBlock 的掩码为 8 - 24 位，默认为 172.16.0.0/12 。\n" +
                "      VPC 创建后无法修改 CIDRBlock\n" +
                "      每个 VPC 包含的云产品实例不能分布在不同 Region，可以分布在同一 Region 的不同可用区内");
        System.out.println("Options []");
        System.out.println("    -c");
        System.out.println("         --cidrBlock, 可选值 10.0.0.0/8、172.16.0.0/12和192.168.0.0/16及它们包含的子网。默认是172.16.0.0/12。");
        //System.out.println("    -u");
        //System.out.println("         --userCidr, 用户自定义私网网段（多个 UserCidr 使用用半角逗号字符隔开，最多3个）。");
        System.out.println("    -n");
        System.out.println("         --vpcName, vpc名称。");
        System.out.println("    -d");
        System.out.println("         --description, 描述信息。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        cidrBlock = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                /*case "-u" :
                    index++;
                    if(parameters.length > index){
                        userCidr = parameters[index];
                        userCidr = EcsUtil.toJsonStr(userCidr);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }*/
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        vpcName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
