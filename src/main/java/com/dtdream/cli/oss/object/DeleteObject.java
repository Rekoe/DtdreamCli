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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomugo on 2016/9/5.
 */
public class DeleteObject extends Command implements CommandParser{
    private String bucketName;
    private List<String> keys = new ArrayList<String>();
    private boolean force = false;
    private boolean recursive = false;
    public DeleteObject(OssCommandFactory factory, String [] parameters) {
        super(factory);
        this.parameters = parameters;
    }

    @Override
    public String getInput() {
        return null;
    }

    @Override
    public boolean checkInput(String input) {
        return false;
    }

    @Override
    public String getCommandKey() {
        return "delete1";
    }

    @Override
    public Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        if(parse(parameters)){
            try{
                for(String key : keys){
                    //判断文件是否存在
                    if(OssClient.getInstance().doesObjectExist(bucketName, key)){
                        if(force){
                            OssClient.getInstance().deleteObject(bucketName, key);
                            System.out.println("delete: " + bucketName + "/" + key + " succeed!");

                        }else{
                            String [] delete = {"rm", bucketName, key};
                            CommandRecord.getInstance().setNextCommand(new Delete((OssCommandFactory) this.factory,delete));
                        }
                    }else if(OssUtil.doesDirectoryExist(bucketName,key)){
                        if(recursive){
                            if(OssUtil.doesDirectoryEmpty(bucketName, key)){
                                //删除空文件夹
                                if(!key.endsWith("/")){
                                    OssClient.getInstance().deleteObject(bucketName, key + "/");
                                }else{
                                    OssClient.getInstance().deleteObject(bucketName, key);
                                }
                                System.out.println("delete: " + bucketName + "/" + key + " succeed!");
                            }else{
                                //迭代删除文件夹中的内容
                                ObjectListing objectListing = null;
                                String nextMarker = null;
                                String prefix = "";
                                if(!key.endsWith("/")){
                                    prefix = key + "/";
                                }
                                do{
                                    ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withPrefix(prefix).withMarker(nextMarker).withMaxKeys(100);
                                    objectListing = OssClient.getInstance().listObjects(listObjectsRequest);
                                    List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                                    for (OSSObjectSummary s : sums) {
                                        if(force){
                                            OssClient.getInstance().deleteObject(bucketName,s.getKey());
                                            System.out.println("delete: " + bucketName + "/" + s.getKey() + " succeed!");
                                        }else{
                                            String [] delete = {"rm", bucketName, s.getKey()};
                                            CommandRecord.getInstance().setNextCommand(new Delete((OssCommandFactory) this.factory,delete));
                                        }
                                    }
                                    nextMarker = objectListing.getNextMarker();

                                } while (objectListing.isTruncated());

                            }
                        }else{
                            System.out.println("无法删除：" + bucketName + ": " + key + " 是一个目录！");
                        }
                    }else{
                        System.out.println("你要删除的文件：" + bucketName+": " + key + " 不存在");
                    }
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
                CommandRecord.getInstance().deleteCommand(this);
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    rm  --删除指定文件或文件夹");
        System.out.println("SYNOPSIS");
        System.out.println("    删除指定文件 [bucketName] [key]");
        System.out.println("DESCRIPTION");
        System.out.println("    删除指定文件");
        System.out.println("    -f 强制删除指定文件");
        System.out.println("    -r 迭代删除文件夹及文件夹中的内容");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("缺少参数");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while(parameters.length>index){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-f" :
                    force = true;
                    index++;
                    break;
                case "-r" :
                    recursive = true;
                    index++;
                    break;
                case "-rf" :
                    recursive = true;
                    force = true;
                    index++;
                    break;
                default:
                    if(bucketName == null){
                        bucketName = parameters[index];
                        index++;
                    }
                    if(parameters.length > index){
                        keys.add(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！。。");
                        System.out.println("请输入 'rm -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(bucketName)){
            System.out.println("参数错误，bucketName不能为空，请输入：delete Object -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
