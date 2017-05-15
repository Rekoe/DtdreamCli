package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.ModifyVRouterAttributeRequest;
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
public class ModifyVRouterAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "modifyVRouterAttribute";
    private String vRouterId;
    private String vRouterName;
    private String description;
    public ModifyVRouterAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifyVRouterAttributeRequest request = new ModifyVRouterAttributeRequest();
            request.setVRouterId(vRouterId);
            request.setDescription(description);
            request.setVRouterName(vRouterName);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("VRouter: " + vRouterId + "'s attribute was modified!");
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
        System.out.println("    modifyVRouterAttribute 修改路由器属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyVRouterAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改指定 VRouter 的属性");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vRouterId, 路由器ID");
        System.out.println("    -d");
        System.out.println("         --description, 路由器描述信息");
        System.out.println("    -n");
        System.out.println("         --vRouterName, 路由器名字");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=7){
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
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        vRouterName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
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
        if(StringUtils.isBlank(vRouterId)){
            System.out.println("参数错误，vRouterID不能为空。");
        }
        return true;
    }
}
