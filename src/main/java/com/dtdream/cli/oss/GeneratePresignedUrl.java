package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.Date;

/**
 * Created by thomugo on 2016/9/7.
 */
public class GeneratePresignedUrl extends Command implements CommandParser {
    private final String COMMAND_NAME = "generatePresignedUrl";
    private long expiration = 3600*1000;
    private HttpMethod type;
    private String bucketName;
    private String key;

    public GeneratePresignedUrl(OssCommandFactory factory, String [] parameters) {
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
                // 设置URL过期时间为1小时
                Date exp = new Date(new Date().getTime() + expiration);

                // 生成URL
                URL url = OssClient.getInstance().generatePresignedUrl(bucketName, key, exp);
                System.out.println(url);
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
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    genUrl  --生成签名URL");
        System.out.println("SYNOPSIS");
        System.out.println("    生成签名URL [options]");
        System.out.println("DESCRIPTION");
        System.out.println("    通过生成签名URL的形式提供给用户一个临时的访问URL。在生成URL时，您可以指定URL过期的时间，从而限制用户长时间访问");
        System.out.println("    options [-b -k]");
        System.out.println("    -e   expiration设置过期时间单位为秒");
        System.out.println("    -b   bucketName");
        System.out.println("    -k   key");
        System.out.println("    -t   设置Http方法[put | get | post | delete | head | options]");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'genUrl -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-t":
                    index++;
                    if(parameters.length > index){
                        switch (parameters[index]){
                            case "get" :
                            case "GET" :
                                type = HttpMethod.GET;
                                break;
                            case "put" :
                            case "PUT" :
                                type = HttpMethod.PUT;
                                break;
                            case "post" :
                            case "POST" :
                                type = HttpMethod.POST;
                            case "delete" :
                            case "DELETE" :
                                type = HttpMethod.DELETE;
                            case "head" :
                            case "HEAD" :
                                type = HttpMethod.HEAD;
                            case "options" :
                            case "OPTIONS" :
                                type = HttpMethod.OPTIONS;
                            default:
                                System.out.println("http 类型错误！。。");
                                System.out.println("请输入 'genUrl -help '查看相关指令");
                                CommandRecord.getInstance().popLastCommand();
                                return false;
                        }
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        expiration = Long.parseLong(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
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
            System.out.println("参数错误，bucketName 或 key 不能为空，请输入：generalPresignedUrl -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
