package com.dtdream.cli.ecs.snapshot;

import com.aliyuncs.ecs.model.v20140526.ModifyAutoSnapshotPolicyExRequest;
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
public class ModifyAutoSnapshotPolicyEx extends Command implements CommandParser {
    private String autoSnapshotPolicyId;
    private String autoSnapshotPolicyName;
    private String timePoints;
    private String repeatWeekdays;
    private int retentionDays = -1;
    public ModifyAutoSnapshotPolicyEx(EcsCommandFactory factory, String [] parameters) {
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
            ModifyAutoSnapshotPolicyExRequest request = new ModifyAutoSnapshotPolicyExRequest();
            request.setautoSnapshotPolicyId(autoSnapshotPolicyId);
            request.setrepeatWeekdays(repeatWeekdays);
            request.settimePoints(timePoints);
            request.setretentionDays(retentionDays);
            request.setautoSnapshotPolicyName(autoSnapshotPolicyName);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("AutoSnapshotPolicy: " + autoSnapshotPolicyId + " was modified!");
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
        System.out.println("    modifyAutoSnapshotPolicy 创建自动快照策略");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteAutoSnapshotPolicy [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -修改一条自动快照策略。\n" +
                "修改自动快照策略后，之前已执行该策略的磁盘随即执行修改后的自动快照策略。");
        System.out.println("Options [-i]");
        System.out.println("    -i");
        System.out.println("         --autoSnapshotPolicyId, 要修改的自动快照策略ID");
        System.out.println("    -n");
        System.out.println("         --autoSnapshotPolicyName, 自动快照策略的名称。不填则为空，默认值为空， [2,128]英文或中文字符，" +
                "必须以大小字母或中文开头，可包含数字，”_”或”-”，磁盘名称会展示在控制台。不能以http://和https://开头。");
        System.out.println("    -t");
        System.out.println("         --timePoints, 指定自动快照的创建时间点。最小单位为小时，从00:00~23:00共24个时间点可选，参数为0~23的数字，" +
                "如：1代表在01:00时间点。\n" +
                "         可以选定多个时间点。传递参数为一个带有格式的 String: \"0, 1, … 23\",最多24个时间点，用半角逗号字符隔开。");
        System.out.println("    -d");
        System.out.println("         --repeatWeekdays, 指定自动快照的重复日期。选定周一到周日中需要创建快照的日期，参数为1~7的数字。\n" +
                "         如：1表示周一。允许选择多个日期。传递参数为一个带有格式的 String：\" 1,2,…7 \" 。");
        System.out.println("    -r");
        System.out.println("         --retentionDays, 指定自动快照的保留时间，单位为天： \n" +
                "           -1：永久保存 (为默认值) \n" +
                "           1~65536：指定保存天数 \n");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=11){
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
                        System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        autoSnapshotPolicyName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        timePoints = parameters[index];
                        timePoints = EcsUtil.toJsonStr(timePoints);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        repeatWeekdays = parameters[index];
                        repeatWeekdays = EcsUtil.toJsonStr(repeatWeekdays);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        retentionDays = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                default:
                    System.out.println("参数错误！请输入：modifyAutoSnapshotPolicy -help 查询帮助");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }

        return checkParameters();
    }

    @Override
    public boolean checkParameters() {
        if(autoSnapshotPolicyId == null){
            System.out.println("参数错误，autoSnapshotPolicyId不能为空，请输入：modifyAutoSnapshotPolicy -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
