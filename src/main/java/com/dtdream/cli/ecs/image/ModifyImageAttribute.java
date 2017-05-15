package com.dtdream.cli.ecs.image;

import com.aliyuncs.ecs.model.v20140526.ModifyImageAttributeRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by shumeng on 2016/11/10.
 */
public class ModifyImageAttribute extends Command implements CommandParser{
    private final String COMMAND_NAME = "modifyImageAttribute";
    private String imageId;
    private String imageName;
    private String description;
    public ModifyImageAttribute(EcsCommandFactory factory, String [] parameters) {
        super(factory);
        this.parameters = parameters;
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
                        imageId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        imageName = parameters[index];
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

            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(imageId == null){
            System.out.println("参数错误，imageId 不能为空！！！");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
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
            ModifyImageAttributeRequest request = new ModifyImageAttributeRequest();
            request.setImageName(imageName);
            request.setImageId(imageId);
            request.setDescription(description);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Modify image: " + imageId + " succeed!");
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
        System.out.println("    modifyImageAttribute 修改镜像属性");
        System.out.println("SYNOPSIS");
        System.out.println("    modifyImageAttribute [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --修改自定义镜像的名称和描述。\n");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例ID。");
        System.out.println("    -n");
        System.out.println("         --imageName, 镜像名称。");
        System.out.println("    -d");
        System.out.println("         --description, 镜像描述信息。");
    }
}
