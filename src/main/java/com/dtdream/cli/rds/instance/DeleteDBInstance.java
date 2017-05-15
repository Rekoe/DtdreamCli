package com.dtdream.cli.rds.instance;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.rds.model.v20140815.DeleteDBInstanceRequest;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;
import com.dtdream.cli.util.RdsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/12/5.
 */
public class DeleteDBInstance extends Command implements CommandParser{
    private final String COMMAND_NAME = "deleteDBInstance";
    private String instanceId;

    public DeleteDBInstance(RdsCommandFactory factory, String [] parameters) {
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
            DeleteDBInstanceRequest request = new DeleteDBInstanceRequest();
            request.setDBInstanceId(instanceId);
            try{
                RdsClient.getInstance().getAcsResponse(request);
                System.out.println("delete rds dbinstance :" + instanceId + " succeed!!");
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    deleteDBInstance");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteDBInstance");
        System.out.println("DESCRIPTION");
        System.out.println("    --删除指定RDS实例");
        System.out.println("Options [i]");
        System.out.println("     -i");
        System.out.println("         --instanceID, RDS实例ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        instanceId = parameters[index];
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
        if(StringUtils.isBlank(instanceId)){
            System.out.println("参数错误，缺少必要参数，instanceId 不能为空，请输入: deleteDBInstance -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
