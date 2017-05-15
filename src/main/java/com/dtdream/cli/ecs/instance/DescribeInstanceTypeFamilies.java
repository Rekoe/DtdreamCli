package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypeFamiliesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypeFamiliesResponse;
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
 * Created by shumeng on 2016/11/14.
 */
public class DescribeInstanceTypeFamilies extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeInstanceTypeFamilies";
    private String regionId = Config.getRegion();
    private String generation;
    public DescribeInstanceTypeFamilies(EcsCommandFactory factory, String [] parameters) {
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
            DescribeInstanceTypeFamiliesRequest request = new DescribeInstanceTypeFamiliesRequest();
            request.setGeneration(generation);
            request.setRegionId(regionId);
            try{
                DescribeInstanceTypeFamiliesResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeInstanceTypeFamiliesResponse.InstanceTypeFamily> instanceTypeFamilies = response
                        .getInstanceTypeFamilies();
                 for(DescribeInstanceTypeFamiliesResponse.InstanceTypeFamily instanceTypeFamily :
                         instanceTypeFamilies){
                     System.out.printf("InstanceTypeFamilyID: %-12s  Generation: %s\n",
                             instanceTypeFamily.getInstanceTypeFamilyId(),
                             instanceTypeFamily.getGeneration());
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
        System.out.println("    describeInstanceTypeFamilies");
        System.out.println("SYNOPSIS");
        System.out.println("    describeInstanceTypeFamilies [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询实例规格族信息");
        System.out.println("Options [-i]");
        System.out.println("    -i ");
        System.out.println("         --regionId, 地域ID");
        System.out.println("    -g");
        System.out.println("         --generation, 实例系列。可选值：[ecs-1: 系列 I, ecs-2: 系列 II]");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=5){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        regionId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-g" :
                    index++;
                    if(parameters.length > index){
                        generation = parameters[index];
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
