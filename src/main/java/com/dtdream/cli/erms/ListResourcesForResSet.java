package com.dtdream.cli.erms;

import com.aliyun.erms.api.ResSetManage;
import com.aliyun.erms.api.common.PagedResponse;
import com.aliyun.erms.api.request.ListResourcesForResSetRequest;
import com.aliyun.erms.api.response.Resource;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.erms.util.ErmsCommandFactory;
import com.dtdream.cli.util.Config;
import org.apache.commons.lang.StringUtils;
import java.util.List;

/**
 * Created by shumeng on 2016/11/22.
 */
public class ListResourcesForResSet extends Command implements CommandParser{
    private final String COMMAND_NAME = "listResourcesForResSet";
    private String resSetId;
    private String resType;
    private String region;
    private long callerId;
    private int pageNum = 1;
    private int pageSize = 10;
    public ListResourcesForResSet(ErmsCommandFactory factory, String [] parameters) {
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
            ResSetManage resSetManage = (ResSetManage) Config.context.getBean("resSetManage");
            ListResourcesForResSetRequest request = new ListResourcesForResSetRequest();
            request.setResSetId(resSetId);
            request.setRegionId(region);
            request.setResourceType(resType);
            request.setPageSize(pageSize);
            request.setPageIndex(pageNum);
            request.setCallerId(callerId);
            try{
                PagedResponse<Resource> response = resSetManage.listResourcesForResSet(request);
                List<Resource> datas = response.getData();
                if(datas != null){
                    for(Resource data : datas){
                        System.out.printf("OwnerID: %-17s  ResourceID: %-20s  ResourceName: %-20s  ResrouceType: %-17s  " +
                                        "ResrouceStatus %-17s\n",
                                data.getOwnerId(),
                                data.getResourceId(),
                                data.getResourceName(),
                                data.getResourceType(),
                                data.getResourceType());
                        System.out.println("  Properties: " + data.getProperties().toString());
                    }
                }else{
                    System.out.println("没有找到任何资源！！");
                }
            } catch (RuntimeException e){
                e.printStackTrace();
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }


        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    listResourcesForResSet 获取指定资源合集下某类云产品实例");
        System.out.println("SYNOPSIS");
        System.out.println("    listResourcesForResSet [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --获取指定资源合集下某类云产品实例");
        System.out.println("Options [-t -r -i -c]");
        System.out.println("    -c");
        System.out.println("         --callerID, ");
        System.out.println("    -t");
        System.out.println("         --resType, 云产品类型: ecs,rds,oss,slb,odps,ads");
        System.out.println("    -r");
        System.out.println("         --resgionID, 区域");
        System.out.println("    -i");
        System.out.println("         --resSetID, 资源合集ID");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号");
        System.out.println("    -s");
        System.out.println("         --pageSize, 分页大小");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=11){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        callerId = Long.parseLong(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        resType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        region = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        resSetId = parameters[index];
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
        if(StringUtils.isBlank(resType) || StringUtils.isBlank(resSetId) || StringUtils.isBlank(region)){
            advice(COMMAND_NAME);
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
