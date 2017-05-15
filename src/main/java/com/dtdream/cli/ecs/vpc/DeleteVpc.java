package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.DeleteVpcRequest;
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
 * Created by shumeng on 2016/11/11.
 */
public class DeleteVpc extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteVpc";
    private String vpcId;
    public DeleteVpc(EcsCommandFactory factory, String [] parameters) {
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
            DeleteVpcRequest request = new DeleteVpcRequest();
            request.setVpcId(vpcId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("VPC: " + vpcId + " was deleted!");
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
        System.out.println("    deleteVpc 删除VPC");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteVpc [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定的 VPC\n" +
                "      删除 VPC 之前，需要先释放或移走 VPC 内的所有资源(包括 VSwitch，ECS Instance，Connection，HaVip 等)\n" +
                "      只有 Available 状态的 VPC 可以删除");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vpcId, VpcID");
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
                        vpcId = parameters[index];
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
            System.out.println("参数错误，vpcID 不能为空，请输入： deleteVpc -help 查询帮助！");
            return false;
        }
        return true;
    }
}
