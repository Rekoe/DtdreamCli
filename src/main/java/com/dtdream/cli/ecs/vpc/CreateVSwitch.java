package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.CreateVSwitchRequest;
import com.aliyuncs.ecs.model.v20140526.CreateVSwitchResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/11.
 */
public class CreateVSwitch extends Command implements CommandParser{
    private final String COMMAND_NAME = "createVSwitch";
    private String vSwitchName;
    private String vpcId;
    private String description;
    private String zoneId;
    private String cidrBlock;
    public CreateVSwitch(EcsCommandFactory factory, String [] parameters) {
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
            CreateVSwitchRequest request = new CreateVSwitchRequest();
            request.setDescription(description);
            request.setVpcId(vpcId);
            request.setCidrBlock(cidrBlock);
            request.setZoneId(zoneId);
            request.setVSwitchName(vSwitchName);
            try{
                CreateVSwitchResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("A new switch: " + response.getVSwitchId() + " was created!");
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
        System.out.println("    createVSwitch 创建交换机");
        System.out.println("SYNOPSIS");
        System.out.println("    createVSwitch [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --在指定 VPC 内创建一个新的VSwitch。\n" +
                "      VSwitch 下的云产品实例不能分布在不同可用区\n" +
                "      每个 VPC 的 VSwitch 数量不允许超过 24 个\n" +
                "      每个 VSwitch 的掩码为 16 - 29 位，可以提供 8 - 65535 个 IP 地址\n" +
                "      每个 VSwitch 的第 1 个和最后 3 个 IP 地址为系统保留(以 192.168.1.0 / 24 为例，192.168.1.0 和 192.168.1.255 以及 " +
                "      192.168.1.253 - 254 这些地址是系统保留地址)。\n" +
                "      VSwitch 不支持组播和广播\n" +
                "      VSwitch 的 CIDRBlock，必须从属于所在 VPC 的 CIDRBlock\n" +
                "      VSwitch 的 CIDRBlock 可以与所在 VPC 的 CIDRBlock 相同，但这意味着您的 VPC 只能拥有 1 个 VSwitch\n" +
                "      VSwitch 的 CIDRBlock，不能与所在 VPC 当前 RouteEntry 的 DestCIDRBlock 相同\n" +
                "      VSwitch 的 CIDRBlock，不能包含所在 VPC 当前 RouteEntry 的 DestCIDRBlock，但可以是当前 RouteEntry 的 DestCIDRBlock" +
                " 的子集\n" +
                "      VSwitch 创建成功后，CidrBlock 无法修改\n" +
                "      VSwitch 下的云产品实例数量不允许超过 VPC 剩余的可用云产品实例数量（5000 - 当前云产品实例数量）\n" +
                "      1 个云产品实例只能属于 1 个 VSwitch");
        System.out.println("Options [-i -z -c]");
        System.out.println("    -i");
        System.out.println("         --vpcId, 指定VSwitch所在的 VPC");
        System.out.println("    -d");
        System.out.println("         --description, 交换机描述信息");
        System.out.println("    -n");
        System.out.println("         --vSwitchName, 交换机名称");
        System.out.println("    -z");
        System.out.println("         --zoneId, 可用区ID");
        System.out.println("    -c");
        System.out.println("         --cidrBlock，指定VSwitch的网段,必须采用CIDR格式来指定IP地址范围,如 192.168.1.0/24(每个 VSwitch 的掩码为 16 - 29 位)");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        vpcId = parameters[index];
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
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        vSwitchName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
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
                case "-z" :
                    index++;
                    if(parameters.length > index){
                        zoneId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                    default:
                        advice(COMMAND_NAME);
                        return false;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(zoneId) || StringUtils.isBlank(cidrBlock) || StringUtils.isBlank(vpcId)){
            System.out.println("缺少必要参数，请输入：createVSwitch -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
