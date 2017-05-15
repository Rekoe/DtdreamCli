package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.CreateSnapshotRequest;
import com.aliyuncs.ecs.model.v20140526.CreateSnapshotResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/2.
 */
public class CreateSnapshot extends Command implements CommandParser {
    private String diskId;
    private String description;
    private String snapshotName;

    public CreateSnapshot(EcsCommandFactory factory, String[] parameters) {
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
            CreateSnapshotRequest request = new CreateSnapshotRequest();
            request.setDiskId(diskId);
            request.setDescription(description);
            request.setSnapshotName(snapshotName);
            String instanceId = EcsUtil.getDisk(diskId).getInstanceId();
            DescribeInstanceStatusResponse.InstanceStatus.Status status = null;
            if (instanceId != null) {
                status = EcsUtil.getInstanceStatus(instanceId).getStatus();
                if (status.equals(DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED) || status.equals
                        (DescribeInstanceStatusResponse.InstanceStatus.Status.RUNNING)) {
                    try {
                        CreateSnapshotResponse response = EcsClient.getInstance().getAcsResponse(request);
                        System.out.println("Snapshot: " + response.getSnapshotId() + " was created!");
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } finally {
                        CommandRecord.getInstance().popLastCommand();
                    }
                } else {
                    System.out.println("ECS 状态不对，只有当ECS状态为：stopped或running状态时才可创建快照");
                }
            } else {
                System.out.println("磁盘：" + diskId + " 未挂载任何ECS");
            }
        }
    }

        @Override
        public void help () {
            System.out.println("NAME");
            System.out.println("    createSnapshot 创建快照");
            System.out.println("SYNOPSIS");
            System.out.println("    createSnapshot [options] ");
            System.out.println("DESCRIPTION");
            System.out.println("    --对指定的磁盘存储设备创建快照。\n" +
                    "      云服务器仅在 Stopped 或 Running 状态下才能创建快照。（但刚创建完成从未启动过的实例不能创建快照）\n" +
                    "      刚创建完成的云服务器系统盘，或刚增加从快照创建的数据盘，由于尚未完成数据的加载，此时创建快照会返回错误。一般来说，系统盘创建完成1" +
                    "      个小时后即可创建快照，数据盘的可创建快照的时间取决于磁盘数据的大小。\n" +
                    "      新增加一块磁盘后，如果实例尚未启动过，新增的这块磁盘不能用于创建快照。\n" +
                    "      如果快照创建没有完成（即进度没有达到 100%），那么这个快照无法用于创建自定义镜像。\n" +
                    "      如果快照创建没有完成（即进度没有达到 100%），那么不能对同一磁盘再次创建快照。\n" +
                    "      快照数量的配额取决于该帐号下拥有的磁盘总数量。最多可创建的快照数量为磁盘数量乘以 6 + 6。\n" +
                    "      如果磁盘在 In_use 状态下，挂载的实例的 OperationLocks 中标记了 \"LockReason\" : \"security\" 的锁定状态时，不能创建快照。\n" +
                    "      磁盘挂载的实例创建后第一次启动前，不能创建快照\n" +
                    "      独立普通云盘创建后，如果没有挂载过，创建快照会出现错误提示。");
            System.out.println("Options [-i]");
            System.out.println("    -i");
            System.out.println("         --diskId, 磁盘编号");
            System.out.println("    -n");
            System.out.println("         --snapshotName 快照的显示名称，[2, 128] 英文或中文字符，必须以大小字母或中文开头，可包含数字，”_”或”-”，且不能以 auto" +
                    " " +
                    "      开头（auto 开头的快照名是预留给自动快照的）。快照的显示名称会显示在控制台中。不能以 http:// 和 https:// 开头。");
            System.out.println("    -d");
            System.out.println("         --description 快照的描述，[2, 256] 个字符。快照的描述会显示在控制台中。不填则为空，默认为空。不能以 http:// 和 " +
                    "https://" +
                    " 开头。");
        }

        @Override
        public boolean parse (String[]parameters){
            displayParameters();
            int index = 1;
            while (parameters.length > index && parameters.length <= 7) {
                switch (parameters[index]) {
                    case "-help":
                    case "--help":
                        help();
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    case "-i":
                        index++;
                        if (parameters.length > index) {
                            diskId = parameters[index];
                            index++;
                            break;
                        } else {
                            System.out.println("参数错误！请输入：createSnapshot -help 查询帮助");
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
                            System.out.println("参数错误！请输入：createSnapshot -help 查询帮助");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    case "-d":
                        index++;
                        if (parameters.length > index) {
                            description = parameters[index];
                            index++;
                            break;
                        } else {
                            System.out.println("参数错误！请输入：createSnapshot -help 查询帮助");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    default:
                        System.out.println("参数错误！请输入：createSnapshot -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                }
            }

            return checkParameters();
        }

        @Override
        public boolean checkParameters () {
            if (diskId == null) {
                System.out.println("参数错误，缺少必要参数！请输入：createSnapshot -help 查询帮助");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
            return true;
        }
    }
