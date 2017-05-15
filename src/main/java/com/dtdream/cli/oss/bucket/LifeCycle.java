package com.dtdream.cli.oss.bucket;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thomugo on 2016/9/9.
 */
public class LifeCycle extends Command implements CommandParser {
    private String bucketName;
    private String id;
    private String prefix = "";
    private int days;
    private Date date;
    private boolean read = true;
    private boolean enable = true;
    private boolean clear = false;
    private boolean remove = false;
    public LifeCycle(OssCommandFactory factory, String [] parameters) {
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
                List<LifecycleRule> rules = null ;
                try{
                    rules = OssClient.getInstance().getBucketLifecycle(bucketName);
                }catch (OSSException oe){
                    if(oe.getErrorCode().equals("NoSuchLifecycle"));
                    rules = null;
                    System.out.println("您还未设置任何规则！");
                }
                //查看指定bucket下的规则
                if(read){
                    if(rules != null) {
                        for (LifecycleRule rule : rules) {
                            System.out.println("ID:  " + rule.getId());
                            System.out.println("Prefix:  " + rule.getPrefix());
                            System.out.println("ExpirationDays:  " + rule.getExpirationDays());
                        }
                    }
                }else{
                    SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);
                    //清除所有规则
                    if(clear){
                        //aliyun  oss bug
                        //request.clearLifecycles();
                        OssClient.getInstance().deleteBucketLifecycle(bucketName);
                        System.out.println("clear rules succeed! ");
                    }else{
                        if(remove){//删除指定规则
                            if(rules == null){
                                System.out.println("不存在规则： " + id);
                            }else{
                                for (LifecycleRule rule : rules) {
                                    if(rule.getId().equals(id)){
                                        rules.remove(rule);
                                    }
                                }
                            }
                        }else{//添加规则
                            LifecycleRule rule = null;
                            if(enable){
                                rule = new LifecycleRule(id, prefix, LifecycleRule.RuleStatus.Enabled);
                                if(days != 0){
                                    rule.setExpirationDays(days);
                                }else{
                                    rule.setExpirationTime(date);
                                }
                            }else{
                                rule = new LifecycleRule(id, prefix, LifecycleRule.RuleStatus.Disabled);
                                if(days != 0){
                                    rule.setExpirationDays(days);
                                }else{
                                    rule.setExpirationTime(date);
                                }
                            }
                            if(rules == null){
                                rules = new ArrayList<LifecycleRule>();
                            }
                            rules.add(rule);
                        }

                        request.setLifecycleRules(rules);
                        OssClient.getInstance().setBucketLifecycle(request);
                        System.out.println("set rule " + id + " succeed");
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
        System.out.println("    life  --查看bucket的生命周期设置");
        System.out.println("SYNOPSIS");
        System.out.println("    life [bucketName] ");
        System.out.println("DESCRIPTION");
        System.out.println("    查看指定bucket的生命周期设置");
        System.out.println("    -p   [prefix]  为bucket中指定prefix的文件设置生命周期规则(注意规则之间的前缀不能重叠)");
        System.out.println("    -i   [id]  为规则指定ID");
        System.out.println("    -c   清空所有规则");
        System.out.println("    -r   [ruleID] 删除指定规则");
        System.out.println("    -d   [days]   指定规则在对象最后修改时间过后多少天生效(整数)");
        System.out.println("    -D   [date]   指定规则何时之前生效。日期必需服从ISO8601的格式，并且总是UTC的零点。 例如：2002-10-11T00:00:00.000Z \n" +
                           "                  类型：字符串 ");
        System.out.println("    -s   [status （true | false）]  规则的开启状态");


    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数不足");
            System.out.println("请输入 'life -help '查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=10){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        id = parameters[index];
                        read = false;
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        prefix = parameters[index];
                        read = false;
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        days = Integer.parseInt(parameters[index]);
                        read = false;
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-D" :
                    index++;
                    if(parameters.length > index){
                        try {
                            date = DateUtil.parseIso8601Date(parameters[index]);
                            read = false;
                            index++;
                            break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                            CommandRecord.getInstance().popLastCommand();
                            return false;
                        }
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    if(parameters.length > index){
                        enable = Boolean.parseBoolean(parameters[index]);
                        read = false;
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-c" :
                    clear = true;
                    read = false;
                    index++;
                    break;
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        remove = true;
                        read = false;
                        id = parameters [index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default :
                    if(bucketName == null){
                        bucketName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入 'life -help '查看相关指令");
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
            System.out.println("参数错误，bucketName不能为空，请输入：lifeCycle -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
