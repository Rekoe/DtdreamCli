package com.dtdream.cli.oss.bucket;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketLoggingResult;
import com.aliyun.oss.model.SetBucketLoggingRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/9/8.
 */
public class Log extends Command implements CommandParser {
    private String targetPrefix = null;
    private String targetBucket = null;
    private String sourceBucket;
    private boolean read = true;
    public Log(OssCommandFactory factory, String [] parameters) {
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
                if(read){
                    BucketLoggingResult result = OssClient.getInstance().getBucketLogging(sourceBucket);
                    System.out.println(result.getTargetBucket());
                    System.out.println(result.getTargetPrefix());
                }else{
                    SetBucketLoggingRequest request = new SetBucketLoggingRequest(sourceBucket);
                    request.setTargetBucket(targetBucket);
                    request.setTargetPrefix(targetPrefix);
                    OssClient.getInstance().setBucketLogging(request);
                    System.out.println("set succeed!! ");
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
            }finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    log  --查看bucket的日志设置");
        System.out.println("SYNOPSIS");
        System.out.println("    log [bucketName] ");
        System.out.println("DESCRIPTION");
        System.out.println("    查看指定bucket的日志设置");
        System.out.println("    -start  [targetBucketName] [targetPrefix]  为bucket开启日志");
        System.out.println("    -stop   为bucket关闭日志");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if (parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'log -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while(parameters.length>index && parameters.length <=5){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-start" :
                    read = false;
                    index++;
                    if(parameters.length > index){
                        targetBucket = parameters[index];
                        index++;
                        if(parameters.length > index){
                            targetPrefix = parameters[index];
                            index++;
                            break;
                        }else{
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'log -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'log -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-stop" :
                    read = false;
                    index++;
                    break;
                default:
                    sourceBucket = parameters[index];
                    index++;
                    break;
            }
        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
