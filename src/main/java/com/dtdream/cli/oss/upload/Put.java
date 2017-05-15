package com.dtdream.cli.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.PutObjectRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.io.*;

/**
 * Created by thomugo on 2016/8/31.
 */
public class Put extends Command implements ProgressListener, CommandParser {
    private final String COMMAND_NAME = "put";
    private String bucketName;
    private String key;
    private String fileName;
    //默认采用文件上传方式上传文件
    private boolean mode = true;
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;

    public Put(OssCommandFactory factory, String [] parameters) {
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
            try{
                if(mode){
                    File file = new File(fileName);
                    if(key == null){
                        key = file.getName();
                    }
                    //OssClient.getInstance().putObject(bucketName, key, file);
                    OssClient.getInstance().putObject(new PutObjectRequest(bucketName, key, file).
                            <PutObjectRequest>withProgressListener(this));
                }else{
                    InputStream inputStream = null;
                    try {
                        File file = new File(fileName);
                        if(key == null){
                            key = file.getName();
                        }
                        inputStream = new FileInputStream(file);
                        OssClient.getInstance().putObject(new PutObjectRequest(bucketName, key, inputStream).
                                <PutObjectRequest>withProgressListener(this));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if(inputStream != null){
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //OssClient.getInstance() .putObject(bucketName, key, inputStream);

                }
            } catch (OSSException oe) {
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
        System.out.println("    put - 使用简单方式上传文件，文件大小不能超过5GB");
        System.out.println("SYNOPSIS");
        System.out.println("    putObject [options -b -p]");
        System.out.println("DESCRIPTION");
        System.out.println("    向指定的Bucket上传文件");
        System.out.println("    -b");
        System.out.println("      --buckname, 指定文件存放位置");
        System.out.println("    -k");
        System.out.println("      --key,  指定存放在bucket中的文件的名字");
        System.out.println("    -p");
        System.out.println("      --path, 文件路径");
        System.out.println("    -i  使用流式上传方式上传");
        System.out.println("    -f  使用文件上传方式上传");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'put -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while(parameters.length>index && parameters.length<=9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-b" :
                    index++;
                    if(parameters.length > index){
                        bucketName = parameters[index++];
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-k" :
                    index++;
                    if(parameters.length > index){
                        key = parameters[index++];
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        fileName = parameters[index++];
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    mode = false;
                    index++;
                    break;
                case "-f" :
                    mode = true;
                    index++;
                    break;
                default:
                    advice(COMMAND_NAME);
                    return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(bucketName) || StringUtils.isBlank(fileName)){
            System.out.println("参数错误，bucketName 和 文件路径不能为空。请输入 pub -help 查询帮助！");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
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
