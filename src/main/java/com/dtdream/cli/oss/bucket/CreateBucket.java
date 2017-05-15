package com.dtdream.cli.oss.bucket;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
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
 * Created by thomugo on 2016/8/29.
 */
public class CreateBucket extends Command implements CommandParser {
    private final String COMMAND_NAME = "cteateBucket";
    public static final String ID;
    private String bucketName;
    //AccessControlList 访问权限
    private String acl = "default";
    static {
        ID = OssUtil.StepFlags.createBucket.toString();
    }

    public CreateBucket(OssCommandFactory factory, String [] parameters) {
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
                    CreateBucketRequest bucketRequest = new CreateBucketRequest(bucketName);
                    // 设置bucket权限
                    switch (acl){
                        case "private" : bucketRequest.setCannedACL(CannedAccessControlList.Private);
                            break;
                        case "public-read" : bucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                            break;
                        case "public-read-write" : bucketRequest.setCannedACL(CannedAccessControlList.PublicReadWrite);
                            break;
                        default : break;
                    }
                    //创建bucket
                    OssClient.getInstance().createBucket(bucketRequest);

                    listBuckets();

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

    private void listBuckets(){
        List<Bucket> buckets = OssClient.getInstance().listBuckets();
        System.out.println("Your buckets:");
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=5){
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
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        switch (parameters[index]){
                            case "DEFAULT" :
                            case "default" : acl = "default";
                                break;
                            case "PRIVATE" :
                            case "private" : acl = "private";
                                break;
                            case "PUBLIC-READ" :
                            case "public-read" : acl = "public-read";
                                break;
                            case "PUBLIC-WRITE-READ" :
                            case "PUBLIC-READ-WRITE" :
                            case "public-write-read" :
                            case "public-read-write" : acl = "public-read-write";
                                break;
                            default :
                                System.out.println("命令参数错误，访问控制权限只能为：[private, public-read, public-read-write] " +
                                        "中的一个");
                                CommandRecord.getInstance().popLastCommand();
                                return false;
                        }
                        index++;
                        break;
                    }else {
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
        if (StringUtils.isBlank(bucketName)){
            System.out.println("参数错误，缺少必要参数，请输入：createBucket -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    createBucket 创建Bucket");
        System.out.println("SYNOPSIS");
        System.out.println("    createBucket [options -b] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --创建Bucket");
        System.out.println("Options");
        System.out.println("    -b");
        System.out.println("      --bucketName, bucket名称。" +
                "         Bucket 命名规范：1)只能包括小写字母，数字和短横线（-）；2)必须以小写字母或者数字开头；3)长度必须在 3-63 字节之间");
        System.out.println("    -a");
        System.out.println("      --acl, bucket的访问控制权限: default, public-read-write, public-read");

    }

}
