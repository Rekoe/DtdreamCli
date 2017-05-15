package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/11/16.
 */
public class DescribeSecurityGroupAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeSecurityGroupAttribute";
    private String regionId = Config.getRegion();
    private String securityGroupId;
    private String nicType = "internet";
    private String direction = "all";
    public DescribeSecurityGroupAttribute(EcsCommandFactory factory, String [] parameters) {
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
            DescribeSecurityGroupAttributeRequest request = new DescribeSecurityGroupAttributeRequest();
            request.setRegionId(regionId);
            request.setSecurityGroupId(securityGroupId);
            request.setNicType(nicType);
            request.setDirection(direction);
            try{
                DescribeSecurityGroupAttributeResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.printf("VpcID: %-12s  SecurityGroupID: %-12s SecurityGroupName: %-17s\n    Description: " +
                                "%s\n",
                        response.getVpcId(),
                        response.getSecurityGroupId(),
                        response.getSecurityGroupName(),
                        response.getDescription());
                List<DescribeSecurityGroupAttributeResponse.Permission> permissions = response.getPermissions();
                for(DescribeSecurityGroupAttributeResponse.Permission permission : permissions){
                    System.out.println("IPProtocol  PortRange  SourceCidrIP  SourceGroupId  " +
                            "DestCidrIp  DestCidrIp  Policy  NicType  Priority  Direction");
                    System.out.printf("%-10s  %-9s  %-12s  %-13s  %-10s  %-10s  %-6s  %-7s  %-8s  %-9s\n",
                            permission.getIpProtocol(),
                            permission.getPortRange(),
                            permission.getSourceCidrIp(),
                            permission.getSourceGroupId(),
                            permission.getDestCidrIp(),
                            permission.getDestGroupId(),
                            permission.getPolicy(),
                            permission.getNicType(),
                            permission.getPriority(),
                            permission.getDirection());
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
        System.out.println("    describeSecurityGroupAttribute 查询安全组规则");
        System.out.println("SYNOPSIS");
        System.out.println("    describeSecurityGroupAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询安全组详情，包括安全权限控制。");
        System.out.println("Options [-i]");
        System.out.println("    -r");
        System.out.println("         --regionId, 区域ID");
        System.out.println("    -i");
        System.out.println("         --securityGroupId, 安全组ID。");
        System.out.println("    -t");
        System.out.println("         --nicType, 网络类型，取值：internet | intranet，默认值为 internet");
        System.out.println("    -d");
        System.out.println("         --direction, 授权方向，取值：egress|ingress|all，默认值为all");
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
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        regionId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        securityGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        nicType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        direction = parameters[index];
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
        if(StringUtils.isBlank(regionId) || StringUtils.isBlank(securityGroupId)){
            System.out.println("参数错误，缺少必要参数，请输入: describeSecurityGroupAttribute -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
