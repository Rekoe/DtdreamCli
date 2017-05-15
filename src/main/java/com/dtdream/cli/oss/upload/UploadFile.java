package com.dtdream.cli.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.UploadFileRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * Created by thomugo on 2016/9/2.
 */
public class UploadFile extends Command implements CommandParser {
    private  String [] parameters;
    private String fileName;
    private String bucketName;
    private String key;
    private int taskNum = 5;
    private long partSize = 10 * 1024 * 1024;

    public UploadFile(OssCommandFactory factory, String [] parameters) {
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
    public void doExecute(){
        if(parse(parameters)){
            try{
                File file = new File(fileName);
                if(key == null){
                    key = file.getName();
                }
                UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
                // 指定上传的本地文件
                uploadFileRequest.setUploadFile(fileName);
                // 指定上传并发线程数
                uploadFileRequest.setTaskNum(taskNum);
                // 指定上传的分片大小
                uploadFileRequest.setPartSize(partSize);
                // 开启断点续传
                uploadFileRequest.setEnableCheckpoint(true);
                // 断点续传上传
                OssClient.getInstance().uploadFile(uploadFileRequest);
                System.out.println("upload file : " + fileName + " succeed!!");
            }catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message: " + oe.getMessage());
                System.out.println("Error Code:       " + oe.getErrorCode());
                System.out.println("Request ID:      " + oe.getRequestId());
                System.out.println("Host ID:           " + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message: " + ce.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    uploadFile - 使用断点续传方式上传文件");
        System.out.println("SYNOPSIS");
        System.out.println("    uploadFile [src] [desc(bucketName key)]");
        System.out.println("DESCRIPTION");
        System.out.println("    向指定的Bucket key追加上传");
        System.out.println("    -k  [key]  指定上传文件的key");
        System.out.println("    -t  [key]  指定并发上传线程数");
        System.out.println("    -s  [key]  指定上传文件的分片大小（单位Byte）");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'uploadFile -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length > index && parameters.length <=9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-k" :
                    index++;
                    if(index < parameters.length){
                        key = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-t" :
                    index++;
                    if(index < parameters.length){
                        taskNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(index < parameters.length){
                        partSize = Long.parseLong(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default :
                    fileName = parameters[index];
                    index++;
                    if(index < parameters.length){
                        bucketName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(fileName) || StringUtils.isBlank(bucketName)){
            System.out.println("参数错误，src(源文件) 和bucketName(目标路径)不能为空，请输入：uploadFile -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
