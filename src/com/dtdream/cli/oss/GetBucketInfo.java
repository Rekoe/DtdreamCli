package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.Grant;
import com.aliyun.oss.model.Owner;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

import java.util.Date;
import java.util.Set;

/**
 * Created by thomugo on 2016/8/30.
 */
public class GetBucketInfo extends Command{
    private String [] parameters;
    private String bucketName;
    public GetBucketInfo(OssCommandFactory factory, String [] parameters) {
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
                BucketInfo info = OssClient.getInstance().getBucketInfo(bucketName);
                System.out.println("Bucket: " +  bucketName);
                // owner
                Owner ower = info.getBucket().getOwner();
                System.out.println("  Owner: " + ower.getDisplayName());
                // 权限
                Set<Grant> grants = info.getGrants();
                System.out.println("  Grants: "+ grants.toString());
                // Location
                String location = info.getBucket().getLocation();
                System.out.println("  Location: " + location);
                // 创建日期
                Date createDate = info.getBucket().getCreationDate();
                System.out.println("  CreateDate: " + createDate.toString());

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
        System.out.println("getBucketInfo [bucketName] : 获取指定bucketName 的 Bucket的信息（包括Location、CreationDate、Owner及权限等信息）。");
    }

    @Override
    public boolean parse(String[] parameters) {
        System.out.printf("parse parameters: [ ");
        for (int i = 0; i < parameters.length; i++) {
            System.out.printf(parameters[i] + " ");
        }
        System.out.println("]");
        if(1 == parameters.length){
            System.out.println("参数错误！。。");
            System.out.println("请输入 'deleteBucket -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }else if(2 == parameters.length){
            if(parameters[1].equals("-help") || parameters[1].equals("--help")){
                help();
                CommandRecord.getInstance().popLastCommand();
                return false;
            }else{
                bucketName = parameters[1];
            }
        }else{
            System.out.println("参数错误！。。");
            System.out.println("请输入 'deleteBucket -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }

        return true;
    }
}
