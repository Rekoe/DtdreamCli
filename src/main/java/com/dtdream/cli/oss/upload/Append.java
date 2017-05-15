package com.dtdream.cli.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.AppendObjectRequest;
import com.aliyun.oss.model.AppendObjectResult;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by thomugo on 2016/9/2.
 */
public class Append extends Command implements ProgressListener, CommandParser{
    private String [] parameters;
    private String src;
    private String type;
    private String bucketName;
    private String key;
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    public Append(OssCommandFactory factory, String [] parameters) {
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
            try {
                if (type.equals("string")) {
                    AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName,
                            key, new ByteArrayInputStream(src.getBytes()));
                    if(OssClient.getInstance().doesObjectExist(bucketName,key)){
                        appendObjectRequest.setPosition(OssClient.getInstance().getObject(bucketName, key).getObjectMetadata().getContentLength());
                    }else{
                        appendObjectRequest.setPosition(0L);
                    }
                    AppendObjectResult appendObjectResult = OssClient.getInstance().appendObject(appendObjectRequest);
                    System.out.println("append to " + key + "succeed!");
                }
                if(type.equals("file")){
                    AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName,
                            key, new File(src));
                    if(OssClient.getInstance().doesObjectExist(bucketName,key)){
                        appendObjectRequest.setPosition(OssClient.getInstance().getObject(bucketName, key).getObjectMetadata().getContentLength());
                    }else{
                        appendObjectRequest.setPosition(0L);
                    }
                    AppendObjectResult appendObjectResult = OssClient.getInstance().appendObject(appendObjectRequest.<AppendObjectRequest>withProgressListener(this));
                }
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
            } finally {
                /*
                * Do not forget to shut down the client finally to release all allocated resources.
                */
                    /*if (OssClient.getInstance() != null) {
                        OssClient.getInstance().shutdown();
                    }*/
                //从命令队列中移除该命令
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    append - 使用追加上传方式上传文件");
        System.out.println("SYNOPSIS");
        System.out.println("    append [src] [desc(bucketName key)]");
        System.out.println("DESCRIPTION");
        System.out.println("    向指定的Bucket key追加上传");
        System.out.println("    -d  [bucketName key]  指定追加上传的目的地址");
        System.out.println("    -s  往指定key上传字符串");
        System.out.println("    -f  往指定key上传文件");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'append -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length > index && parameters.length <= 6){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-s" :
                    type = "string";
                    index++;
                    if(parameters.length > index){
                        src = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-f" :
                    type = "file";
                    index++;
                    if(parameters.length > index){
                        src = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        bucketName = parameters[index];
                        index++;
                        if(parameters.length > index){
                            key = parameters[index];
                            index++;
                            break;
                        }else{
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'putObject -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'putObject -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default :
                    System.out.println("参数错误！。。");
                    System.out.println("请输入 'putObject -help '查看相关指令");
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

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }
}
