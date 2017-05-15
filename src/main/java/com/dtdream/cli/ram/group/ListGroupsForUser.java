package com.dtdream.cli.ram.group;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.ram.model.v20150501.ListGroupsForUserRequest;
import com.aliyuncs.ram.model.v20150501.ListGroupsForUserResponse;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.util.RamClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/12/6.
 */
public class ListGroupsForUser extends Command implements CommandParser{
    private final String COMMAND_NAME = "listGroupsForUser";
    private String userName;
    public ListGroupsForUser(RamCommandFactory factory, String [] parameters) {
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
            ListGroupsForUserRequest request = new ListGroupsForUserRequest();
            request.setUserName(userName);
            try{
                ListGroupsForUserResponse response = RamClient.getInstance().getAcsResponse(request);
                List<ListGroupsForUserResponse.Group> groups = response.getGroups();
                System.out.println("Find: " + groups.size() + " groups");
                System.out.println("GroupName\t\t\t\t\t\tJoinedDate\t\t\t\tComments");
                for (ListGroupsForUserResponse.Group group : groups){
                    System.out.printf("%-30s\t%-20s\t%s\n",
                            group.getGroupName(),
                            group.getJoinDate(),
                            group.getComments());
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
        System.out.println("    listGroupForUser");
        System.out.println("SYNOPSIS");
        System.out.println("    listGroupForUser [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --列出指定子用户所加入的组信息");
        System.out.println("Options [-n]");
        System.out.println("    -n");
        System.out.println("         --userName");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=3){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        userName = parameters[index];
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
        if(StringUtils.isBlank(userName)){
            System.out.println("参数错误，缺少必要参数：userName 不能为空，请输入：listGroupsForUser -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
