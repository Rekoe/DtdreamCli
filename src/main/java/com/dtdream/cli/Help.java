package com.dtdream.cli;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.util.DtdreamCommandFactory;
import org.apache.log4j.Logger;

/**
 * Created by shumeng on 2016/11/25.
 */
public class Help extends Command implements CommandParser{
    static Logger logger = Logger.getLogger(Help.class);

    public Help(DtdreamCommandFactory factory) {
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
        logger.info("command: {dtdream -help}");
        CommandRecord.getInstance().popLastCommand();
    }

    public void help() {

        System.out.println("========================欢迎使用DtDream命令行工具====================");
        System.out.println("DtDreamCli命令行模块：");
        System.out.println("  --ecs:  ");
        System.out.println("    云服务器 Elastic Compute Service（ECS）是数梦工厂提供的一种基础云计算服务。使用云服务器ECS" +
                "就像使用水、电、煤气等资源一样便捷、高效。您无需提前采购硬件设备，而是根据业务需要，随时创建所需数量的云服务器实例，并在使用过程中，" +
                "随着业务的扩展，对云服务器进行扩容磁盘、增加带宽。如果不再需要云服务器，也可以方便的释放资源，节省费用。\n" +
                "    云服务器 ECS 实例是一个虚拟的计算环境，包含了 CPU、内存、操作系统、磁盘、带宽等最基础的服务器组件，是 ECS " +
                "提供给每个用户的操作实体。一个实例就等同于一台虚拟机，您对所创建的实例拥有管理员权限，可以随时登录进行使用和管理。" +
                "您可以在实例上进行基本操作，如挂载磁盘、创建快照、创建镜像、部署环境等。");
        System.out.println();
        System.out.println("  --oss:  ");
        System.out.println("    数梦工厂对象存储服务（Object Storage Service，简称OSS），是数梦工厂对外提供的海量，安全，低成本，高可靠的云存储服务。" +
                "用户可以通过本文档提供的简单的REST 接口，在任何时间、任何地点、任何互联网设备上进行上传和下载数据。基于OSS，" +
                "用户可以搭建出各种多媒体分享网站、网盘、个人和企业数据备份等基于大规模数据的服务。");
        System.out.println();
        System.out.println("  --rds:  ");
        System.out.println("    关系型数据库（Relational Database Service，简称 RDS）是一种稳定可靠、可弹性伸缩的在线数据库服务。" +
                "    基于阿里云分布式文件系统和高性能存储，RDS 支持 MySQL、SQL Server、PostgreSQL 和 PPAS（Postgre Plus Advanced Server，一种高度兼容" +
                " Oracle 的数据库）引擎，并且提供了容灾、备份、恢复、监控、迁移等方面的全套解决方案，彻底解决数据库运维的烦恼。");
        System.out.println();
        System.out.println("  --slb:  ");
        System.out.println("    负载均衡（Server Load " +
                "Balancer）是对多台云服务器进行流量分发的负载均衡服务。负载均衡可以通过流量分发扩展应用系统对外的服务能力，通过消除单点故障提升应用系统的可用性。\n" +
                "    负载均衡主要有如下几个功能点：\n" +
                "    1. 负载均衡服务通过设置虚拟服务地址（IP），将位于同一地域（Region）的多台云服务器（Elastic Compute " +
                "Service，简称ECS）资源虚拟成一个高性能、高可用的应用服务池；根据应用指定的方式，将来自客户端的网络请求分发到云服务器池中。\n" +
                "    2. 负载均衡服务会检查云服务器池中ECS的健康状态，自动隔离异常状态的ECS，从而解决了单台ECS的单点问题，同时提高了应用的整体服务能力。" +
                "在标准的负载均衡功能之外，负载均衡服务还具备TCP与HTTP抗DDoS攻击的特性，增强了应用服务器的防护能力。\n" +
                "    3. 负载均衡服务是ECS面向多机方案的一个配套服务，需要同ECS结合使用。");
        System.out.println();
        System.out.println("  --erms");
        System.out.println("    数梦云资产管理服务。erms通过云监控和大数据管家来获取云产品的性能数据");
        System.out.println("  --ram");
        System.out.println("    RAM (Resource Access Management) 提供的是资源访问控制服务。" +
                "通过RAM，您可以集中管理您的用户（比如员工、系统或应用程序），以及控制用户可以访问您名下哪些资源的权限。");
        System.out.println("    RAM包括下列功能：\n" +
                "    1. 集中控制RAM用户及其密钥 —— 您可以管理每个用户及其访问密钥，为用户绑定/解绑多因素认证设备\n" +
                "    2. 集中控制RAM用户的访问权限 —— 您可以控制每个用户可以访问您名下哪些资源的操作权限\n" +
                "    3. 集中控制RAM用户的资源访问方式 —— 您可以确保用户必须使用安全信道（如SSL）、在指定时间、以及在指定的网络环境下请求访问特定的云服务\n" +
                "    4. 集中控制云资源 —— 您可以对用户创建的实例或数据进行集中控制。当用户离开您的组织时，这些实例或数据不会丢失\n" +
                "    5. 统一账单 —— 您的账户将收到包括所有用户的资源操作所发生的费用的单一账单");
        System.out.println();
        System.out.println("  --logo");
        System.out.println("    应用logo相关操作。通过命令行控制应用的Logo");
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
