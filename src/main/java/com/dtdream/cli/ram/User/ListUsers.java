package com.dtdream.cli.ram.User;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.ram.model.v20150501.ListUsersRequest;
import com.aliyuncs.ram.model.v20150501.ListUsersResponse;
import com.dtdream.cli.command.*;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.FormatUtil;
import com.dtdream.cli.util.RamClient;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shumeng on 2016/12/5.
 */
public class ListUsers extends Command implements CommandParser, CommandOutPuter{
    private final String COMMAND_NAME = "listUsers";
    private String outputFormat = Config.outputFormat;
    private int pageSize = 100;
    private int pageNum = 1;
    private String nextMarker = null;
    private boolean nextPage = false;
    private List<ListUsersResponse.User> users  = new ArrayList<>();

    public ListUsers(RamCommandFactory factory, String [] parameters) {
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
            IAcsClient client = RamClient.getInstance();
            int index_num = 1;
            if(pageNum == -1){
                pageSize = 100;
            }
            try{
                ListUsersResponse response;
                do {
                    ListUsersRequest request = new ListUsersRequest();
                    request.setMaxItems(pageSize);
                    request.setMarker(nextMarker);
                    request.setProtocol(ProtocolType.HTTPS);
                    response = client.getAcsResponse(request);
                    if(index_num==pageNum || pageNum==-1){
                        users.addAll(response.getUsers());
                    }
                    nextMarker = response.getMarker();
                    index_num++;
                    if(pageNum==-1 || index_num<=pageNum){
                        nextPage = true;
                    }
                }while (response.getIsTruncated() && nextPage);

                if(StringUtils.equals(outputFormat, "text")){
                    printText();
                }else{
                    printJson();
                }

            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }

    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    listUsers");
        System.out.println("SYNOPSIS");
        System.out.println("    listUsers [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --列出当前云账号下的所有ram用户");
        System.out.println("    -n");
        System.out.println("         --pageNum, 分页号。\n" +
                "         当查询所有ram用户时，分页号可设置为：-1(此时pageSize参数无效)");
        System.out.println("    -s");
        System.out.println("         --pageSize, 默认值：100。 取值范围：[1-100]。" +
                "         指定返回结果的条数，当返回结果达到MaxItems限制被截断时，返回参数IsTruncated将等于true");
        System.out.println("    -F");
        System.out.println("         --outputFormat, 命令行输出格式, 目前支持两种输出格式：[json, text(制表符分隔的文本)]");
    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=7){
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    advice(null);
                    return false;
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
                case "-F" :
                    index++;
                    if(parameters.length > index){
                        outputFormat = parameters[index];
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
        if(StringUtils.isBlank(outputFormat)){
            System.out.println("命令行结果输出格式尚未设置，请输入：listUsers -help 查询帮助");
        }
        return true;
    }

    @Override
    public void printText() {
        System.out.println("UserID\t\t\t\tUserName:\t\t\t\tDisplayName\t\t\t\tMobilePhone\t\tEmail\t\t" +
                "\t\t\tCreateDate\t\t\t\tUpdateDate\t\t\t\tComments");
        for(ListUsersResponse.User user : users){
            System.out.printf("%-18s\t%-20s\t%-20s\t%-12s\t%-20s\t%-20s\t%-20s\t%s\n",
                    user.getUserId(),
                    user.getUserName(),
                    user.getDisplayName(),
                    user.getMobilePhone(),
                    user.getEmail(),
                    user.getCreateDate(),
                    user.getUpdateDate(),
                    user.getComments());

        }
    }

    @Override
    public void printJson() {
        String jsonStr = JSON.toJSONString(users);
        FormatUtil.printJson(jsonStr);
    }
}
