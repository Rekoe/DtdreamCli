package com.dtdream.cli.ecs.image;

import com.aliyuncs.ecs.model.v20140526.CopyImageRequest;
import com.aliyuncs.ecs.model.v20140526.CopyImageResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/10.
 */
public class CopyImage extends Command implements CommandParser{
    private final String COMMAND_NAME = "copyImage";
    private String regionId = Config.getRegion();
    private String imageId;
    private String destinationRegionId;
    private String destinationImageName;
    private String destinationDescription;
    public CopyImage(EcsCommandFactory factory, String [] parameters) {
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
            CopyImageRequest request = new CopyImageRequest();
            request.setImageId(imageId);
            request.setRegionId(regionId);
            request.setDestinationDescription(destinationDescription);
            request.setDestinationImageName(destinationImageName);
            request.setDestinationRegionId(destinationRegionId);
            try{
                CopyImageResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Copy succeed, the new image is " + response.getImageId());
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
        System.out.println("    copyImage 复制镜像");
        System.out.println("SYNOPSIS");
        System.out.println("    copyImage [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --复制镜像可以把用户的某个地域的自定义镜像复制到其他地域进行跨地域一致性部署，在其他地域可以使用复制后的镜像进行创建实例和更换系统盘等操作。\n" +
                "      只有自定义镜像为可用状态才可以进行复制。\n" +
                "      复制的镜像只能在本账号下进行，不能跨账号进行复制\n" +
                "      该镜像没有复制完成，是不能进行删除操作，可以进行取消复制操作。\n");
        System.out.println("Options [-i -r]");
        System.out.println("    -i");
        System.out.println("         --imageId, 镜像ID。");
        System.out.println("    -r");
        System.out.println("         --destinationRegionId, 目的区域ID。");
        System.out.println("    -n");
        System.out.println("         --destinationImageName, 目的镜像名称。");
        System.out.println("    -d");
        System.out.println("         --destinationImageDescription, 目的镜像描述信息。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=9){
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
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        destinationRegionId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        destinationImageName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        destinationDescription = parameters[index];
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
        if(StringUtils.isBlank(imageId) || StringUtils.isBlank(destinationRegionId)){
            System.out.println("参数错误，imageID, 和destinationRegionID不能为空");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
