package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.DeleteInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.DeleteInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse;
import com.aliyuncs.ecs.model.v20140526.StopInstanceRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.EcsClient;

import static com.aliyuncs.ecs.model.v20140526.DescribeInstanceStatusResponse.InstanceStatus.Status.DELETED;

/**
 * Created by thomugo on 2016/10/18.
 */
public class DeleteInstance extends Command implements CommandParser {
    private String instanceId;
//    private String instanceName;
    public DeleteInstance(EcsCommandFactory factory, String [] parameters) {
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
            if(instanceId != null){
                try{
                    int i = 2;
                    String [] pendstr = {"。","。。","。。。"};
                    DescribeInstanceStatusResponse.InstanceStatus status = EcsUtil.getInstanceStatus(instanceId);
                    while (status!=null && status.getStatus()!=DELETED){
                        switch (status.getStatus()){
                            case DELETED : //已释放
                                return;
                            case STOPPED : //已停止
                                DeleteInstanceRequest delRequest = new DeleteInstanceRequest();
                                delRequest.setInstanceId(instanceId);
                                DeleteInstanceResponse delresponse = EcsClient.getInstance().getAcsResponse(delRequest);
                                Thread.sleep(1000);

                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case TRANSFERRING : //转换中
                                Thread.sleep(1000);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case RUNNING :  //运行中
                                StopInstanceRequest stopRequest = new StopInstanceRequest();
                                stopRequest.setInstanceId(instanceId);
                                EcsClient.getInstance().getAcsResponse(stopRequest);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case RESETTING : //重置中
                                Thread.sleep(1000);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case STARTING : //启动中
                                Thread.sleep(1000);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case STOPPING : //停止中
                                Thread.sleep(1000);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            case PENDING : //准备中
                                Thread.sleep(1500);
                                i = (i+1)%3;
                                System.out.println("instance: " + instanceId + " was deleting " + pendstr[i]);
                                status = EcsUtil.getInstanceStatus(instanceId);
                                break;
                            default :
                                System.out.println("ECS instance: " + instanceId + " 出现内部状态错误，无法删除！！！");
                                return;
                        }
                    }
                    System.out.printf("ECS Instance：" + instanceId + " was deleted");
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.getMessage();
                    e.getErrMsg();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    CommandRecord.getInstance().popLastCommand();
                }
            }
            /*if(instanceId==null && instanceName!=null){
                List<DescribeInstancesResponse.Instance> list = EcsUtil.getInstance(instanceId, instanceName);
                if(list != null){
                    try{
                        for(DescribeInstancesResponse.Instance instance : list){
                            request.setInstanceId(instance.getInstanceId());
                            while(EcsUtil.getInstanceStatus(instance.getInstanceId()).getStatus() != DescribeInstanceStatusResponse.InstanceStatus.Status.STOPPED){
                                StopInstanceRequest stopRequest = new StopInstanceRequest();
                                stopRequest.setInstanceId(instance.getInstanceId());
                                EcsClient.getInstance().getAcsResponse(stopRequest);
                                Thread.sleep(1000);
                            }
                            EcsClient.getInstance().getAcsResponse(request);
                            System.out.println("instance: " + instance.getInstanceId() + " was started. ");
                        }
                    } catch (ServerException e) {
                        e.printStackTrace();
                    } catch (ClientException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        CommandRecord.getInstance().popLastCommand();
                    }
                }
            }*/
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    deleteInstance");
        System.out.println("SYNOPSIS");
        System.out.println("    deleteInstance [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 删除ECS实例");
        System.out.println("Options");
        System.out.println("    -i");
        System.out.println("         --instanceId, 实例的ID。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        if(parameters.length == 1){
            System.out.println("参数错误");
            System.out.println("请输入 'deleteInstance' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-i" :
                    index++;
                    if (parameters.length > index){
                        instanceId = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入：'deleteInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }
                /*case "-n" :
                    index++;
                    if(parameters.length > index){
                        instanceName = parameters[index];
                        index++;
                        break;
                    }else{
                        System.out.println("参数错误");
                        System.out.println("请输入：'deleteInstance -help' 查看相关指令");
                        CommandRecord.getInstance().popLastCommand();
                        return false;
                    }*/
                default:
                    System.out.println("参数错误");
                    System.out.println("请输入：'deleteInstance -help' 查看相关指令");
                    CommandRecord.getInstance().popLastCommand();
                    return false;
            }
        }
        if(instanceId==null){
            System.out.println("缺少必要参数");
            System.out.println("请输入：'deleteInstance -help' 查看相关指令");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkParameters() {
        return false;
    }
}
