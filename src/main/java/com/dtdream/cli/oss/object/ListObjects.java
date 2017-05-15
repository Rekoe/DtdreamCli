package com.dtdream.cli.oss.object;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.oss.util.OssUtil;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by thomugo on 2016/9/5.
 */
public class ListObjects extends Command implements CommandParser {
    private final String COMMAND_NAME = "listObjects";
    private String bucketName;
    private String prefix="";
    private int maxKeys = 30;
    private String nextMarker = null;
    private String fdirName;
    private String ddirName;

    public ListObjects(OssCommandFactory factory, String [] parameters) {
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
            if(OssUtil.doesBucketExist(bucketName)){
                try{
                    ObjectListing objectListing = null;
                    do {
                        // 构造ListObjectsRequest请求
                        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withPrefix(prefix).withMarker(nextMarker).withMaxKeys(maxKeys);
                        if(fdirName != null){
                            if(!fdirName.endsWith("/")){
                                fdirName += "/";
                            }
                            if(!OssUtil.doesDirectoryExist(bucketName, fdirName)){
                                System.out.println("不存在文件夹：" + fdirName);
                                return;
                            }else{
                                listObjectsRequest.setPrefix(fdirName);
                                listObjectsRequest.withDelimiter("/");
                                objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
                                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                                for (OSSObjectSummary s : sums) {
                                    System.out.println("\t" + s.getKey());
                                }
                                nextMarker = objectListing.getNextMarker();
                            }
                        }
                        if(ddirName != null){
                            if(!ddirName.endsWith("/")){
                                ddirName += "/";
                            }
                            if(!OssUtil.doesDirectoryExist(bucketName, ddirName)){
                                System.out.println("bucket: " + bucketName + " 中不存在文件夹：" +
                                        ddirName.substring(0, ddirName.length()-1));
                                return;
                            }else{
                                listObjectsRequest.setPrefix(ddirName);
                                listObjectsRequest.withDelimiter("/");
                                objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
                                List<String> sums = objectListing.getCommonPrefixes();
                                for (String s : sums) {
                                    System.out.println("\t" + s);
                                }
                                nextMarker = objectListing.getNextMarker();
                            }
                        }
                        if(fdirName == null && ddirName == null){
                            objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
                            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                            for (OSSObjectSummary s : sums) {
                                System.out.println("\t" + s.getKey());
                            }
                            nextMarker = objectListing.getNextMarker();
                        }

                    } while (objectListing.isTruncated());

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
            }else{
                System.out.println("不存在名称为：" + bucketName + " 的bucket!");
                CommandRecord.getInstance().popLastCommand();
            }

        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    listObjects -递归列举目录下所有的文件");
        System.out.println("SYNOPSIS");
        System.out.println("    listObjects [options -b]");
        System.out.println("DESCRIPTION");
        System.out.println("    列举指定bucket下的Object");
        System.out.println("    -b");
        System.out.println("       --bucketName");
        System.out.println("    -p");
        System.out.println("       --prefix 指定Object前缀");
        System.out.println("    -f");
        System.out.println("       --dirName 列出指定目录下的文件");
        System.out.println("    -d");
        System.out.println("       --dirName 列出指定目录下的文件夹");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'listObjects -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        int index = 1;
        while (parameters.length > index && parameters.length <= 9){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
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
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        prefix = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-f" :
                    index++;
                    if(parameters.length > index){
                        fdirName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        ddirName = parameters[index];
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
            System.out.println("参数错误，bucketName 不能为空，请输入：listObjects -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
