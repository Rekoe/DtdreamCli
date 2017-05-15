package com.dtdream.cli.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.AbortMultipartUploadRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/9/7.
 */
public class AbortUpload extends Command implements CommandParser{
    private String uploadId;
    private String bucketName;
    private String key;
    public AbortUpload(OssCommandFactory factory, String [] parameters) {
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
                AbortMultipartUploadRequest abortMultipartUploadRequest =
                        new AbortMultipartUploadRequest(bucketName, key, uploadId);
                OssClient.getInstance().abortMultipartUpload(abortMultipartUploadRequest);
                System.out.println("Abort upload " + bucketName + ": " + key + "  uploadID: " + uploadId + " succeed!!!");
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
        System.out.println("    abortUpload  -撤销上传事件");
        System.out.println("SYNOPSIS");
        System.out.println("    撤销指定的上传事件 [uploadId] ");
        System.out.println("DESCRIPTION");
        System.out.println("    撤销指定的上传事件");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        if(parameters.length == 1){
            System.out.println("缺少参数");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        int index = 1;
        while (parameters.length>index && parameters.length<=4){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                default:
                    bucketName = parameters[index];
                    index++;
                    if(parameters.length > index){
                        key = parameters[index];
                        index++;
                        if(parameters.length > index){
                            uploadId = parameters[index];
                            index++;
                            break;
                        }else{
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'abortUpload  -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'abortUpload  -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
