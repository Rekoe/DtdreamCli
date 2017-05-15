package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupRequest;
import com.aliyuncs.ecs.model.v20140526.CreateSecurityGroupResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.EcsClient;

/**
 * Created by shumeng on 2016/11/11.
 */
public class CreateSecurityGroup extends Command implements CommandParser{
    private final String COMMAND_NAME = "createSecurityGroup";
    private String securityGroupName;
    private String description;
    private String vpcId;
    public CreateSecurityGroup(EcsCommandFactory factory, String [] parameters) {
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
        if (parse(parameters)) {
            CreateSecurityGroupRequest request = new CreateSecurityGroupRequest();
            request.setDescription(description);
            request.setVpcId(vpcId);
            request.setSecurityGroupName(securityGroupName);
            try{
                CreateSecurityGroupResponse response = EcsClient.getInstance().getAcsResponse(request);
                System.out.println("A new security group: " + response.getSecurityGroupId() + " was created!");
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
        System.out.println("    createSecurityGroup 创建安全组");
        System.out.println("SYNOPSIS");
        System.out.println("    createSecurityGroup [options -i -I] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 新建一个安全组，通过安全组防火墙规则配置实现对一组实例的防火墙配置。一个安全组可包含多个实例。\n" +
                "      新建的安全组，默认只打开组内实例之间的访问权限，其他访问权限全部关闭。若用户允许其他组的实例或来自互联网的访问，则可通过授权安全组权限接口来对安全组防火墙策略进行修改。\n" +
                "      防火墙规则区分内网和公网。\n" +
                "      每个用户最多可创建 100 个安全组。\n" +
                "      创建专有网络类型的安全组，需要指定 VpcId。");
        System.out.println("Options []");
        System.out.println("    -n");
        System.out.println("         --securityGroupName, 安全组名称。");
        System.out.println("    -v");
        System.out.println("         --vpcId, 安全组所在的专有网络。");
        System.out.println("    -d");
        System.out.println("         --description,安全组描述信息。");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=7){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        securityGroupName = parameters[index];
                        index++;
                        break;
                    }else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        vpcId = parameters[index];
                        index++;
                        break;
                    }else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
                        index++;
                        break;
                    }else {
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
