package com.dtdream.cli.ecs.image;

import com.aliyuncs.ecs.model.v20140526.DeleteImageRequest;
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
public class DeleteImage extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteImage";
    private String imageId;
    public DeleteImage(EcsCommandFactory factory, String [] parameters) {
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
            DeleteImageRequest request = new DeleteImageRequest();
            request.setImageId(imageId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Image: " + imageId + " was deleted!!");
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
        System.out.println("    deleteImage 删除用户自定义镜像");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteImage [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定的用户自定义镜像。镜像删除之后将不能再用于创建、重置ECS实例。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("    --imageId, 镜像ID, 最多 100 个Id，用半角逗号字符隔开。(阿里产品api参数格式不一致)");
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
                        imageId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                    }
                    default:
                        advice(COMMAND_NAME);
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(imageId)){
            System.out.println("参数错误，imageID不能为空。请输入：deleteImage -help查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
