package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.AccessControlList;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/8/30.
 */
public class GetBucketAcl extends Command{
    private String [] parameters;
    private String bucketName;
    public GetBucketAcl(OssCommandFactory factory, String [] parameters) {
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
                AccessControlList acl = OssClient.getInstance().getBucketAcl(bucketName);
                System.out.println(acl.toString());
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
        System.out.println("getBucketAcl [bucketName] : 查询指定 bucketName 的访问控制权限 ");
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
