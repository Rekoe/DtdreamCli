package com.dtdream.cli.ecs.image;

import com.aliyuncs.ecs.model.v20140526.DescribeImagesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeImagesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/10/12.
 */
public class DescribeImages extends Command implements CommandParser {
    private final String COMMAND_NAME = "describeImages";
    private int pageSize = 10;
    private int pageNum = 1;
    private String imageId;
    private String imageName;
    private String snapshotId;
    public DescribeImages(EcsCommandFactory factory, String[] parameters) {
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
        if (parse(parameters)) {
            DescribeImagesRequest request = new DescribeImagesRequest();
            request.setPageNumber(pageNum);
            request.setPageSize(pageSize);
            request.setImageId(imageId);
            request.setImageName(imageName);
            request.setSnapshotId(snapshotId);
            try {
                DescribeImagesResponse response = EcsClient.getInstance().getAcsResponse(request);
                for (DescribeImagesResponse.Image image : response.getImages()) {
                    System.out.printf("imageID: %-52s imageName: %s \ndescription: %s\nsize: %-10s imageVersion: " +
                                    "%-12s usage: %s\n",
                            image.getImageId(),
                            image.getImageName(),
                            image.getDescription(),
                            image.getSize(),
                            image.getImageVersion(),
                            image.getUsage());
                }
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
        System.out.println("    describeImages 查询用户可以使用的镜像列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeImages [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询用户可以使用的镜像列表。\n" +
                "      显示出的镜像资源列表包括用户自定义的镜像，阿里云提供的公共镜像，第三方镜像市场的镜像和其他用户主动共享给您的共享镜像。\n" +
                "      镜像市场的镜像需要先到镜像市场的网站上订阅后才能进行创建ECS实例。\n" +
                "      此接口支持分页查询，查询结果包括可使用的镜像资源的总数和当前页的镜像资源。每页的数量默认为10条。");
        System.out.println("Options");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小。");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。");
        System.out.println("    -i");
        System.out.println("         --imageId, 镜像ID, 最多 100 个Id，用半角逗号字符隔开。(阿里产品api参数格式不一致)");
        System.out.println("    -S");
        System.out.println("         --SnapshotId, 创建镜像的快照ID");
        System.out.println("    -N");
        System.out.println("         --imageName, 镜像名");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 0;
        while (parameters.length > index && parameters.length <= 11) {
            switch (parameters[index]) {
                case "describeImages":
                    index++;
                    break;
                case "-help":
                case "--hlep":
                    advice(null);
                    return false;
                case "-i":
                    index++;
                    if (parameters.length > index) {
                        imageId = parameters[index];
                        index++;
                        break;
                    } else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-N":
                    index++;
                    if (parameters.length > index) {
                        imageName = parameters[index];
                        index++;
                        break;
                    } else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-S":
                    index++;
                    if (parameters.length > index) {
                        snapshotId = parameters[index];
                        index++;
                        break;
                    } else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s":
                    index++;
                    if (parameters.length > index) {
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    } else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n":
                    index++;
                    if (parameters.length > index) {
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    } else {
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
        return true;
    }
}
