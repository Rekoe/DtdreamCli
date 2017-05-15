package com.dtdream.cli.oss;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;

/**
 * Created by thomugo on 2016/8/30.
 */
public class Help extends Command implements CommandParser {
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
        help();
        CommandRecord.getInstance().popLastCommand();
    }

    @Override
    public void help() {
        System.out.println("欢迎使用 DtDreamCli OSS 模块!!! ");
        System.out.println("Bucket相关：");
        System.out.println("    --oss  createBucket           创建bucket");
        System.out.println("    --oss  listBuckets            列出当前账户下所有的bucket");
        System.out.println("    --oss  deleteBucket           删除指定名称的bucket");
        System.out.println("    --oss  getBucketAcl           获取指定名称的bucket的访问控制链");
        System.out.println("    --oss  getBucketInfo          获取指定名称的bucket的基本信息");
        System.out.println("    --oss  getBucketLocation      获取指定名称的bucket的区域位置（Region）信息");
        System.out.println("    --oss  setBucketAcl           设置指定名称的bucket的访问控制链");
        System.out.println("文件上传相关：");
        System.out.println("    --oss  put                    用简单上传的方法向指定bucket上传文件");
        System.out.println("    --oss  uploadFile             用断点续传方式向指定bucket上传文件");
        System.out.println("    --oss  mkdir                  在指定bucket中创建文件夹");
        System.out.println("    --oss  append                 使用追加上传方式上传文件");
        System.out.println("    --oss  listMultiUpload        列举指定bucket下全部上传事件");
        System.out.println("    --oss  abortUpload            撤销上传事件");
        System.out.println("文件下载相关：");
        System.out.println("    --oss  get                    把指定的Object下载到指定的本地文件中");
        System.out.println("    --oss  download               使用断点续传方式下载文件");
        System.out.println("文件管理相关：");
        System.out.println("    --oss  copy                   拷贝指定文件到目标路径");
        System.out.println("    --oss  cors                   查看指定bucket的跨域资源共享规则");
        System.out.println("    --oss  delete                 删除某个指定的Object");
        System.out.println("    --oss  deleteObject           删除指定文件或文件夹");
        System.out.println("    --oss  generatePresignedUrl   生成签名URL");
        System.out.println("    --oss  getObjectMeta          获取Object的元信息");
        System.out.println("    --oss  lifeCycle              查看bucket的生命周期设置");
        System.out.println("    --oss  listObjects            递归列举目录下所有的文件");
        //阿里专有云不支持
        System.out.println("    --oss  log                    查看bucket的日志设置");
        System.out.println("    --oss  read                   获取Object的输入流");
        System.out.println("    --oss  referer                获取Bucket Referer白名单列表");
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
