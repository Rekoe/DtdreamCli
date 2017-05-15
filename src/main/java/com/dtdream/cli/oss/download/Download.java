package com.dtdream.cli.oss.download;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.DownloadFileResult;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by thomugo on 2016/9/2.
 */
public class Download extends Command implements CommandParser {
    private final String COMMAND_NAME = "download";
    private String [] parameters;
    private String bucketName;
    private String key;
    private String filePath;
    private long partSize = 1 * 1024 * 1024;
    private int taskNum = 5;

    public Download(OssCommandFactory factory, String []parameters) {
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
                if(filePath.endsWith("\\") || filePath.endsWith("/")){
                    filePath += key;
                }
                DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, key);
                downloadFileRequest.setDownloadFile(filePath);
                downloadFileRequest.setTaskNum(taskNum);
                downloadFileRequest.setPartSize(partSize);
                downloadFileRequest.setEnableCheckpoint(true);
                // 下载文件
                DownloadFileResult downloadRes = OssClient.getInstance().downloadFile(downloadFileRequest);
                // 下载成功时，会返回文件的元信息
                System.out.println("download: " + key + " succeed!");
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
        System.out.println("    download - 使用断点续传方式下载文件");
        System.out.println("SYNOPSIS");
        System.out.println("    download [options -b -k -p]");
        System.out.println("DESCRIPTION");
        System.out.println("    使用断点续传方式下载指定文件");
        System.out.println("    -b");
        System.out.println("      --bucketName, bucket的名称");
        System.out.println("    -k");
        System.out.println("      --key, bucket中指定的object");
        System.out.println("    -p");
        System.out.println("      --localFilePath, 本地文件路径");
        System.out.println("    -t  [key]  指定并发上传线程数taskNum");
        System.out.println("    -s  [key]  指定上传文件的分片大小size（单位Byte）");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'download -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-b" :
                    index++;
                    if(parameters.length > index){
                        bucketName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-k" :
                    index++;
                    if(parameters.length > index){
                        key = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        filePath = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        taskNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        partSize = Long.parseLong(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                default:
                    advice(COMMAND_NAME);
                    return false;

            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(bucketName) || StringUtils.isBlank(key) || StringUtils.isBlank(filePath)){
            advice(COMMAND_NAME);
            return false;
        }
        return false;
    }
}
