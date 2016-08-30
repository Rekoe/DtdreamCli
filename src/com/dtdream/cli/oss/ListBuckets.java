package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketList;
import com.aliyun.oss.model.ListBucketsRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/8/30.
 */
public class ListBuckets extends Command{
    private String [] parameters;
    private String prefix;
    public ListBuckets(OssCommandFactory factory, String [] parameters) {
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
                ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
                listBucketsRequest.setPrefix(prefix);
                BucketList buckets =  OssClient.getInstance().listBuckets(listBucketsRequest);
                for (Bucket bucket : buckets.getBucketList()) {
                    System.out.println(" - " + bucket.getName());
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
        System.out.println("ListBuckets: 列出当前用户下的Buckets");
        System.out.println(" -p [prefix] ");
        System.out.println("按指定前缀过滤当前用户下的Buckets.");
    }

    @Override
    public boolean parse(String[] parameters) {
        System.out.printf("parse parameters: [ ");
        for (int i = 0; i < parameters.length; i++) {
            System.out.printf(parameters[i] + " ");
        }
        System.out.println("]");

        if(1 == parameters.length){
            prefix = "";
        }else if(2 == parameters.length) {
            if(parameters[1].equals("-help") || parameters[1].equals("--help")){
                help();
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
        }else if(3 == parameters.length){
            if(parameters[1].equalsIgnoreCase("-p")){
                prefix = parameters[2];
            }else{
                System.out.println("参数错误：请输入 -help 查看帮助");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
        }else{
            System.out.println("参数错误：请输入 -help 查看帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }

        return true;

    }
}
