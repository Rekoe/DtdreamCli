package com.dtdream.cli.oss.object;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;

/**
 * Created by thomugo on 2016/9/5.
 */
public class SetObjectAcl extends Command implements CommandParser {
    private final String COMMAND_NAME = "setObjectAcl";
    private String []parameters;
    private String bucketName;
    private String key;
    private String acl;

    public SetObjectAcl(OssCommandFactory factory, String [] parameters) {
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
                switch (acl){
                    case "private" : OssClient.getInstance().setObjectAcl(bucketName, key, CannedAccessControlList.Private);
                        break;
                    case "public-read" : OssClient.getInstance().setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
                        break;
                    case "public-read-write" : OssClient.getInstance().setObjectAcl(bucketName, key, CannedAccessControlList.PublicReadWrite);
                        break;
                    default : break;
                }
                System.out.println("set Object : " + bucketName + "/ " + key + " access control list successed!");
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
        System.out.println("    setObjectAcl -设置Object的访问控制链");
        System.out.println("SYNOPSIS");
        System.out.println("    setObjectAcl [options -b -k -a]");
        System.out.println("DESCRIPTION");
        System.out.println("    为指定的对象Object设置访问控制链");
        System.out.println("    -a");
        System.out.println("      --acl, bucket的访问控制权限: default, public-read-write, public-read");
        System.out.println("    -b");
        System.out.println("      --bucketName, bucket名称");
        System.out.println("    -a");
        System.out.println("      --acl, bucket的访问控制权限: default, public-read-write, public-read");


    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'setObjectAcl -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length > index && parameters.length <=5) {
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        switch (parameters[index]){
                            case "PRIVATE" :
                            case "private" : acl = "private";
                                index++;
                                break;
                            case "PUBLIC-READ" :
                            case "public-read" : acl = "public-read";
                                index++;
                                break;
                            case "PUBLIC-WRITE-READ" :
                            case "PUBLIC-READ-WRITE" :
                            case "public-write-read" :
                            case "public-read-write" : acl = "public-read-write";
                                index++;
                                break;
                            default:
                                System.out.println("参数错误！。。");
                                System.out.println("请输入 'setObjectAcl -help '查看相关指令");
                                CommandRecord.getInstance().popLastCommand();
                                return false;
                        }
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'setObjectAcl -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    if(bucketName == null){
                        bucketName = parameters[index];
                        index++;
                        if(parameters.length > index){
                            key = parameters[index];
                            index++;
                            break;
                        }else{
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'putObject -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        if(parameters.length > index){
                            System.out.println("参数错误！。。");
                            System.out.println("请输入 'putObject -help '查看相关指令");
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }else{
                            break;
                        }
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
