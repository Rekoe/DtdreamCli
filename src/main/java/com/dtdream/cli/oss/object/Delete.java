package com.dtdream.cli.oss.object;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.OssClient;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by thomugo on 2016/9/6.
 */
public class Delete extends Command implements CommandParser{
    private final String COMMAND_NAME = "delete";
    private String bucketName;
    private String key;
    public Delete(OssCommandFactory factory, String [] parameters) {
        super(factory);
        this.parameters = parameters;
    }

    @Override
    public String getInput() {
        try
        {
            System.out.println("是否删除？(Y/N)");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            String input = in.readLine();
            return input;
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkInput(String input) {
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n"))
        {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getCommandKey() {
        return "delete2";
    }

    @Override
    public Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        String input = (String) CommandRecord.getInstance().getRecords().get("delete2");
        if(parse(parameters) && input.equalsIgnoreCase("y")){
            try{
                OssClient.getInstance().deleteObject(bucketName,key);
                System.out.println("delete: " + bucketName + "/" + key + " succeed!");
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
        System.out.println("    delete  --删除某个指定的Object。");
        System.out.println("SYNOPSIS");
        System.out.println("    delete [options]");
        System.out.println("DESCRIPTION");
        System.out.println("    查看指定bucket的跨域资源共享规则");
        System.out.println("    options [-b -k]");
        System.out.println("    -b  bucketName");
        System.out.println("    -k  key");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=5){
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
            System.out.println("参数错误，bucketName 或 key 不能为空，请输入：delete -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
