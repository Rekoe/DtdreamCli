package com.dtdream.cli.oss.object;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectAcl;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/9/5.
 */
public class GetObjectAcl extends Command implements CommandParser {
    String [] parameters;
    String bucketName;
    String key;
    public GetObjectAcl(OssCommandFactory factory, String [] parameters) {
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
                ObjectAcl objectAcl = OssClient.getInstance().getObjectAcl(bucketName, key);
                System.out.println(objectAcl.getPermission().toString());
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
        System.out.println("    getObjectAcl - 获取指定Object的访问控制链");
        System.out.println("SYNOPSIS");
        System.out.println("    getObjectAcl [bucketName] [key]");
        System.out.println("DESCRIPTION");
        System.out.println("    获取指定的对象Object设置访问控制链");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'getObjectAcl -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while(parameters.length > index && parameters.length <= 3){
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
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'getObjectAcl -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
            }

        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
