package com.dtdream.cli.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeDisksRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeDisksResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by thomugo on 2016/11/1.
 */
public class DescribeDisks extends Command implements CommandParser {
    private int pageNum = 1;
    private int pageSize = 10;
    private String instanceId;
    private String category;
    private String diskIds;
    public DescribeDisks(EcsCommandFactory factory, String [] parameters) {
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
            DescribeDisksRequest request = new DescribeDisksRequest();
            request.setInstanceId(instanceId);
            request.setDiskIds(diskIds);
            request.setCategory(category);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            try{
                DescribeDisksResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("There are totally: " + response.getTotalCount() + "disks.");
                List<DescribeDisksResponse.Disk> disks = response.getDisks();
                for(DescribeDisksResponse.Disk disk : disks){
                    System.out.printf("DiskID: %s  DiskName: %-15s ECSInstance: %s  Size: %s\n\tDescription: %s \n\tCategory: %-16s  Status: %-10s " +
                                    "DeleteWithInstance: %-5s  DeleteAutoSnapshot: %-5s EnableAutoSnapshot: %-5s\n",
                            disk.getDiskId(),
                            disk.getDiskName(),
                            disk.getInstanceId(),
                            disk.getSize(),
                            disk.getDescription(),
                            disk.getCategory(),
                            disk.getStatus(),
                            disk.getDeleteWithInstance(),
                            disk.getDeleteAutoSnapshot(),
                            disk.getEnableAutoSnapshot());
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
        System.out.println("    describeDisks 查询磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    describeDisks [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询磁盘。");
        System.out.println("Options");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小。");
        System.out.println("    -i");
        System.out.println("         --diskIds, 磁盘ID。最多 100 个 Id，注意只能用半角逗号字符隔开不能出现空格。");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。");
        System.out.println("    -I");
        System.out.println("         --instanceId, 实例ID。");
        System.out.println("    -c");
        System.out.println("         --category, 磁盘类别。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 0;
        while (parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "describeDisks" :
                    index++;
                    break;
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        category = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        diskIds = parameters[index];
                        diskIds = EcsUtil.toJsonStr(diskIds);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-I" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误：请输入 describeDisks -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
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
