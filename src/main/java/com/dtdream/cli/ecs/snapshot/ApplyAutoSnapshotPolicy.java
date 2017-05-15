package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.ApplyAutoSnapshotPolicyRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by thomugo on 2016/11/2.
 */
public class ApplyAutoSnapshotPolicy extends Command implements CommandParser {
    private String autoSnapshotPolicyId;
    private String diskIds;
    public ApplyAutoSnapshotPolicy(EcsCommandFactory factory, String [] parameters) {
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
            ApplyAutoSnapshotPolicyRequest request = new ApplyAutoSnapshotPolicyRequest();
            request.setautoSnapshotPolicyId(autoSnapshotPolicyId);
            request.setdiskIds(diskIds);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("Apply AutoSnapshotPolicy: " + autoSnapshotPolicyId + " succeed.");
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
        System.out.println("    applyAutoSnapshotPolicy 执行自动快照策略");
        System.out.println("SYNOPSIS");
        System.out.println("    applyAutoSnapshotPolicy [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --为磁盘执行自动快照策略。\n" +
                "      一个磁盘只能执行一条自动快照策略，已拥有自动快照策略的情况下再进行执行操作视为修改当前执行的自动快照策略。\n" +
                "      可同时对多个磁盘执行同一条自动快照策略。\n" +
                "      对多个磁盘执行同一条自动快照策略时，不能保证这些磁盘的快照数据处于同一个时间点，即无法保证他们是并行快照。");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --autoSnapshotPolicyId, 要执行的快照策略ID");
        System.out.println("     -I");
        System.out.println("         --diskIds, 要应用的自动快照策略的磁盘集合。一个带有格式的 String：\"d-xxxxxxxxx, d-yyyyyyyyy, … "+
                " d-zzzzzzzzz\"，最多 X 个 Id，用英文半角逗号字符隔开。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=5){
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
                        System.out.println("参数错误！请输入：applyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-I" :
                    index++;
                    if(parameters.length > index){
                        diskIds = parameters[index];
                        diskIds = EcsUtil.toJsonStr(diskIds);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：applyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：applyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if (autoSnapshotPolicyId==null || diskIds==null){
            System.out.println("参数错误，缺少必要参数！请输入：applyAutoSnapshotPolicy -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
