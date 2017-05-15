package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DescribeVRoutersRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVRoutersResponse;
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
 * Created by shumeng on 2016/11/11.
 */
public class DescribeVRouters extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeVRouters";
    private String vRouterId;
    private int pageSize = 10;
    private int pageNum = 1;
    private String regionId = Config.getRegion();
    public DescribeVRouters(EcsCommandFactory factory, String [] parameters) {
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
            DescribeVRoutersRequest request = new DescribeVRoutersRequest();
            request.setRegionId(regionId);
            request.setPageNumber(pageNum);
            request.setPageSize(pageSize);
            request.setVRouterId(vRouterId);
            try{
                DescribeVRoutersResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Find " + response.getTotalCount() + " vRouters");
                List<DescribeVRoutersResponse.VRouter> vRouters = response.getVRouters();
                for(DescribeVRoutersResponse.VRouter vRouter : vRouters){
                    System.out.printf("VRouterID: %-13s VRouterName: %-17s Description: %s\n    RouteTableIds: %s\n",
                            vRouter.getVRouterId(),
                            vRouter.getVRouterName(),
                            vRouter.getDescription(),
                            vRouter.getRouteTableIds());
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
        System.out.println("    describeVRouters 查询路由器列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeVRouters [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询指定地域的路由器列表。此接口支持分页查询，每页的数量默认为 10 条。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vRouterId, 要查询的路由器ID");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号");
        System.out.println("    -I");
        System.out.println("         --regionId, 区域ID");
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
                        vRouterId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-I" :
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
        if(StringUtils.isBlank(regionId)){
            System.out.println("参数错误，regionID不能为空。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
