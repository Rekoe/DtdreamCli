package com.dtdream.cli.oss.bucket;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by thomugo on 2016/8/30.
 */
public class DeleteBucket extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteBucket";
    private String bucketName;

    public DeleteBucket(OssCommandFactory factory, String [] parameters) {
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
                OssClient.getInstance().deleteBucket(bucketName);
                System.out.println("delete bucket ["+ bucketName + "] succeed!");
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
        System.out.println("    deleteBucket 删除指定的bucket");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteBucket [options -b] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定的Bucket");
        System.out.println("Options");
        System.out.println("    -b");
        System.out.println("      --bucketName, bucket名称。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
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
            System.out.println("参数错误，bucketName 不能为空，请输入： deleteBucket -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
