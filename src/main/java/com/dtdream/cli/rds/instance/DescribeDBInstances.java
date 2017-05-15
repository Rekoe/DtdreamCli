package com.dtdream.cli.rds.instance;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.RdsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/11/30.
 */
public class DescribeDBInstances extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeDBInstances";
    private String regionId = Config.getRegion();
    private String engine;
    private String dbInstanceType;
    private String instanceNetworkType;
    private String connectionMode;
    private int pageSize = 30;
    private int pageNum = 1;

    public DescribeDBInstances(RdsCommandFactory factory, String [] parameters) {
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
            DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
            request.setRegionId(regionId);
            request.setEngine(engine);
            request.setDBInstanceType(dbInstanceType);
            request.setInstanceNetworkType(instanceNetworkType);
            request.setConnectionMode(connectionMode);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            try{
                DescribeDBInstancesResponse response = RdsClient.getInstance().getAcsResponse(request);
                List<DescribeDBInstancesResponse.DBInstance> dbInstances = response.getItems();
                System.out.printf("总共有: %d 个 RDS 实例\n", response.getTotalRecordCount());
                if(dbInstances != null){
                    for(DescribeDBInstancesResponse.DBInstance instance : dbInstances){
                        System.out.printf("DBInstancdID: %-17s  DBInstanceType: %-8s  NetworkType: %-7s  " +
                                "ConnectionMode %-8s  RegionID: %-17s\n" +
                                "    Description: %s\n" +
                                "    DBInstanceStatus: %-8s  Engine: %-8s  EngineVersion: %-4s  " +
                                "DBInstanceNetType: %-8s  LockMode: %-8s  LockReason: %s\n" +
                                "    MasterInstanceId: %-17s  GuardDBInstanceId: %-17s  TempDBInstanceId: %-17s  " +
                                "ExpireTime: %s\n" +
                                "    ReadOnlyDBInstanceId:%s\n",
                                instance.getDBInstanceId(),
                                instance.getDBInstanceType(),
                                instance.getInstanceNetworkType(),
                                instance.getConnectionMode(),
                                instance.getDBInstanceDescription(),
                                instance.getRegionId(),
                                instance.getDBInstanceStatus(),
                                instance.getEngine(),
                                instance.getEngineVersion(),
                                instance.getDBInstanceNetType(),
                                instance.getLockMode(),
                                instance.getLockReason(),
                                instance.getMasterInstanceId(),
                                instance.getGuardDBInstanceId(),
                                instance.getTempDBInstanceId(),
                                instance.getExpireTime(),
                                instance.getReadOnlyDBInstanceIds().toString()
                        );
                    }
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
    public void help() {
        System.out.println("NAME");
        System.out.println("    describeDBInstances");
        System.out.println("SYNOPSIS");
        System.out.println("    describeDBInstances [options -r] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询RDS实例列表");
        System.out.println("Options");
        System.out.println("    -r ");
        System.out.println("         --regionId, 区域ID");
        System.out.println("    -e");
        System.out.println("         --engine, 数据库类型。\n" +
                "         取值范围:[MySQL/SQLServer/PostgreSQL/PPAS；不填则返回所有]。");
        System.out.println("    -t");
        System.out.println("         --dbInstanceType, 实例类型。\n" +
                "         取值范围: [Primary:主实例；Readonly：只读实例；Guard：灾备实例；Temp：临时实例；不填，默认返回所有]。");
        System.out.println("    -T");
        System.out.println("         --instanceNetworkType, 实例网络类型.\n" +
                "         类型范围：[VPC：返回VPC实例;Classic：返回Classic实例；不填，默认返回所有]");
        System.out.println("    -m");
        System.out.println("         --connectionMode, 连接模式。\n" +
                "         类型：[Performance为标准访问模式;Safty为高安全访问模式;不填，默认返回所有]。");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号默认值为1");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小默认值为30");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=15){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        regionId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        engine = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        dbInstanceType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-T" :
                    index++;
                    if(parameters.length > index){
                        instanceNetworkType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-m" :
                    index++;
                    if(parameters.length > index){
                        connectionMode = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        pageNum = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
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
        if(StringUtils.isBlank(regionId)){
            System.out.println("参数错误，缺少必要参数，regionId不能为空，请输入：describeDBInstances -help 查看帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
