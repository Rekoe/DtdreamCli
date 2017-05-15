package com.dtdream.cli.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.SetBucketCORSRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomugo on 2016/9/8.
 */
public class Cors extends Command implements CommandParser {
    private String bucketName;
    private boolean read = true;
    private int time = 10;
    private List<String> origin = new ArrayList<String>();
    private List<String> method = new ArrayList<String>();
    private List<String> aheader = new ArrayList<String>();
    private List<String> eheader = new ArrayList<String>();
    private boolean delete = false;

    public Cors(OssCommandFactory factory, String [] parameters) {
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
                    ArrayList<SetBucketCORSRequest.CORSRule> corsRules;
                    //获得CORS规则列表
                    corsRules =  (ArrayList<SetBucketCORSRequest.CORSRule>) OssClient.getInstance().getBucketCORSRules(bucketName);
                    for (SetBucketCORSRequest.CORSRule rule : corsRules) {
                        System.out.println("Allowed Origin: ");
                        for (String allowedOrigin1 : rule.getAllowedOrigins()) {
                            //获得允许跨域请求源
                            System.out.println(allowedOrigin1);
                        }
                        System.out.println("Allowed Method: ");
                        for (String allowedMethod1 : rule.getAllowedMethods()) {
                            //获得允许跨域请求方法
                            System.out.println(allowedMethod1);
                        }

                        if (rule.getAllowedHeaders().size() > 0){
                            System.out.println("Allowed Header: ");
                            for (String allowedHeader1 : rule.getAllowedHeaders()) {
                                //获得允许头部列表
                                System.out.println(allowedHeader1);
                            }
                        }

                        if (rule.getExposeHeaders().size() > 0) {
                            System.out.println("Exposed Header: ");
                            for (String exposeHeader : rule.getExposeHeaders()) {
                                //获得允许头部
                                System.out.println(exposeHeader);
                            }
                        }

                        if ( null != rule.getMaxAgeSeconds()) {
                            System.out.println("缓存时间: ");
                            System.out.println(rule.getMaxAgeSeconds());
                        }
                    }
                }else{
                    if(delete){
                        // 清空bucket的CORS规则
                        OssClient.getInstance().deleteBucketCORSRules(bucketName);
                        System.out.println("delete succeed!");
                    }else{
                        SetBucketCORSRequest request = new SetBucketCORSRequest(bucketName);
                        //CORS规则的容器,每个bucket最多允许10条规则
                        ArrayList<SetBucketCORSRequest.CORSRule> putCorsRules = new ArrayList<SetBucketCORSRequest.CORSRule>();

                        SetBucketCORSRequest.CORSRule corRule = new SetBucketCORSRequest.CORSRule();

                        corRule.setAllowedMethods(method);
                        corRule.setAllowedOrigins(origin);
                        corRule.setAllowedHeaders(aheader);
                        corRule.setExposeHeaders(eheader);
                        //指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒。
                        corRule.setMaxAgeSeconds(time);

                        //最多允许10条规则
                        putCorsRules.add(corRule);
                        request.setCorsRules(putCorsRules);
                        OssClient.getInstance().setBucketCORS(request);
                        System.out.println("set succeed! ");
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
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    cors  - 查看指定bucket的跨域资源共享规则");
        System.out.println("SYNOPSIS");
        System.out.println("    cors [bucketName]");
        System.out.println("DESCRIPTION");
        System.out.println("    查看指定bucket的跨域资源共享规则");
        System.out.println("    -o  origin 来源");
        System.out.println("    -m  method  (GET/PUT/DELETE/POST/HEAD)访问方法");
        System.out.println("    -a  Allowed header");
        System.out.println("    -e  Expose header");
        System.out.println("    -d  delete");
        System.out.println("    -t  time  指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'cors -help '查看相关指令");
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
                case "-o" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        while(!parameters[index].startsWith("-") && parameters.length>index){
                            origin.add(parameters[index++]);
                        }
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'cors -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-m" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        while(parameters.length>index && !parameters[index].startsWith("-")){
                            switch (parameters[index]){
                                case "get" :
                                case "GET" :
                                    method.add("GET");
                                    index++;
                                    break;
                                case "put" :
                                case "PUT" :
                                    method.add("PUT");
                                    index++;
                                    break;
                                case "delete" :
                                case "DELETE" :
                                    method.add("DELETE");
                                    index++;
                                    break;
                                case "post" :
                                case "POST" :
                                    method.add("POST");
                                    index++;
                                    break;
                                case "head" :
                                case "HEAD" :
                                    method.add("HEAD");
                                    index++;
                                    break;
                                default :
                                    System.out.println("method类型出错");
                                    System.out.println("请输入 'cors -help '查看相关指令");
                                    CommandRecord.getInstance().popLastCommand();
                                    return false;
                            }
                        }
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'cors -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-a" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        while(!parameters[index].startsWith("-") && parameters.length>index){
                            aheader.add(parameters[index++]);
                        }
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'cors -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-e" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        while(!parameters[index].startsWith("-") && parameters.length>index){
                            eheader.add(parameters[index++]);
                        }
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'cors -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-t" :
                    index++;
                    read = false;
                    if(parameters.length > index){
                        time = Integer.parseInt(parameters[index++]);
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'cors -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    read = false;
                    delete = true;
                    index++;
                    break;
                default :
                    bucketName = parameters[index];
                    index++;
                    break;
            }
        }
        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
