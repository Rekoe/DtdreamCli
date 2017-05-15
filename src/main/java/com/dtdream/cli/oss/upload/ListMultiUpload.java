package com.dtdream.cli.oss.upload;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.AbortMultipartUploadRequest;
import com.aliyun.oss.model.ListMultipartUploadsRequest;
import com.aliyun.oss.model.MultipartUpload;
import com.aliyun.oss.model.MultipartUploadListing;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by thomugo on 2016/9/7.
 */
public class ListMultiUpload extends Command implements CommandParser {
    private final String COMMAND_NAME = "listMultiUpload";
    private String bucketName;
    private boolean abort = false;
    public ListMultiUpload(ICommandFactory factory, String [] parameters) {
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
                // 列举分片上传事件
                MultipartUploadListing multipartUploadListing;
                ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(bucketName);
                // 每个页中事件数目
                listMultipartUploadsRequest.setMaxUploads(50);
                do {
                    multipartUploadListing = OssClient.getInstance().listMultipartUploads(listMultipartUploadsRequest);
                    for (MultipartUpload multipartUpload : multipartUploadListing.getMultipartUploads()) {
                        // Upload Id
                        System.out.println("UploadID: " + multipartUpload.getUploadId());
                        // Key
                        System.out.println("Key: " + multipartUpload.getKey());
                        // Date of initiate multipart upload
                        System.out.println("Date: " + multipartUpload.getInitiated().toString());
                        if(abort){
                            AbortMultipartUploadRequest abortMultipartUploadRequest =
                                    new AbortMultipartUploadRequest(bucketName, multipartUpload.getKey(), multipartUpload.getUploadId());
                            OssClient.getInstance().abortMultipartUpload(abortMultipartUploadRequest);
                        }
                    }
                    listMultipartUploadsRequest.setKeyMarker(multipartUploadListing.getNextKeyMarker());
                    listMultipartUploadsRequest.setUploadIdMarker(multipartUploadListing.getNextUploadIdMarker());
                } while (multipartUploadListing.isTruncated());
                System.out.println("list over!!!");
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
        System.out.println("    listUpload  -列举指定bucket下全部上传事件");
        System.out.println("SYNOPSIS");
        System.out.println("    分页列举全部上传事件 [options -b] ");
        System.out.println("DESCRIPTION");
        System.out.println("    列举全部上传事件");
        System.out.println("    -b");
        System.out.println("       --bucketName, bucket名称");
        System.out.println("    -a");
        System.out.println("       --abort列举并撤销未完成的上传事件");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("缺少参数");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length <=4){
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
                case "-a" :
                    abort = true;
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
        if(StringUtils.isBlank(bucketName)){
            System.out.println("参数错误，bucketName 不能为空，请输入：listMultiUpload -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
