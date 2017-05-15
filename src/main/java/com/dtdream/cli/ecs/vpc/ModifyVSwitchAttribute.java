package com.dtdream.cli.ecs.vpc;

import com.aliyuncs.ecs.model.v20140526.ModifyVSwitchAttributeRequest;
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
public class ModifyVSwitchAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "modifyVSwitchAttribute";
    private String vSwitchId;
    private String vSwitchName;
    private String description;
    public ModifyVSwitchAttribute(EcsCommandFactory factory, String [] parameters) {
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
            ModifyVSwitchAttributeRequest request = new ModifyVSwitchAttributeRequest();
            request.setVSwitchId(vSwitchId);
            request.setVSwitchName(vSwitchName);
            request.setDescription(description);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Modify VSwitch: " + vSwitchId + " succeed!");
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
        System.out.println("    modifyVSwitchAttribute 修改交换机属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyVSwitchAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改指定VSwitch的属性");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --vSwitchId, VSwitchID");
        System.out.println("    -n");
        System.out.println("         --vSwitchName");
        System.out.println("    -d");
        System.out.println("         --description, 交换机描述信息");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<= 7){
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
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        vSwitchName = parameters[index];
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
        if(StringUtils.isBlank(vSwitchId)){
            System.out.println("参数错误，VswitchId不能为空。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
