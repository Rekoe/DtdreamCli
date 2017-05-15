package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.AuthorizeSecurityGroupEgressRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/11/17.
 */
public class AuthorizeSecurityGroupEgress extends Command implements CommandParser{
    private final String COMMAND_NAME = "authorizeSecurityGroupEgress";
    private String securityGroupId;
    private String regionId = Config.getRegion();
    private String ipProtocol;
    private String portRange;
    private String destGroupId;
    private String destGroupOwnerAccount; //跨账号操作，暂不支持
    private String destGroupOwnerId;      //跨账号操作，暂不支持
    private String destCidrIp;
    private String policy = "accept";
    private String priority = "1";
    private String nicType = "internet";

    public AuthorizeSecurityGroupEgress(EcsCommandFactory factory, String [] parameters) {
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
            AuthorizeSecurityGroupEgressRequest request = new AuthorizeSecurityGroupEgressRequest();
            request.setSecurityGroupId(securityGroupId);
            request.setRegionId(regionId);
            request.setIpProtocol(ipProtocol);
            request.setPortRange(portRange);
            request.setDestGroupId(destGroupId);
            request.setDestCidrIp(destCidrIp);
            request.setPolicy(policy);
            request.setPriority(priority);
            request.setNicType(nicType);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("添加安全组出方向规则成功！");
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
        System.out.println("    authorizeSecurityGroupEgress 授权安全组Out方向的访问权限");
        System.out.println("SYNOPSIS");
        System.out.println("    authorizeSecurityGroupEgress [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --设定安全组Out方向的访问权限。\n" +
                "         可支持的授权策略为：accept（接受访问），drop (拒绝访问)。\n" +
                "         可支持不同的网络类型，如NicType可选择internet或intranet，分别表示公网或内网。VPC类型的ECS实例，只支持intranet类型。\n" +
                "         单个安全组的授权规则最多为100条。\n" +
                "         安全组的优先级根据创建的时间降序排序匹配。\n" +
                "         规则优先级可选范围为1-100，默认值为1，即最高优先级。数字越大，代表优先级越低。\n" +
                "         相同优先级的授权规则，授权策略为drop的规则优先。\n" +
                "         组和组之间的授权只能在内网，即NicType只能选择intranet。\n" +
                "         安全组规则由两组可选参数中的一组组成：destGroupOwnerAccount、destGroupId、IpProtocol、PortRange、NicType securityGroupId" +
                "、Policy 或者destCidrIp、IpProtocol、PortRange、NicType、Policy、securityGroupId。如匹配的规则已存在将会报错。\n" +
                "         支持两种授权方式： 1、授权同一region(经典网络)/同一VPC(VPC)内其他安全组（允许跨用户授权）对其的访问权限；2、授权指定某IP地址范围（CIDR格式）对其的访问权限");
        System.out.println("Options [(-s -i -p -t -d)|(-c -i -p -t -s)]");
        System.out.println("    -r");
        System.out.println("         --regionId, 安全组所属区域ID");
        System.out.println("    -s");
        System.out.println("         --securityGroupId, 源安全组ID。");
        System.out.println("    -t");
        System.out.println("         --nicType, 网络类型，取值：internet | intranet，默认值为 internet; " +
                "当对安全组进行相互授权时（即指定了destGroupId且没有指定destCidrIp），必须指定NicType为intranet");
        System.out.println("    -P");
        System.out.println("         --policy, 授权策略，参数值可为：accept（接受访问），drop (拒绝访问)  默认值为：accept");
        System.out.println("    -i");
        System.out.println("         --ipProtocol, IP协议，取值：tcp | udp | icmp | gre | all(all表示同时支持四种协议)");
        System.out.println("    -p");
        System.out.println("         --portRange, IP协议相关的端口号范围\n" +
                "         协议为tcp、udp时默认端口号，取值范围为1~65535；例如“1/200”意思是端口号范围为1~200，若输入值为：“200/1”接口调用将报错。\n" +
                "         协议为icmp时端口号范围值为-1/-1；\n" +
                "         gre协议时端口号范围值为-1/-1；\n" +
                "         协议为all时端口号范围值为-1/-1");
        System.out.println("    -v");
        System.out.println("         --priority, 授权策略优先级，参数值可为：1-100 默认值为：1");
        System.out.println("    -d");
        System.out.println("         --destGroupId, " +
                "目的ID，destGroupId或者destCidrIp参数必须设置一项，如果两项都设置，则默认对destCidrIp授权。如果指定了该字段且没有指定destCidrIp" +
                "，则NicType只能选择intranet");
        System.out.println("    -c");
        System.out.println("         --destCidrIp, " +
                "目的IP地址范围，必须采用CIDR格式来指定IP地址范围，默认值为0.0.0.0/0（表示不受限制），其他支持的格式如10.159.6.18/12。仅支持IPV4。");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while(parameters.length>index && parameters.length<=19){
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
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        securityGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        nicType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-P" :
                    index++;
                    if(parameters.length > index){
                        policy = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        ipProtocol = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        portRange = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        priority = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        destGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        destCidrIp = parameters[index];
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
        if(StringUtils.isBlank(regionId) ||
                StringUtils.isBlank(securityGroupId) ||
                StringUtils.isBlank(portRange)){
            System.out.println("参数错误，缺少必要参数，请输入: authorizeSecurityGroupEgress -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if((StringUtils.equals(ipProtocol,"tcp") || StringUtils.equals(ipProtocol, "udp")) && !checkPortRange()){
            System.out.println("端口错误。请输入: authorizeSecurityGroupEgress -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(StringUtils.equals(ipProtocol,"icmp") || StringUtils.equals(ipProtocol,"gre") || StringUtils.equals
                (ipProtocol,"all")){
            if(!StringUtils.isBlank(portRange)){
                System.out.println("当协议采用: icmp | gre | all时 不需指定端口号范围，请输入: authorizeSecurityGroupEgress -help 查询帮助");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
            portRange = "-1/-1";
        }
        if(StringUtils.isBlank(destGroupId) && StringUtils.isBlank(destCidrIp)){
            System.out.println("参数错误，destGroupId或者destCidrIp参数必须设置一项，请输入; authorizeSecurityGroupEgress -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(!StringUtils.isBlank(destGroupId) && StringUtils.equals(nicType, "internet") ||
                !StringUtils.isBlank(destCidrIp) && StringUtils.equals(nicType, "intranet")){
            System.out.println("参数错误，缺少必要参数，请输入: authorizeSecurityGroupEgress -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }

    private boolean checkPortRange(){
        String [] port = portRange.split("/");
        if(port.length != 2){
            return false;
        }
        int start = Integer.parseInt(port[0]);
        int end = Integer.parseInt(port[1]);
        if(start > end){
            return false;
        }
        return true;
    }
}
