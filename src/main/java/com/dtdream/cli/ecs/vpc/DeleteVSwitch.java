package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DeleteVSwitchRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/14.
 */
public class DeleteVSwitch extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteVSwitch";
    private String vSwitchId;
    public DeleteVSwitch(EcsCommandFactory factory, String [] parameters) {
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
            DeleteVSwitchRequest request = new DeleteVSwitchRequest();
            request.setVSwitchId(vSwitchId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Delete VSwitch: " + vSwitchId + " succeed!");
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
        System.out.println("    deleteVSwitch 删除指定的交换机");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteVSwitch [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定的VSwitch\n" +
                "      只允许删除Available状态下的VSwitch\n" +
                "      删除VSwitch之前，需要先释放或移走VSwitch下的所有产品实例，比如ECS实例\n" +
                "      指定RouteTable所在的VPC正在创建/删除VSwitch或正在创建/删除RouteEntry操作时，无法进行删除VSwitch操作\n" +
                "      删除VSwitch之前，需要先释放该VSwitch下所有的HaVip实例");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vSwitchId");
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
                        vSwitchId = parameters[index];
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
        if(StringUtils.isBlank(vSwitchId)){
            System.out.println("参数错误，缺少必要参数：VSwitchID");
            return false;
        }
        return true;
    }
}
