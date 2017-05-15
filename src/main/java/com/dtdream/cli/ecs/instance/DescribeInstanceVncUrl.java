package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceVncUrlRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceVncUrlResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/10/27.
 */
public class DescribeInstanceVncUrl extends Command implements CommandParser {
    private String instanceId;
//    private String regionId;
    public DescribeInstanceVncUrl(EcsCommandFactory factory, String [] parameters) {
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
            DescribeInstanceVncUrlRequest request = new DescribeInstanceVncUrlRequest();
            request.setInstanceId(instanceId);
            request.setRegionId(Config.getRegion());
            try{
                DescribeInstanceVncUrlResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("终端地址为：" + response.getVncUrl());
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
        System.out.println("    describeInstanceVncUrl 查询 ECS 的 Web 管理终端地址\n" +
                "\n" +
                "管理终端地址的有效期为 15 秒。如果查询地址后 15 秒内不连接，URL 地址会失效，需要重新查询\n" +
                "单个管理终端连接的 keepalive 时间为 60 秒，60 秒内用户在管理终端窗口没有交互操作，会自动断开连接\n" +
                "如果连接中断，每分钟内重新连接的次数不能超过 30 次");
        System.out.println("SYNOPSIS");
        System.out.println("    describeInstanceVncUrl [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 停止ECS实例");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'describeInstanceVncUrl -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }
                    default:
                        System.out.println("参数错误");
                        System.out.println("请输入 'describeInstanceVncUrl -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }
        if(instanceId == null){
            System.out.println("参数错误");
            System.out.println("请输入 'describeInstanceVncUrl -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
