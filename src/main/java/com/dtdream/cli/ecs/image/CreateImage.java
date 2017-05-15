package com.dtdream.cli.ecs.image;

import com.aliyuncs.ecs.model.v20140526.CreateImageRequest;
import com.aliyuncs.ecs.model.v20140526.CreateImageResponse;
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
public class CreateImage extends Command implements CommandParser{
    private final String COMMAND_NAME = "createImage";
    private String imageName;
    private String imageVersion;
    private String description;
    private String instanceId;
    private String snapshotId;
    public CreateImage(EcsCommandFactory factory, String [] parameters) {
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
            CreateImageRequest request = new CreateImageRequest();
            request.setImageName(imageName);
            request.setSnapshotId(snapshotId);
            request.setInstanceId(instanceId);
            request.setDescription(description);
            request.setImageVersion(imageVersion);
            try{
                CreateImageResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Image: " + response.getImageId() + " was created!");
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
        System.out.println("    createImage 创建自定义镜像");
        System.out.println("SYNOPSIS");
        System.out.println("    createImage [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --创建自定义镜像：\n" +
                "    1.如果您只需要对实例的系统盘创建自定义镜像，那么只需要指定一个快照Id进行创建自定义镜像。\n" +
                "      只有系统盘的快照可以用于创建自定义镜像。\n" +
                "      只有达到完成状态（进度为100%）的快照可以用于创建自定义镜像。\n" +
                "    2.如果您需要对整个ECS实例做个模板，那么可以指定实例Id进行创建自定义镜像。(阿里专有云暂不支持)\n" +
                "      指定的实例的状态只能使运行中或者已停止。\n" +
                "      会给该实例的每块磁盘新增一个快照。\n" +
                "      需要等待快照创建完成后，镜像的状态才能变成可用状态，需要您等待一段时间。\n"
                //"    3.如果您需要对多个快照进行组合成一个镜像模板，那么可以指定DiskDeviceMappings进行创建自定义镜像。\n" +
                //"      只能指定一个系统盘快照，指定系统盘快照的device必须为：/dev/xvda\n" +
                //"      可以不指定快照的Id，那么会创建一个指定大小的没有任何数据的空盘。\n" +
                //"      只有达到完成状态（进度为100%）的快照可以用于创建自定义镜像。"
        );
        System.out.println("Options [-i | -s]");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例ID。");
        System.out.println("    -s");
        System.out.println("         --snapshotId, 快照ID。");
        System.out.println("    -n");
        System.out.println("         --imageName, 镜像名称。");
        System.out.println("    -v");
        System.out.println("         --imageVersion, 镜像版本号。");
        System.out.println("    -d");
        System.out.println("         --description, 镜像描述信息。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    break;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        snapshotId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        imageName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        imageVersion = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
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
        if(StringUtils.isEmpty(instanceId) && StringUtils.isEmpty(snapshotId)){
            System.out.println("参数错误，缺少必要参数。请输入：creatImage -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
