package com.dtdream.cli.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeZonesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;
import java.util.List;

/**
 * Created by thomugo on 2016/11/3.
 */
public class DescribeZones extends Command implements CommandParser {
    private boolean displayInstanceType = false;
    public DescribeZones(EcsCommandFactory factory, String [] parameters) {
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
            DescribeZonesRequest request = new DescribeZonesRequest();
            try{
                DescribeZonesResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeZonesResponse.Zone> zones = response.getZones();
                for (DescribeZonesResponse.Zone zone : zones){
                    System.out.printf("ZoneID: %s   LocalName: %s \n", zone.getZoneId(), zone.getLocalName());
                    System.out.println("  允许创建的资源类型:");
                    System.out.println("  " + zone.getAvailableResourceCreation().toString());
                    System.out.println("  允许创建的磁盘类型:");
                    System.out.println("  " + zone.getAvailableDiskCategories().toString());
                    if(displayInstanceType){
                        System.out.println("  允许创建的实例规格类型:");
                        System.out.println("  " + zone.getAvailableInstanceTypes().toString());
                    }
                    System.out.println();
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
        System.out.println("    describeZones 查询可用区列表");
        System.out.println("SYNOPSIS");
        System.out.println("    describeZones [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询可用区列表");
        System.out.println("    -i");
        System.out.println("        --display available instance types. 是否显示允许创建实例规格类型，true(显示)；false(不显示),默认为:  false");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 0;
        while(parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "describeZones" :
                    index++;
                    break;
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        displayInstanceType = Boolean.parseBoolean(parameters[index]);
                        index++;
                        break;
                    }else{
                        System.out.printf("参数错误！请输入: describeZones -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                    default:
                        System.out.printf("参数错误！请输入: describeZones -help 查询帮助。");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
            }
        }
        return checkParameters();

    }

    @Override
    public boolean checkParameters() {
        return true;
    }
}
