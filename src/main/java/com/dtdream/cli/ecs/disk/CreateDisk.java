package com.dtdream.cli.ecs.disk;

import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.ecs.util.RegionUtil;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by thomugo on 2016/11/1.
 */
public class CreateDisk extends Command implements CommandParser{
    private String diskName;
    private int size;
    private String category;
    private String snapshotId;
    private String zoneId;
    private CreateDiskRequest request;
    public CreateDisk(EcsCommandFactory factory, String [] parameters) {
        super(factory);
        this.parameters = parameters;
        request = new CreateDiskRequest();
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
            try{
                CreateDiskResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("A new disk: " + response.getDiskId() + " was created。");
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
        System.out.println("    createDisk 创建磁盘");
        System.out.println("SYNOPSIS");
        System.out.println("    createDisk [options -c -z -i|-s] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 创建可卸载云盘的数据盘，包括普通云盘、高效云盘和SSD云盘，系统盘的快照不能用来创建数据盘。\n " +
                "创建磁盘时，默认在删除磁盘时删除自动快照，即 DeleteAutoSnapshot=true，可以通过 修改磁盘属性 修改该参数。\n " +
                "通过该接口创建的盘Portable属性为true。收费方式为按量付费。\n " +
                "请求参数中 Size 和 SnapshotId 必须选择其中一项来指定磁盘的大小或使用快照创建磁盘。。");
        System.out.println("Options [-c -z -s|-i]");
        System.out.println("    -n");
        System.out.println("         --diskName, 磁盘名称。名称字符串长度应小于17字节");
        System.out.println("    -c");
        System.out.println("         --diskCategory, 必填参数磁盘类别。");
        System.out.println("    -s");
        System.out.println("         --diskSize, 磁盘大小。容量大小，以GB为单位：\n" +
                           "           cloud：5 ~ 2000\n" +
                           "           cloud_efficiency：20 ~ 32768\n" +
                           "           cloud_ssd：20 ~ 32768\n" +
                           "           指定该参数后，Size大小必须 ≥ 指定快照 SnapshotId 的大小。");
        System.out.println("    -i");
        System.out.println("         --snapshotId, 创建数据盘使用的快照 。");
        System.out.println("    -z");
        System.out.println("         --zoneId, 必填参数可用区ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        category = parameters[index];
                        request.setDiskCategory(category);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        snapshotId = parameters[index];
                        request.setSnapshotId(snapshotId);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        diskName = parameters[index];
                        request.setDiskName(diskName);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        size = Integer.parseInt(parameters[index]);
                        request.setSize(size);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-z" :
                    index++;
                    if(parameters.length > index){
                        zoneId = parameters[index];
                        request.setZoneId(zoneId);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：createDisk -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(zoneId==null || category==null){
            System.out.println("参数错误，缺少必要参数！请输入：createDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        List<DescribeZonesResponse.Zone> zones = RegionUtil.getZones(Config.getRegion());
        boolean contain = false;
        for (DescribeZonesResponse.Zone zone : zones){
            if(zone.getZoneId().equals(zoneId)){
                contain = true;
            }
        }
        if(!contain){
            System.out.println("ZoneID 错误，不存在该可用区");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        List<String> diskCategories = RegionUtil.getDiskCategories(Config.getRegion(), zoneId);
        if(!diskCategories.contains(category)){
            System.out.println("Category 错误， 可用区：" + zoneId + " 中不存在该磁盘类别");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(size==0 && snapshotId==null){
            System.out.println("参数错误，缺少必要参数！请求参数中 Size 和 SnapshotId 必须选择其中一项来指定磁盘的大小或使用快照创建磁盘。\n请输入：createDisk -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }

        return true;
    }

    /**
     * Created by thomugo on 2016/11/1.
     */
    public static class DescribeDisks extends Command implements CommandParser {
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
                        System.out.printf("DiskID: %s  DiskName: %-15s ECSInstance: %-17s  Size: %s\n\tDescription: %s \n\tCategory: %-16s  Status: %-10s " +
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
}
