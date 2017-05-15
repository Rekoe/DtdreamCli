package com.dtdream.cli.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
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
 * Created by thomugo on 2016/11/3.
 */
public class DescribeRegions extends Command implements CommandParser {
    public DescribeRegions(EcsCommandFactory factory, String [] parameters) {
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
            DescribeRegionsRequest request = new DescribeRegionsRequest();
            try{
                DescribeRegionsResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeRegionsResponse.Region> regions = response.getRegions();
                for(DescribeRegionsResponse.Region region : regions){
                    System.out.printf("RegionID: %s   LocalName: %s \n", region.getRegionId(), region.getLocalName());
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
        System.out.println("    describeRegions 查询可用地域列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeRegions [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询可用地域列表");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        switch (parameters.length){
            case 1 :
                return checkParameters();
            case 2 :
                if(parameters[1].equals("-help") || parameters[1].equals("--help")){
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                }
            default :
                System.out.printf("参数错误！请输入: describeRegions -help 查询帮助。");
                CommandRecord.getInstance().popLastCommand();
                return false;
        }
    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
