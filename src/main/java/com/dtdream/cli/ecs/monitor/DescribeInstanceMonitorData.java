package com.dtdream.cli.ecs.monitor;

import com.aliyuncs.ecs.model.v20140526.DescribeInstanceMonitorDataRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceMonitorDataResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/11/15.
 */
public class DescribeInstanceMonitorData extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeInstanceMonitorData";
    private String instanceId;
    private String startTime;
    private String endTime;
    private int period = 60;
    public DescribeInstanceMonitorData(EcsCommandFactory factory, String [] parameters) {
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
            DescribeInstanceMonitorDataRequest request = new DescribeInstanceMonitorDataRequest();
            request.setInstanceId(instanceId);
            request.setStartTime(startTime);
            request.setEndTime(endTime);
            request.setPeriod(period);
            try{
                DescribeInstanceMonitorDataResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeInstanceMonitorDataResponse.InstanceMonitorData> instanceMonitorDatas = response
                        .getMonitorData();
                for(DescribeInstanceMonitorDataResponse.InstanceMonitorData monitorData : instanceMonitorDatas){
                    System.out.printf("InstanceId: %-17s TimeStamp: %-12s\n    CPU(CPU的使用比例): %-4d%  " +
                            "IntranetBandwidth(云服务器实例的带宽（单位时间内的网络流量）):%d kb/s\n" +
                            "    InternetRX(云服务器实例接收到的数据流量): %-6d kb/s  InternetTX(云服务器实例接发送的数据流量): %-6d kb/s\n" +
                            "    IOPSRead: %-6d 次/s  IOPSWrite: %-6d 次/s  BPSRead(系统盘磁盘读带宽): %-8 Byte/s  BPSWrite" +
                            "(系统盘磁盘写带宽): %-8 Byte/s\n",
                            monitorData.getInstanceId(),
                            monitorData.getCPU(),
                            monitorData.getTimeStamp(),
                            monitorData.getInternetBandwidth(),
                            monitorData.getInternetRX(),
                            monitorData.getInternetTX(),
                            monitorData.getIOPSRead(),
                            monitorData.getIOPSWrite(),
                            monitorData.getBPSRead(),
                            monitorData.getBPSWrite());
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
        System.out.println("    describeInstanceMonitorData  查询云服务器实例的监控信息");
        System.out.println("SYNOPSIS");
        System.out.println("    describeInstanceMonitorData [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --分页查询本用户的所有云服务器相关的监控信息。\n" +
                "      只能查询到状态非 Deleted 或者非刚刚创建完成尚处于 Stopped 状态的云服务器的监控信息。\n" +
                "      可返回的监控内容包括：云服务器的 CPU使用率、云服务器分配到的内存数、云服务器接收到的数据流量、云服务器发送的数据流量、" +
                "云服务器网络流量、云服务器平均带宽。有可能返回的监控内容中会缺少部分内部，这可能是由于系统没有获得到相应的信息，" +
                "比如当时实例处于 Stopped 状态。\n" +
                "       一次最大只容许返回 400 条监控数据，如果指定的（EndTime – StartTime）/ Peroid > 400，则返回错误。");
        System.out.println("Options [-i -s -e]");
        System.out.println("    -r ");
        System.out.println("       --instanceId, 实例ID");
        System.out.println("    -s");
        System.out.println("       --startTime, 获取数据的起始时间点：按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为：YYYY-MM-DDThh:mm:ssZ。" +
                "例如：2016-11-13T00:00:00Z\n" +
                "如果秒不是 00，则自动取为下一分钟开始时");
        System.out.println("    -e");
        System.out.println("       --endTime, 获取数据的结束时间点：按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为：YYYY-MM-DDThh:mm:ssZ。" +
                "例如：2016-11-13T00:00:00Z \n" +
                "如果秒不是 00，则自动取为下一分钟开始时");
        System.out.println("    -p");
        System.out.println("       --period, 获取监控数据的精度:\n" +
                "         60 秒\n" +
                "         600 秒\n" +
                "         3600 秒\n" +
                "         默认 60 秒。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=9){
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
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        startTime = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        endTime = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        period = Integer.parseInt(parameters[index]);
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
        if(StringUtils.isBlank(instanceId) || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
            System.out.println("参数错误，缺少必要参数，请输入：describeInstanceMonitorData -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
