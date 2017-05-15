package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.DescribeAutoSnapshotPolicyExRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeAutoSnapshotPolicyExResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by thomugo on 2016/11/2.
 */
public class DescribeAutoSnapshotPolicyEx extends Command implements CommandParser {
    private int pageSize = 10;
    private int pageNum = 1;
    private String autoSnapshotPolicyId;
    public DescribeAutoSnapshotPolicyEx(EcsCommandFactory factory, String [] parameters) {
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
            DescribeAutoSnapshotPolicyExRequest request = new DescribeAutoSnapshotPolicyExRequest();
            request.setAutoSnapshotPolicyId(autoSnapshotPolicyId);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            try{
                DescribeAutoSnapshotPolicyExResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.printf("共有：%d 条策略\n", response.getTotalCount() );
                List<DescribeAutoSnapshotPolicyExResponse.AutoSnapshotPolicy> policies = response.getAutoSnapshotPolicies();
                for (DescribeAutoSnapshotPolicyExResponse.AutoSnapshotPolicy policy : policies){
                    System.out.printf("AutoSnapshotPolicyID: %s  AutoSnapshotPolicyName: %s  TimePoints: %s  " +
                                    "RepeatWeekdays: %s  RetentionDays: %d \n",
                            policy.getAutoSnapshotPolicyId(),
                            policy.getAutoSnapshotPolicyName(),
                            policy.getTimePoints(),
                            policy.getRepeatWeekdays(),
                            policy.getRetentionDays());
                }
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
    //自动快照策略目前暂不支持根据id查询(由于阿里专有云bug造成的)
    public void help() {
        System.out.println("NAME");
        System.out.println("    describeAutoSnapshotPolicy 查询自动快照策略");
        System.out.println("SYNOPSIS");
        System.out.println("    describeAutoSnapshotPolicy [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --查询快照列表");
        System.out.println("Options ");
        /*System.out.println("    -i");
        System.out.println("         --autoSnapshotPolicyId, " +
                "要查询的自动快照策略ID，参数为空时为返回全部自动快照策略。一个带有格式的String：\"p-xxxxxxxx, p-yyyyyyyyy\" ");*/
        System.out.println("    -N");
        System.out.println("         --pageNum, 分页号");
        System.out.println("    -S");
        System.out.println("         --pageSize, 分页大小 ");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=7){
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
                        autoSnapshotPolicyId = EcsUtil.toJsonStr(autoSnapshotPolicyId);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：describeAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：describeAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：describeAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.println("参数错误！请输入：describeAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        /*if(autoSnapshotPolicyId == null){
            System.out.println("参数错误，缺少必要参数！请输入：describeAutoSnapshotPolicy -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }*/
        return true;
    }
}
