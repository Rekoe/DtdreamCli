package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.ResizeDiskRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/1.
 */
public class ResizeDisk extends Command implements CommandParser {
    private int newSize;
    private String diskId;

    public ResizeDisk(EcsCommandFactory factory, String [] parameters) {
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
            ResizeDiskRequest request = new ResizeDiskRequest();
            request.setDiskId(diskId);
            request.setNewSize(newSize);
            try{
                EcsClient.getInstance().getAcsResponse(request);
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
        System.out.println("    resizeDisk 扩容磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    resizeDisk [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --当前仅支持对数据盘的扩容操作。\n" +
                "只有数据盘类型为普通云盘、高效云盘、SSD 云盘，才可以进行扩容操作。\n" +
                "挂载在实例上的数据盘，只有实例为 Running 或者 Stopped 状态时，才可以进行扩容操作。\n" +
                "磁盘上如果正在执行打快照的操作，则此时不允许进行扩容操作。\n" +
                "挂载在实例上的磁盘，在进行扩容操作后，需要在控制台或者使用 Open API 重启实例才能使扩容生效。\n" +
                "扩容仅做磁盘容量的扩大，不做分区和文件系统的扩大，扩容完成后，用户还需要手动分配存储空间。");
        System.out.println("Options [-i -s]");
        System.out.println("    -i");
        System.out.println("         --diskId, 磁盘编号");
        System.out.println("    -s");
        System.out.println("         --newSize, 希望扩容到的磁盘大小。以 GB 为单位，取值范围为 (Cloud：5 ~ 2000)");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=5){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        diskId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：resizeDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        newSize = Integer.parseInt(parameters[index]);
                        if(newSize<5 || newSize>2000){
                            System.out.println("磁盘大小不在合法范围！");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：resizeDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：resizeDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(diskId==null || newSize==0){
            System.out.println("参数错误！请输入：resizeDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
