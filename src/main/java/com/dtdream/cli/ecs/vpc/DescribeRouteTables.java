package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DescribeRouteTablesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRouteTablesResponse;
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
 * Created by shumeng on 2016/11/14.
 */
public class DescribeRouteTables extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeRouteTables";
    private String routerType;
    private String routerId;
    private String vRouterId;
    private String routeTableId;
    private int pageSize = 50;
    private int pageNum = 1;
    public DescribeRouteTables(EcsCommandFactory factory, String [] parameters) {
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
            DescribeRouteTablesRequest request = new DescribeRouteTablesRequest();
            request.setVRouterId(vRouterId);
            request.setRouterId(routerId);
            request.setRouteTableId(routeTableId);
            request.setRouterType(routerType);
            request.setPageNumber(pageNum);
            request.setPageSize(pageSize);
            try{
                DescribeRouteTablesResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeRouteTablesResponse.RouteTable> routeTables = response.getRouteTables();
                for(DescribeRouteTablesResponse.RouteTable routeTable : routeTables){
                    System.out.printf("VRouterID: %12S  RouteTableType: %-7s  RouteTableId:%s\n    Route:%s",
                            routeTable.getVRouterId(),
                            routeTable.getRouteTableType(),
                            routeTable.getRouteTableId(),
                            routeTable.getRouteEntrys().toString());
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
        System.out.println("    describeRouteTables 查询路由表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeRouteTables [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询用户名下路由表的列表。支持查询专有网络中的路由器（VRouter）和高速通道中的边界路由器（VBR）上的路由表（RouteTable）");
        System.out.println("Options []");
        System.out.println("    -t");
        System.out.println("         --routerType, 所属的路由器类型。可选值：VRouter，VBR");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页查询时设置的每页行数，最大值50行，默认为10");
        System.out.println("    -n");
        System.out.println("         --pageNum, 实例状态列表的页码，起始值为1，默认值为1。");
        System.out.println("    -i");
        System.out.println("         --routerId, 所属的路由器ID。VRouterID或VBRID");
        System.out.println("    -v");
        System.out.println("         --VRouterId, 所属专有网络路由器ID。指定VRouterId自动指定RouterType为VRouter。");
        System.out.println("    -r");
        System.out.println("         --RouteTableId, RouteTableId");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=13){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        routerType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        routerId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        vRouterId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        routeTableId = parameters[index];
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
                        CommandRecord.getInstance().popLastCommand();
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
