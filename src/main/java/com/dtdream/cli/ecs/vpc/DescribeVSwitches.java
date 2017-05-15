package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/11/14.
 */
public class DescribeVSwitches extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeVSwitches";
    private String vpcId;
    private String zoneId;
    private String vSwitchId;
    private int pageSize = 10;
    private int pageNum = 1;
    private boolean isDefault = false;
    public DescribeVSwitches(EcsCommandFactory factory, String [] parameters) {
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
            DescribeVSwitchesRequest request = new DescribeVSwitchesRequest();
            request.setVpcId(vpcId);
            request.setVSwitchId(vSwitchId);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            request.setZoneId(zoneId);
            request.setIsDefault(isDefault);
            try{
                DescribeVSwitchesResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeVSwitchesResponse.VSwitch> vSwitches = response.getVSwitches();
                if(response.getTotalCount() > 0){
                    System.out.println("VpcID: " + vpcId);
                    for (DescribeVSwitchesResponse.VSwitch vSwitch : vSwitches){
                        System.out.printf("vSwitchID: %-12s  vSwitchName: %-17s  Status: %s\n    zoneID: %-15s  " +
                                        "AvailableIpAddressCount: %d\n    CidrBlock: %s\n    Description: %s\n",
                                vSwitch.getVSwitchId(),
                                vSwitch.getVSwitchName(),
                                vSwitch.getStatus(),
                                vSwitch.getZoneId(),
                                vSwitch.getAvailableIpAddressCount(),
                                vSwitch.getCidrBlock(),
                                vSwitch.getDescription());
                    }
                }else{
                    System.out.println("Find 0 VSwitch");
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
        System.out.println("    describeVpcs 查询VSwitch");
        System.out.println("SYNOPSIS");
        System.out.println("    describeVpcs [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询用户的 VSwitch 列表。此接口支持分页查询，每页的数量默认为 10 条。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vpcId, VpcID");
        System.out.println("    -I");
        System.out.println("         --vSwitchId, vSwitchID");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小。");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。");
        System.out.println("    -z");
        System.out.println("         --zoneId, 可用区ID。");
        System.out.println("    -d");
        System.out.println("         --isDefault, 是否为指定Region下的默认交换机，可选值：\n" +
                "         true ——是\n" +
                "         false——否\n" +
                "         当不填写此参数时返回所有交换机ID。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=13){
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
                case "-I" :
                    index++;
                    if(parameters.length > index){
                        vSwitchId = parameters[index];
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
        if(StringUtils.isBlank(vpcId)){
            System.out.println("参数错误，vpcID不能为空，请输入：describeVSwitches -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
