package com.dtdream.cli.oss.bucket;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketReferer;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomugo on 2016/9/9.
 */
public class Referer extends Command implements CommandParser {
    private String bucketName;
    private boolean read = true;
    private String refererName;
    private List<String> refererList = new ArrayList<String>();
    private boolean clear = false;
    private boolean delete = false;

    public Referer(OssCommandFactory factory, String [] parameters) {
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
                    // 获取Bucket Referer列表
                    BucketReferer br = OssClient.getInstance().getBucketReferer(bucketName);
                    refererList = br.getRefererList();
                    for (String referer : refererList) {
                        System.out.println(referer);
                    }
                }else{
                    //清空白名单
                    if(clear){
                        // 默认允许referer字段为空，且referer白名单为空。
                        BucketReferer br = new BucketReferer();
                        OssClient.getInstance().setBucketReferer(bucketName, br);
                    }else{
                        BucketReferer br = OssClient.getInstance().getBucketReferer(bucketName);
                        List<String> oldrefererList = br.getRefererList();
                        if(delete){//删除指定referer
                            deleteReferer(oldrefererList, refererName);
                            BucketReferer newbr = new BucketReferer(true, oldrefererList);
                            OssClient.getInstance().setBucketReferer(bucketName, newbr);
                            System.out.println("delete " + refererName + " succeed");
                        }else if(refererName != null){//添加referer
                            oldrefererList.add(refererName);
                            BucketReferer newbr = new BucketReferer(true, oldrefererList);
                            OssClient.getInstance().setBucketReferer(bucketName, newbr);
                            System.out.println("add " + refererName + " succeed");
                        }else{
                            // 允许referer字段为空，并设置Bucket Referer列表
                            BucketReferer newbr = new BucketReferer(true, refererList);
                            OssClient.getInstance().setBucketReferer(bucketName, newbr);
                            System.out.println("set refererList succeed");
                        }

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
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    referer  --获取Bucket Referer白名单列表");
        System.out.println("SYNOPSIS");
        System.out.println("    referer [bucketName]");
        System.out.println("DESCRIPTION");
        System.out.println("    -l  list添加referer白名单项");
        System.out.println("    -c  clear 清空referer白名单项");
        System.out.println("    -a  add  添加referer白名单项");
        System.out.println("    -d  delete 删除referer白名单项");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'referer -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-c" :
                    read = false;
                    clear = true;
                    index++;
                    break;
                case "-a" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        refererName = parameters[index++];
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'referer -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        delete = true;
                        refererName = parameters[index++];
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'referer -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-l" :
                    index++;
                    read = false;
                    while (parameters.length > index){
                        refererList.add(parameters[index++]);
                    }
                    if(refererList.size() == 0){
                        System.out.println("参数错误");
                        System.out.println("请输入 'referer -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    break;
                default :
                    bucketName = parameters[index++];
                    break;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(bucketName)){
            System.out.println("参数错误，bucketName不能为空，请输入：referer -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    private List<String> deleteReferer(List<String> refererList, String refererName){
        if(refererList.contains(refererName)){
            refererList.remove(refererName);
        }else{
            System.out.println("当前白名单中不包含： " + refererName);
        }
        return refererList;
    }

}
