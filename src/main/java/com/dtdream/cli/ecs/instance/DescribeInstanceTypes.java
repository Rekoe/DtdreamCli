package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
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
public class DescribeInstanceTypes extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeInstanceTypes";
    private String instanceTypeFamily;

    public DescribeInstanceTypes(EcsCommandFactory factory, String [] parameters) {
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
            DescribeInstanceTypesRequest request = new DescribeInstanceTypesRequest();
            request.setInstanceTypeFamily(instanceTypeFamily);
            try{
                DescribeInstanceTypesResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeInstanceTypesResponse.InstanceType> instanceTypes = response.getInstanceTypes();
                for(DescribeInstanceTypesResponse.InstanceType instanceType : instanceTypes){
                    System.out.printf("InstanceTypeFamily: %-10s  InstanceTypeID: %-7s\n    CpuCoreCount: %d核  " +
                                    "MemorySize: %dGB\n",
                            instanceType.getInstanceTypeFamily(),
                            instanceType.getInstanceTypeId(),
                            instanceType.getCpuCoreCount(),
                            instanceType.getMemorySize().intValue());
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
        System.out.println("    describeInstanceType");
        System.out.println("SYNOPSIS");
        System.out.println("    describeInstanceStatus [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询 ECS 所提供的实例资源规格列表。\n");
        System.out.println("Options []");
        System.out.println("    -f ");
        System.out.println("         --instanceTypeFamily, 实例规格族。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-f" :
                    index++;
                    if(parameters.length > index){
                        instanceTypeFamily = parameters[index];
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
