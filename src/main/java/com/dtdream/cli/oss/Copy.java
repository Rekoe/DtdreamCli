package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomugo on 2016/9/6.
 */
public class Copy extends Command implements CommandParser{
    private String sbucketName;
    private String skey;
    private String dbucketName;
    private String dkey;
    private boolean succeed = false;
    // 分片大小，10MB
    private long partSize = 1024 * 1024 * 10;
    private List<PartETag> partETags = new ArrayList<PartETag>();
    public Copy(OssCommandFactory factory, String [] parameters) {
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
                if(dkey == null){
                    if(sbucketName.equals(dbucketName)){
                        if(skey.contains("/")){
                            dkey = skey.substring(skey.lastIndexOf("/") + 1);
                        }else{
                            return ;
                        }
                    }else{
                        dkey = skey;
                    }
                }
                // 得到被拷贝object大小
                ObjectMetadata objectMetadata = OssClient.getInstance().getObjectMetadata(sbucketName, skey);
                long contentLength = objectMetadata.getContentLength();
                //文件大小大于500MByte时采用分片拷贝
                if(contentLength > 100*1024*1024){
                    // 计算分块数目
                    int partCount = (int) (contentLength / partSize);
                    if (contentLength % partSize != 0) {
                        partCount++;
                    }
                    System.out.println("total part count:" + partCount);

                    // 初始化拷贝任务
                    InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(dbucketName, dkey);
                    InitiateMultipartUploadResult initiateMultipartUploadResult = OssClient.getInstance().initiateMultipartUpload(initiateMultipartUploadRequest);
                    String uploadId = initiateMultipartUploadResult.getUploadId();

                    final int finalPartCount = partCount;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int percent = 0;
                            while (!isCopyCompleted()) {
                                if((getPercentage(finalPartCount) - percent) > 0.1 )
                                    percent = (int)getPercentage(finalPartCount);
                                System.out.println("Copy progress: " + percent + "%");
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();


                    // 分片拷贝

                    for (int i = 0; i < partCount; i++) {
                        // 计算每个分块的大小
                        long skipBytes = partSize * i;
                        long size = partSize < contentLength - skipBytes ? partSize : contentLength - skipBytes;

                        // 创建UploadPartCopyRequest
                        UploadPartCopyRequest uploadPartCopyRequest =
                                new UploadPartCopyRequest(sbucketName, skey, dbucketName, dkey);
                        uploadPartCopyRequest.setUploadId(uploadId);
                        uploadPartCopyRequest.setPartSize(size);
                        uploadPartCopyRequest.setBeginIndex(skipBytes);
                        uploadPartCopyRequest.setPartNumber(i + 1);
                        UploadPartCopyResult uploadPartCopyResult = OssClient.getInstance().uploadPartCopy(uploadPartCopyRequest);

                        // 将返回的PartETag保存到List中
                        partETags.add(uploadPartCopyResult.getPartETag());
                    }

                    // 提交分片拷贝任务
                    CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
                            dbucketName, dkey, uploadId, partETags);
                    OssClient.getInstance().completeMultipartUpload(completeMultipartUploadRequest);
                    succeed = true;
                    System.out.println("Copy progress: 100%");
                    System.out.println("multicopy " + sbucketName +": " + skey + " succeed!");

                }else{//简单拷贝
                    // 拷贝Object
                    // 创建CopyObjectRequest对象
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(sbucketName, skey, dbucketName, dkey);
                    // 设置新的Metadata

                    // 复制Object
                    CopyObjectResult result = OssClient.getInstance().copyObject(copyObjectRequest);
                    System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());
                    System.out.println("Copy " + sbucketName +": " + skey + " succeed!");
                }

            }catch (OSSException oe) {
                if(oe.getErrorCode().equals("OperationNotSupported")){
                    System.out.println(sbucketName + ": " + skey + " 不支持文件复制操作");
                }
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message: " + ce.getMessage());
            }finally {
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
        System.out.println("    cp  -拷贝指定文件到目标路径");
        System.out.println("SYNOPSIS");
        System.out.println("    拷贝指定文件到目标路径 [src(bucketName, key)] [des(bucketName)] ");
        System.out.println("DESCRIPTION");
        System.out.println("    拷贝指定文件到目标路径(注意追加上传文件不支持此操作)");
        System.out.println("    -k  指定目的地文件的key");
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
        while (parameters.length > index && parameters.length <= 6){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case  "-k" :
                    index++;
                    if(parameters.length > index){
                        dkey = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'cp -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    if(sbucketName == null){
                        sbucketName = parameters[index];
                        index++;
                        if(parameters.length > index){
                            skey = parameters[index];
                            index++;
                            if(parameters.length > index){
                                dbucketName = parameters[index];
                                index++;
                                break;
                            }else{
                                System.out.println("参数错误！。。");
                                System.out.println("请输入 'cp -help '查看相关指令");
                                CommandRecord.getInstance().popLastCommand();
                                return false;
                            }
                        }else{
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'cp -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'cp -help '查看相关指令");
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

    private double getPercentage(int partCount){
        return partETags.size()*100.0 / partCount;
    }

    private boolean isCopyCompleted(){
        return succeed;
    }

}
