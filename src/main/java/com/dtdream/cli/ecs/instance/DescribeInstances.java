package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/10/13.
 */
public class DescribeInstances extends Command implements CommandParser {
    private final String COMMAND_NAME = "describeInstances";
    private String instanceName;
    private String instanceId;
    private int pageSize = 10;
    private int pageNum = 1;

    public DescribeInstances(EcsCommandFactory factory, String [] parameters) {
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
            DescribeInstancesRequest request = new DescribeInstancesRequest();
            request.setInstanceName(instanceName);
            request.setInstanceIds(instanceId);
            request.setPageNumber(pageNum);
            request.setPageSize(pageSize);
            try {
                DescribeInstancesResponse response = EcsClient.getInstance().getAcsResponse(request);
                if(response.getTotalCount() >= 0){
                    switch (response.getTotalCount()){
                        case 0 :
                            System.out.println("there are no instances were created. ");
                            break;
                        case 1:
                            System.out.println("there is only one instance: ");
                            break;
                        default :
                            System.out.println("there are totaly " + response.getTotalCount() + " instances: ");
                            break;
                    }
                }
                for(DescribeInstancesResponse.Instance instance : response.getInstances()){
                    System.out.printf("instanceName: %-12s instanceId: %-12s instanceStatus: %s \n",
                            instance.getInstanceName(),
                            instance.getInstanceId(),
                            instance.getStatus()
                    );
                }
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    describeInstances");
        System.out.println("SYNOPSIS");
        System.out.println("    describeInstances [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询ECS实例详细信息");
        System.out.println("Options");
        System.out.println("    -i ");
        System.out.println("         --instanceId, ECS实例ID");
        System.out.println("    -n");
        System.out.println("         --instanceName, 实例的显示名称。");
        System.out.println("    -S");
        System.out.println("         --pageSize, 分页大小默认值为10");
        System.out.println("    -N");
        System.out.println("         --pageNum, 分页号默认值为1");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=45){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        instanceName = parameters[index];
                        index++;
                        break;
                    }else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        instanceId = EcsUtil.toJsonStr(instanceId);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-S" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-N" :
                    index++;
                    if(parameters.length > index){
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                default :
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
