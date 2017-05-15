package com.dtdream.cli.oss.object;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.SimplifiedObjectMeta;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by thomugo on 2016/9/5.
 */
public class GetObjectMeta extends Command implements CommandParser {
    private final String COMMAND_NAME = "getObjectMeta";
    private String [] parameters;
    private String bucketName;
    private String key;
    private boolean displayAll = false;
    public GetObjectMeta(OssCommandFactory factory, String [] parameters) {
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
                if(displayAll){
                    // 获取文件的全部元信息
                    ObjectMetadata metadata = OssClient.getInstance().getObjectMetadata(bucketName, key);
                    System.out.println("ContentType: " + metadata.getContentType());
                    System.out.println("LastModified: " + metadata.getLastModified());
                }else{
                    // 获取文件的部分元信息
                    SimplifiedObjectMeta objectMeta = OssClient.getInstance().getSimplifiedObjectMeta(bucketName, key);
                    System.out.println("Size: " + objectMeta.getSize() + " Bytes");
                    System.out.println("Etag: " + objectMeta.getETag());
                    System.out.println("LastModified: " + objectMeta.getLastModified());
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
        System.out.println("    getObjectMeta --获取Object的元信息");
        System.out.println("SYNOPSIS");
        System.out.println("    getObjectMeta [options]");
        System.out.println("DESCRIPTION");
        System.out.println("    获取指定Object的元信息");
        System.out.println("    options [-b -k]");
        System.out.println("    -a  获取文件全部元数据");
        System.out.println("    -b  bucketName");
        System.out.println("    -k  key");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'getObjectMeta -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length  > index && parameters.length <=6 ){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-a" :
                    displayAll = true;
                    index++;
                    break;
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
                case "-k" :
                    index++;
                    if(parameters.length > index){
                        key = parameters[index];
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
        if(StringUtils.isBlank(bucketName) || StringUtils.isBlank(key)){
            System.out.println("参数错误，bucketName 或 key 不能为空，请输入：getObjectMeta -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
