package com.dtdream.cli.ecs.network;

import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressRequest;
import com.aliyuncs.ecs.model.v20140526.AllocatePublicIpAddressResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by shumeng on 2016/11/10.
 */
public class AllocatePublicIpAddress extends Command implements CommandParser{
    private final String COMMAND_NAME = "allocatePublicIpAddress";
    private String instanceId;
    public AllocatePublicIpAddress(EcsCommandFactory factory, String [] parameters) {
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
            AllocatePublicIpAddressRequest request = new AllocatePublicIpAddressRequest();
            request.setInstanceId(instanceId);
            try{
                AllocatePublicIpAddressResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("A new public Ip address: " + response.getIpAddress() + " was created!");
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
        System.out.println("    allocatePublicIpAddress 分配公网IP");
        System.out.println("SYNOPSIS");
        System.out.println("    allocatePublicIpAddress [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --给一个特定实例分配一个可用公网IP地址。\n" +
                "      实例的状态必须为 Running 或 Stopped 状态，才可以调用此接口。\n" +
                "      分配的 IP 必须在实例启动或重启后才能生效。\n" +
                "      分配的时候只能是 IP，不能是 IP 段。\n" +
                "      目前，一个实例只能分配一个 IP。当调用此接口时，如果实例已经拥有一个公网 IP，将直接返回原 IP 地址。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("    --instanceId, 实例ID");
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
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
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
        if(StringUtils.isBlank(instanceId)){
            System.out.println("参数错误，instanceId不能为空，请输入：allocatePublicIpAddress -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
