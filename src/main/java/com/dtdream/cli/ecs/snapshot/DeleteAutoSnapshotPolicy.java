package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.DeleteAutoSnapshotPolicyRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/2.
 */
public class DeleteAutoSnapshotPolicy extends Command implements CommandParser {
    private String autoSnapshotPolicyId;
    public DeleteAutoSnapshotPolicy(EcsCommandFactory factory, String [] parameters) {
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
            DeleteAutoSnapshotPolicyRequest request = new DeleteAutoSnapshotPolicyRequest();
            request.setautoSnapshotPolicyId(autoSnapshotPolicyId);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("AutoSnapshotPolicy: " + autoSnapshotPolicyId + " was deleted!");
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
        System.out.println("    deleteAutoSnapshotPolicy 删除指定自动快照策略");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteAutoSnapshotPolicy [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --释放一条自动快照策略。\n" +
                "      如果当前预释放的自动快照策略已经在磁盘上执行，释放自动快照策略后这些磁盘取消执行该策略。");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --autoSnapshotPolicyId, 快照ID");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        autoSnapshotPolicyId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：deleteAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：deleteAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(autoSnapshotPolicyId == null){
            System.out.println("参数错误！请输入：deleteAutoSnapshotPolicy -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
