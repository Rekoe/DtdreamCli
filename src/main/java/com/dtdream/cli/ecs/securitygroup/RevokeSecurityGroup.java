package com.dtdream.cli.ecs.securitygroup;

import com.aliyuncs.ecs.model.v20140526.RevokeSecurityGroupRequest;
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
public class RevokeSecurityGroup extends Command implements CommandParser{
    private final String COMMAND_NAME = "revokeSecurityGroup";
    private String securityGroupId;
    private String regionId = Config.getRegion();
    private String ipProtocol;
    private String portRange;
    private String sourceGroupId;
    private String sourceGroupOwnerAccount; //跨账号操作，暂不支持
    private String sourceGroupOwnerId;      //跨账号操作，暂不支持
    private String sourceCidrIp;
    private String policy = "accept";
    //private String priority = "1";
    private String nicType = "internet";

    public RevokeSecurityGroup(EcsCommandFactory factory, String [] parameters) {
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
            RevokeSecurityGroupRequest request = new RevokeSecurityGroupRequest();
            request.setSecurityGroupId(securityGroupId);
            request.setRegionId(regionId);
            request.setIpProtocol(ipProtocol);
            request.setPortRange(portRange);
            request.setSourceGroupId(sourceGroupId);
            request.setSourceCidrIp(sourceCidrIp);
            request.setPolicy(policy);
            request.setNicType(nicType);
            try{
                EcsClient.getInstance().getAcsResponse(request);
                System.out.println("撤销成功！！");
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
        System.out.println("    revokeSecurityGroup 撤销安全组In方向的访问权限");
        System.out.println("SYNOPSIS");
        System.out.println("    revokeSecurityGroup [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --撤销安全组的访问权限支持两种方式：一是撤销本Region内(经典网络)/本VPC内(VPC)" +
                "         其他安全组采用指定协议通过指定端口对本安全组的授权规则；二是撤销IP网段采用指定协议通过指定端口访问本安全组，只有调用授权接口授权的权限条目才可以删除。\n" +
                "         安全组规则由两组可选参数中的一组组成：\n" +
                "         SourceGroupOwnerAccount、SourceGroupId、IpProtocol、PortRange、NicType、Policy\n" +
                "         或者SourceCidrIp、IpProtocol、PortRange、NicType、Policy。如匹配的规则找不到将会报错。");
        System.out.println("Options [(-s -i -p -t -d)|(-c -i -p -t -d)]");
        System.out.println("    -r");
        System.out.println("         --regionId, 安全组所属区域ID");
        System.out.println("    -d");
        System.out.println("         --securityGroupId, 目标安全组ID。");
        System.out.println("    -t");
        System.out.println("         --nicType, 网络类型，取值：internet | intranet，默认值为 internet; " +
                "当对安全组进行相互授权时（即指定了SourceGroupId且没有指定SourceCidrIp），必须指定NicType为intranet");
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
        System.out.println("    -s");
        System.out.println("         --sourceGroupId, 源安全组ID，SourceGroupId或者SourceCidrIp参数必须设置一项，如果两项都设置，则默认对SourceCidrIp授权。如果指定了该字段且没有指定SourceCidrIp，则NicType只能选择intranet");
        System.out.println("    -c");
        System.out.println("         --sourceCidrIp, 源IP地址范围，必须采用CIDR格式来指定IP地址范围，默认值为0.0.0.0/0（表示不受限制），其他支持的格式如10.159.6.18/12。仅支持IPV4。");

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
                case "-d" :
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
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        sourceGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        sourceCidrIp = parameters[index];
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
            System.out.println("参数错误，缺少必要参数，请输入: revokeSecurityGroup -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if((StringUtils.equals(ipProtocol,"tcp") || StringUtils.equals(ipProtocol, "udp")) && !checkPortRange()){
            System.out.println("端口错误。请输入: revokeSecurityGroup -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(StringUtils.equals(ipProtocol,"icmp") || StringUtils.equals(ipProtocol,"gre") || StringUtils.equals
                (ipProtocol,"all")){
            if(!StringUtils.isBlank(portRange)){
                System.out.println("当协议采用: icmp | gre | all时 不需指定端口号范围，请输入: revokeSecurityGroup -help 查询帮助");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
            portRange = "-1/-1";
        }
        if(StringUtils.isBlank(sourceGroupId) && StringUtils.isBlank(sourceCidrIp)){
            System.out.println("参数错误，SourceGroupId或者SourceCidrIp参数必须设置一项，请输入; revokeSecurityGroup -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(!StringUtils.isBlank(sourceGroupId) && StringUtils.equals(nicType, "internet") ||
                !StringUtils.isBlank(sourceCidrIp) && StringUtils.equals(nicType, "intranet")){
            System.out.println("参数错误，缺少必要参数，请输入: revokeSecurityGroup -help 查询帮助");
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
