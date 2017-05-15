package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DescribeVpcsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by shumeng on 2016/11/11.
 */
public class DescribeVpcs extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeVpcs";
    private int pageSize = 10;
    private int pageNum = 1;
    private boolean isDefault;
    private String vpcId;
    public DescribeVpcs(EcsCommandFactory factory, String [] parameters) {
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
            DescribeVpcsRequest request = new DescribeVpcsRequest();
            request.setVpcId(vpcId);
            request.setIsDefault(isDefault);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            try{
                DescribeVpcsResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Find " + response.getTotalCount() + " vpc.");
                List<DescribeVpcsResponse.Vpc> vpcs = response.getVpcs();
                for (DescribeVpcsResponse.Vpc vpc : vpcs){
                    System.out.printf("VpcId: %-13s  VpcName: %-17s Status: %s\n    Description: %s\n    CidrBlock: " +
                                    "%-16s" +
                                    "    VRouterId: %-16s\n",
                            vpc.getVpcId(),
                            vpc.getVpcName(),
                            vpc.getStatus(),
                            vpc.getDescription(),
                            vpc.getCidrBlock(),
                            vpc.getVRouterId());
                    List<String> vswitchs = vpc.getVSwitchIds();
                    System.out.println("    Vswitch: " + vswitchs.toString());
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
        System.out.println("    describeVpcs 查询VPC");
        System.out.println("SYNOPSIS");
        System.out.println("    describeVpcs [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询指定地域的专有网络列表。此接口支持分页查询，每页的数量默认为 10 条。");
        System.out.println("Options []");
        System.out.println("    -i");
        System.out.println("         --vpcId, VpcID");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小。");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。");
        System.out.println("    -d");
        System.out.println("         --isDefault, 是否为指定Region下的默认VPC，可选值：\n" +
                "         true ——是\n" +
                "         false——否\n" +
                "         当不填写此参数时返回所有指定Region下的VPC。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=9){
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
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        isDefault = Boolean.parseBoolean(parameters[index]);
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
