package com.dtdream.cli.ecs.util;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.*;
import com.dtdream.cli.ecs.disk.*;
import com.dtdream.cli.ecs.image.*;
import com.dtdream.cli.ecs.instance.*;
import com.dtdream.cli.ecs.monitor.DescribeInstanceMonitorData;
import com.dtdream.cli.ecs.network.AllocatePublicIpAddress;
import com.dtdream.cli.ecs.securitygroup.*;
import com.dtdream.cli.ecs.snapshot.*;
import com.dtdream.cli.ecs.vpc.*;

/**
 * Created by thomugo on 2016/10/12.
 */
public class EcsCommandFactory implements ICommandFactory{
    @Override
    public Command getCommand(String[] var) {
        switch (var[0]) {
            case "-help":
            case "--help": return new Help(this);
            case "describeInstanceTypes" : return new DescribeInstanceTypes(this, var);
            case "describeInstanceTypeFamilies" : return new DescribeInstanceTypeFamilies(this, var);
            case "describeTasks" : return new DescribeTasks(this, var);
            //地域相关
            case "describeRegions" : return new DescribeRegions(this, var);
            case "describeZones" : return new DescribeZones(this, var);
            //ECS实例相关
            case "describeInstances" : return new DescribeInstances(this, var);
            case "createInstance" : return new CreateInstance(this, var);
            case "startInstance" : return new StartInstance(this, var);
            case "deleteInstance" : return new DeleteInstance(this, var);
            case "stopInstance" : return new StopInstance(this, var);
            case "rebootInstance" : return new RebootInstance(this, var);
            case "describeInstanceStatus" : return new DescribeInstanceStatus(this, var);
            case "modifyInstanceAttribute" : return new ModifyInstanceAttribute(this, var);
            case "modifyInstanceVpcAttribute" : return new ModifyInstanceVpcAttribute(this, var);
            case "joinSecurityGroup" : return new JoinSecurityGroup(this, var);
            case "leaveSecurityGroup" : return new LeaveSecurityGroup(this, var);
            case "describeInstanceVncUrl" : return new DescribeInstanceVncUrl(this, var);
            // disk
            case "describeDisks" : return  new CreateDisk.DescribeDisks(this, var);
            case "createDisk" : return new CreateDisk(this, var);
            case "attachDisk" : return new AttachDisk(this, var);
            case "detachDisk" : return new DetachDisk(this, var);
            case "modifyDiskAttribute" : return new ModifyDiskAttribute(this, var);
            case "deleteDisk" : return new DeleteDisk(this, var);
            case "reInitDisk" : return new ReInitDisk(this, var);
            case "resetDisk" : return new ResetDisk(this, var);     //待验证
            //阿里专有云暂不支持此操作
            case "replaceSystemDisk" : return new ReplaceSystemDisk(this, var);
            case "resizeDisk" : return new ResizeDisk(this, var);
            //snapshot
            case "createSnapshot" : return new CreateSnapshot(this, var);
            case "deleteSnapshot" : return new DeleteSnapshot(this, var);
            case "describeSnapshots" : return new DescribeSnapshots(this, var);
            case "createAutoSnapshotPolicy" : return new CreateAutoSnapshotPolicy(this, var);
            case "deleteAutoSnapshotPolicy" : return new DeleteAutoSnapshotPolicy(this, var);
            case "modifyAutoSnapshotPolicy" : return new ModifyAutoSnapshotPolicyEx(this, var);
            case "describeAutoSnapshotPolicy" : return new DescribeAutoSnapshotPolicyEx(this, var);
            case "applyAutoSnapshotPolicy" : return new ApplyAutoSnapshotPolicy(this, var);
            case "cancelAutoSnapshotPolicy" : return new CancelAutoSnapshotPolicy(this, var);
            //image
            case "describeImages" : return new DescribeImages(this, var);
            case "createImage" : return new CreateImage(this, var);
            case "modifyImageAttribute" : return new ModifyImageAttribute(this, var);
            //阿里专有云暂不支持
            case "copyImage" : return new CopyImage(this, var);
            case "deleteImage" : return new DeleteImage(this, var);
            //网络相关
            case "allocatePublicIpAddress" : return new AllocatePublicIpAddress(this, var);
            //安全组相关 (未完成)
            case "createSecurityGroup" : return new CreateSecurityGroup(this, var);
            case "describeSecurityGroups" : return new DescribeSecurityGroups(this, var);
            case "describeSecurityGroupAttribute" : return new DescribeSecurityGroupAttribute(this, var);
            case "modifySecurityGroupAttribute" : return new ModifySecurityGroupAttribute(this, var);
            case "authorizeSecurityGroup" : return new AuthorizeSecurityGroup(this, var);
            case "revokeSecurityGroup" : return new RevokeSecurityGroup(this, var);
            case "authorizeSecurityGroupEgress" : return new AuthorizeSecurityGroupEgress(this, var);
            case "revokeSecurityGroupEgress" : return new RevokeSecurityGroupEgress(this, var);
            case "deleteSecurityGroup" : return new DeleteSecurityGroup(this, var);
            //VPC相关
            case "createVpc" : return new CreateVpc(this, var);
            case "describeVpcs" : return new DescribeVpcs(this, var);
            case "modifyVpcAttribute" : return new ModifyVpcAttribute(this, var);
            case "deleteVpc" : return new DeleteVpc(this, var);
            //路由器相关
            case "describeVRouters" : return new DescribeVRouters(this, var);
            case "modifyVRouterAttribute" : return new ModifyVRouterAttribute(this, var);
            //交换机相关
            case "createVSwitch" : return new CreateVSwitch(this, var);
            case "describeVSwitches" : return new DescribeVSwitches(this, var);
            case "modifyVSwitchAttribute" : return new ModifyVSwitchAttribute(this, var);
            case "deleteVSwitch" : return new DeleteVSwitch(this, var);
            //路由表相关
            case "describeRouteTables" : return new DescribeRouteTables(this, var);
            //监控相关    --此接口暂不可用
            case "describeInstanceMonitorData" : return new DescribeInstanceMonitorData(this, var);
            default:
                System.out.println("没有找到命令 " + var[0]);
                System.out.println("请输入命令' ecs -help '查找帮助");
                break;
        }
        return null;
    }
}
