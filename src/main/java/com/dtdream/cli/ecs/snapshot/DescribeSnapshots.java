package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.DescribeSnapshotsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSnapshotsResponse;
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
 * Created by thomugo on 2016/11/2.
 */
public class DescribeSnapshots extends Command implements CommandParser {
    private int pageNum = 1;
    private int pageSize = 10;
    private String instanceId;
    private String diskId;
    private String snapshotName;
    private String snapshotIds;

    public DescribeSnapshots(EcsCommandFactory factory, String[] parameters) {
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
            DescribeSnapshotsRequest request = new DescribeSnapshotsRequest();
            request.setInstanceId(instanceId);
            request.setDiskId(diskId);
            request.setSnapshotName(snapshotName);
            request.setSnapshotIds(snapshotIds);
            request.setPageNumber(pageNum);
            request.setPageSize(pageSize);
            try {
                DescribeSnapshotsResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeSnapshotsResponse.Snapshot> snapshots = response.getSnapshots();
                for (DescribeSnapshotsResponse.Snapshot snapshot : snapshots) {
                    System.out.printf("SnapshotId: %s SnapshotName: %-30s  DiskID: %-12s  Status: %-12s Usage: %s\n",
                            snapshot.getSnapshotId(),
                            snapshot.getSnapshotName(),
                            snapshot.getSourceDiskId(),
                            snapshot.getStatus(),
                            snapshot.getUsage());
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
        System.out.println("    describeSnapshots 查询快照列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeSnapshots [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询快照列表");
        System.out.println("Options ");
        System.out.println("    -i");
        System.out.println("         --instanceId, ECS实例ID");
        System.out.println("    -I");
        System.out.println("         --diskId, 磁盘ID");
        System.out.println("    -n");
        System.out.println("         --snapshotName, 快照名称");
        System.out.println("    -S");
        System.out.println("         --snapshotIds, 一个带有格式的，多个ID用半角逗号字符隔开,中间不能出项空格");
        System.out.println("    -N");
        System.out.println("         --pageNum, 分页号");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小 ");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length > index && parameters.length <= 13) {
            switch (parameters[index]) {
                case "-help":
                case "--help":
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i":
                    index++;
                    if (parameters.length > index) {
                        instanceId = parameters[index];
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-I":
                    index++;
                    if (parameters.length > index) {
                        diskId = parameters[index];
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n":
                    index++;
                    if (parameters.length > index) {
                        snapshotName = parameters[index];
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-S":
                    index++;
                    if (parameters.length > index) {
                        snapshotIds = parameters[index];
                        snapshotIds = EcsUtil.toJsonStr(snapshotIds);
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-N":
                    index++;
                    if (parameters.length > index) {
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s":
                    index++;
                    if (parameters.length > index) {
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    } else {
                        System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误！请输入：describeSnapshots -help 查询帮助");
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
