package com.dtdream.cli.oss;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;

/**
 * Created by thomugo on 2016/8/30.
 */
public class Help extends Command {
    public Help(OssCommandFactory factory) {
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
        System.out.println("欢迎使用 DtDreamCli OSS 模块!!! ");
        System.out.println("Bucket相关：");
        System.out.println(" -oss createBucket [bucketName] : 创建bucket");
        System.out.println(" -oss listBuckets : 列出当前账户下所有的bucket");
        System.out.println(" -oss deleteBucket [bucketName] : 删除指定名称的bucket");
        System.out.println(" -oss getBucketAcl [bucketName] : 获取指定名称的bucket的访问控制链");
        System.out.println(" -oss setBucketAcl [bucketName] : 设置指定名称的bucket的访问控制链");
        System.out.println(" -oss getBucketLocation [bucketName] : 获取指定名称的bucket的区域位置（Region）信息");
        System.out.println(" -oss getBucketInfo [bucketName] : 获取指定名称的bucket的基本信息");

        CommandRecord.getInstance().popLastCommand();
    }

    @Override
    public void help() {

    }

    @Override
    public boolean parse(String[] parameters) {
        return false;
    }
}
