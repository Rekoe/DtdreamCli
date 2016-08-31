package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;

/**
 * Created by thomugo on 2016/8/31.
 */
public class PutObject extends Command{
    private String bucketName;
    private String key;
    private String filename;
    //默认采用流式上传方式上传文件
    private boolean mode = true;
    public PutObject(OssCommandFactory factory, String [] parameters) {
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
        System.out.println("    putObject - 使用简单方式上传文件，文件大小不能超过5GB");
        System.out.println("SYNOPSIS");
        System.out.println("    putObject [src(key)] [desc(bucketName)]");
        System.out.println("DESCRIPTION");
        System.out.println("    向指定的Bucket上传文件");
        System.out.println("    -i  使用流式上传方式上传");
        System.out.println("    -f  使用文件上传方式上传");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        if(2 == parameters.length){
            if(parameters[1].equals("-help") || parameters[1].equals("--help")){
                help();
            }else{
                System.out.println("参数错误！。。");
                System.out.println("请输入 'putObject -help '查看相关指令");
            }
            CommandRecord.getInstance().popLastCommand();
            return false;
        }else if(3 == parameters.length){
            key = parameters[1];
            bucketName = parameters[2];
        }else if(4 == parameters.length){
            if(parameters[3].equalsIgnoreCase("-i")){
                mode = true;
                key = parameters[1];
                bucketName = parameters[2];
            }else if(parameters[3].equalsIgnoreCase("-f")){
                mode = false;
                key = parameters[1];
                bucketName = parameters[2];
            }else{
                System.out.println("参数错误！。。");
                System.out.println("请输入 'deleteBucket -help '查看相关指令");
                CommandRecord.getInstance().popLastCommand();
                return false;
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
