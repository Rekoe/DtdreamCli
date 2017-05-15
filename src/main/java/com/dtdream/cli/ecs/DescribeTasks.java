package com.dtdream.cli.ecs;

import com.aliyuncs.ecs.model.v20140526.DescribeTasksRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeTasksResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;

import java.util.List;

/**
 * Created by shumeng on 2016/11/14.
 */
public class DescribeTasks extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeTasks";
    private int pageSize = 10;
    private int pageNum = 1;
    private String regionId = Config.getRegion();
    private String taskAction;
    private String taskIds;
    private String taskStatus;
    private String startTime;
    private String endTime;
    public DescribeTasks(EcsCommandFactory factory, String [] parameters) {
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
            DescribeTasksRequest request = new DescribeTasksRequest();
            request.setRegionId(regionId);
            request.setPageSize(pageSize);
            request.setPageNumber(pageNum);
            request.setStartTime(startTime);
            request.setEndTime(endTime);
            request.setTaskAction(taskAction);
            request.setTaskIds(taskIds);
            request.setTaskStatus(taskStatus);
            try{
                DescribeTasksResponse response = EcsClient.getInstance().getAcsResponse(request);
                List<DescribeTasksResponse.Task> tasks = response.getTaskSet();
                System.out.println("Find: " + response.getTotalCount() + " tasks!");
                for(DescribeTasksResponse.Task task : tasks){
                    System.out.printf("TaskID: %-10s  Action: %-10s  TaskStatus: %-10s  SupportCancel: %s\n    " +
                            "CreationTime: %-12s  FinishedTime: %s\n",
                            task.getTaskId(),
                            task.getTaskAction(),
                            task.getTaskStatus(),
                            task.getSupportCancel(),
                            task.getCreationTime(),
                            task.getFinishedTime());
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
        System.out.println("    describeTasks");
        System.out.println("SYNOPSIS");
        System.out.println("    describeTasks [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询指定的异步请求的进度。");
        System.out.println("Options [-i]");
        System.out.println("    -r ");
        System.out.println("       --regionId, 地域ID");
        System.out.println("    -a");
        System.out.println("       --taskAction,任务操作的接口名称，支持的接口有：[ImportImage, ExportImage]");
        System.out.println("    -i");
        System.out.println("       --taskIds, 任务Ids，最多支持100个，用逗号分隔");
        System.out.println("    -S");
        System.out.println("       --taskStatus, 任务状态，可选值：\n" +
                "         Finished 已完成\n" +
                "         Processing 运行中\n" +
                "         Pending 等待执行\n" +
                "         Deleted 已取消\n" +
                "         Paused 暂停\n" +
                "         默认值：无");
        System.out.println("    -s");
        System.out.println("       --startTime, 按创建时间查询，创建时间区间的起始点。时间类型按照 ISO8601 标准表示，并需要使用UTC 时间。格式为：YYYY-MM-DDThh:mmZ。");
        System.out.println("    -e");
        System.out.println("       --endTime, 按创建时间查询，创建时间区间的终止点。时间类型按照 ISO8601 标准表示，并需要使用 UTC 时间。格式为：YYYY-MM-DDThh:mmZ。");
        System.out.println("    -p");
        System.out.println("       --pageSize, 分页大小");
        System.out.println("    -n");
        System.out.println("       --pageNum, 分页号");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=17){
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
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        taskAction = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        taskIds = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-S" :
                    index++;
                    if(parameters.length > index){
                        taskStatus = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }case "-s" :
                    index++;
                    if(parameters.length > index){
                        startTime = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }case "-e" :
                    index++;
                    if(parameters.length > index){
                        endTime = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }case "-p" :
                    index++;
                    if(parameters.length > index){
                        pageSize = Integer.parseInt(parameters[index]);
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
                    default:
                        advice(COMMAND_NAME);
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
