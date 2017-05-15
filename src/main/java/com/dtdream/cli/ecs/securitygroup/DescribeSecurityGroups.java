package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by shumeng on 2016/11/15.
 */
public class DescribeSecurityGroups extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeSecurityGroups";
    private String regionId = Config.getRegion();
    private String vpcId;
    private int pageSize = 10;
    private int pageNum = 1;
    public DescribeSecurityGroups(EcsCommandFactory factory, String [] parameters) {
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
            DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
            request.setRegionId(regionId);
            request.setVpcId(vpcId);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            try{
                DescribeSecurityGroupsResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroups = response.getSecurityGroups();
                for(DescribeSecurityGroupsResponse.SecurityGroup securityGroup : securityGroups){
                    System.out.printf("SecurityGroupID: %-12s  SecurityGroupName: %-17s  VpcID: %s\n    CreationTime: %-18s  " +
                                    "Description: %s\n",
                            securityGroup.getSecurityGroupId(),
                            securityGroup.getSecurityGroupName(),
                            securityGroup.getVpcId(),
                            securityGroup.getCreationTime(),
                            securityGroup.getDescription());
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
        System.out.println("    describeSecurityGroups 查询安全组列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeSecurityGroups [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --分页查询用户定义的所有安全组基本信息。每页的数量默认为10条，数据按照安全组ID降序排列。");
        System.out.println("Options");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小。[1, 50]");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。");
        System.out.println("    -r");
        System.out.println("         --regionId, 区域ID。");
        System.out.println("    -i");
        System.out.println("         --vpcId, vpcID。");
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
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        pageNum = Integer.parseInt(parameters[index]);
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
        return true;
    }
}
