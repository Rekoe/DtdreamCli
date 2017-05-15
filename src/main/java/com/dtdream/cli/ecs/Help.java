package com.dtdream.cli.ecs;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;

/**
 * Created by thomugo on 2016/10/12.
 */
public class Help extends Command implements CommandParser{
    public Help(EcsCommandFactory factory) {
        super(factory);
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
        help();
        CommandRecord.getInstance().popLastCommand();
    }

    @Override
    public void help() {
        System.out.println("欢迎使用 DtDreamCli Ecs 模块!!! ");
        System.out.println("Ecs相关命令：");
        System.out.println("    --ecs  createInstance               创建ECS实例");
        System.out.println("    --ecs  startInstance                启动ECS实例");
        System.out.println("    --ecs  stopInstance                 停止ECS实例");
        System.out.println("    --ecs  rebootInstance               重启ECS实例");
        System.out.println("    --ecs  modifyInstanceAttribute      修改实例属性");
        System.out.println("    --ecs  modifyInstanceVpcAttribute   修改实例VPC属性");
        System.out.println("    --ecs  describeInstanceStatus       查询实例状态");
        System.out.println("    --ecs  describeInstances            查询实例详细信息");
        System.out.println("    --ecs  deleteInstance               删除ECS实例");
        System.out.println("    --ecs  joinSecurityGroup            将ECS实例加入安全组");
        System.out.println("    --ecs  leaveSecurityGroup           将ECS实例移除安全组");
        System.out.println("    --ecs  describeInstanceVncUrl       查询 ECS 的 Web 管理终端地址");
        System.out.println("    --ecs  modifyInstanceVncPasswd      修改 ECS 实例的 Web 管理终端口令");
        System.out.println("Disk相关命令：");
        System.out.println("    --ecs  createDisk                   创建磁盘");
        System.out.println("    --ecs  describeDisks                查询磁盘");
        System.out.println("    --ecs  attachDisk                   挂载磁盘");
        System.out.println("    --ecs  detachDisk                   卸载磁盘");
        System.out.println("    --ecs  modifyDiskAttribute          修改磁盘属性");
        System.out.println("    --ecs  deleteDisk                   删除磁盘");
        System.out.println("    --ecs  reinitDisk                   初始化磁盘");
        System.out.println("    --ecs  resetDisk                    回滚磁盘");
        System.out.println("    --ecs  replaceSystemDisk            更换系统磁盘");
        System.out.println("    --ecs  resizeDisk                   扩容磁盘");
        System.out.println("    --ecs  createInstance               创建ECS实例");
        System.out.println("Snapshot相关命令");
        System.out.println("    --ecs  createSnapshot               创建快照");
        System.out.println("    --ecs  deleteSnapshot               删除快照");
        System.out.println("    --ecs  describeSnapshots            查询快照");
        System.out.println("    --ecs  createAutoSnapshotPolicy     创建自动快照策略");
        System.out.println("    --ecs  deleteAutoSnapshotPolicy     删除自动快照策略");
        System.out.println("    --ecs  modifyAutoSnapshotPolicyEx   修改自动快照策略");
        System.out.println("    --ecs  describeAutoSnapshotPolicyEx 查询自动快照策略");
        System.out.println("    --ecs  applyAutoSnapshotPolicy      执行自动快照策略");
        System.out.println("    --ecs  cancelAutoSnapshotPolicy     取消自动快照策略");
        System.out.println("Image相关命令");
        System.out.println("    --ecs  describeImages               查询镜像");
        System.out.println("    --ecs  createImage                  创建自定义镜像");
        System.out.println("    --ecs  modifyImageAttribute         修改镜像属性");
        System.out.println("    --ecs  deleteImage                  删除自定义镜像");
        System.out.println("    --ecs  copyImage                    复制镜像");
        System.out.println("    --ecs  cancelCopyImage              取消复制镜像");
        System.out.println("SecurityGroup相关命令");
        System.out.println("    --ecs  createSecurityGroup          创建安全组");
        System.out.println("    --ecs  authorizeSecurityGroup       授权安全组入方向规则");
        System.out.println("    --ecs  describeSecurityGroupAttribute 查询安全组规则");
        System.out.println("    --ecs  describeSecurityGroups       查询安全组列表");
        System.out.println("    --ecs  revokeSecurityGroup          撤销安全组入方向规则");
        System.out.println("    --ecs  deleteSecurityGroup          删除安全组");
        System.out.println("    --ecs  modifySecurityGroupAttribute 修改安全组属性");
        System.out.println("    --ecs  authorizeSecurityGroupEgress 授权安全组出方向规则");
        System.out.println("    --ecs  revokeSecurityGroupEgress    撤销安全组出方向规则");
        System.out.println("VPC相关命令");
        System.out.println("    --ecs  createVpc                    新建专有网络");
        System.out.println("    --ecs  deleteVpc                    删除专有网络");
        System.out.println("    --ecs  describeVpcs                 查询专有网络");
        System.out.println("    --ecs  modifyVpcAttribute           修改专有网络属性");
        System.out.println("VRouter相关命令");
        System.out.println("    --ecs  describeVRouters             查询路由器列表");
        System.out.println("    --ecs  modifyVRouterAttribute       修改路由器属性");
        System.out.println("VSwitch相关命令");
        System.out.println("    --ecs  createVSwitch                新建交换机");
        System.out.println("    --ecs  deleteVSwitch                删除交换机");
        System.out.println("    --ecs  describeVSwitches            查询交换机列表");
        System.out.println("    --ecs  modifyVSwitchAttribute       修改交换机属性");
        System.out.println("Region相关命令");
        System.out.println("    --ecs  describeRegions              查询可用地域列表");
        System.out.println("    --ecs  describeZones                产讯可用区列表");
        System.out.println("其它命令");
        System.out.println("    --ecs  describeInstanceTypes        查询实例资源规格列表");
        System.out.println("    --ecs  describeInstanceTypeFamilies 查询实例规格族列表");
        System.out.println("    --ecs  describeTasks                查询指定的异步请求的进度");
        System.out.println("    --ecs  describeTaskAttribute        查询任务属性");
        System.out.println("    --ecs  cancelTask                   取消任务");

    }

    @Override
    public boolean parse(String[] parameters) {
        return false;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }

}
