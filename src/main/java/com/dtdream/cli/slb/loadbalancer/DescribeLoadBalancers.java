package com.dtdream.cli.slb.loadbalancer;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersRequest;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.slb.util.SlbCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.SlbClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by shumeng on 2016/11/30.
 */
public class DescribeLoadBalancers extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeLoadBalancers";
    private String regionId = Config.getRegion();
    private String loadBalancerId;
    //阿里接口中没有该参数
    private String loadBalancerName;
    private String addressType;
    private String netWorkType;
    private String vpcId;
    private String vswitchId;
    private String address;
    private String serverId;

    public DescribeLoadBalancers(SlbCommandFactory factory, String [] parameters) {
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
            DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
            request.setRegionId(regionId);
            request.setLoadBalancerId(loadBalancerId);
            request.setAddressType(addressType);
            request.setAddress(address);
            request.setNetworkType(netWorkType);
            request.setVpcId(vpcId);
            request.setVSwitchId(vswitchId);
            request.setServerId(serverId);
            try{
                DescribeLoadBalancersResponse response = SlbClient.getInstance().getAcsResponse(request);
                List<DescribeLoadBalancersResponse.LoadBalancer> loadBalancers = response.getLoadBalancers();
                if(loadBalancers != null){
                    for(DescribeLoadBalancersResponse.LoadBalancer loadBalancer : loadBalancers){
                        System.out.printf("LoadBalancerID: %-12s  LoadBalancerName: %-23s  LoadBalancerStatus: %-8s " +
                                " AddressType: %-12s  Address: %s\n",
                                loadBalancer.getLoadBalancerId(),
                                loadBalancer.getLoadBalancerName(),
                                loadBalancer.getLoadBalancerStatus(),
                                loadBalancer.getAddressType(),
                                loadBalancer.getAddress());
                        System.out.printf("    NetworkType: %-12s  VpcID: %-17s  CreateTime: %s\n",
                                loadBalancer.getNetworkType(),
                                loadBalancer.getVpcId(),
                                loadBalancer.getCreateTime());
                    }
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
        System.out.println("    describeLoadBalancers");
        System.out.println("SYNOPSIS");
        System.out.println("    describeLoadBalancers [options -r] ");
        System.out.println("DESCRIPTION");
        System.out.println("    -- 查询用户创建的所有LoadBalancer列表。");
        System.out.println("Options");
        System.out.println("    -r ");
        System.out.println("         --regionId, 区域ID");
        System.out.println("    -b");
        System.out.println("         --loadBalancerId, 以负载均衡实例ID作为过滤器进行模糊搜索。支持多值查询。\n" +
                "         取值：可以输入多个，以”,”分割\n" +
                "         默认值：无。不设置该参数表示不使用该参数作为过滤条件。");
        /*System.out.println("    -n");
        System.out.println("         --loadBalancerName, 以负载均衡实例名称作为过滤器进行模糊搜索。支持多值查询。\n" +
                "         取值：可以输入多个，以”,”分割\n" +
                "         默认值：无。不设置该参数表示不使用该参数作为过滤条件。");
        */
        System.out.println("    -t");
        System.out.println("         --networkType, 实例网络类型.\n" +
                "         类型范围：[vpc：返回VPC实例;classic：返回Classic实例；不填，默认返回所有]");
        System.out.println("    -T");
        System.out.println("         --addressType, 以Address类型作为过滤器。\n" +
                "         取值：[internet / intranet。默认值：无] 不设置该参数表示不使用该参数作为过滤条件。");
        System.out.println("    -v");
        System.out.println("         --vpcId, 负载均衡实例Vpcid。");
        System.out.println("    -V");
        System.out.println("         --vswitchId, 负载均衡实例Vswitchid。");
        System.out.println("    -a");
        System.out.println("         --address, 负载均衡实例服务地址。");
        System.out.println("    -s");
        System.out.println("         --serverId, 以后端服务器名称ID（ECS实例ID）作为过滤器。若使用该条件，则只返回挂载了此服务器的负载均衡实例。");


    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=19){
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
                case "-b" :
                    index++;
                    if(parameters.length > index){
                        loadBalancerId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length > index){
                        loadBalancerName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        netWorkType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-T" :
                    index++;
                    if(parameters.length > index){
                        addressType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        vpcId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-V" :
                    index++;
                    if(parameters.length > index){
                        vswitchId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-a" :
                    index++;
                    if(parameters.length > index){
                        address = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        serverId = parameters[index];
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
        if(StringUtils.isBlank(regionId)){
            System.out.println("参数错误，缺少必要参数，regionId不能为空，请输入：describeLoadBalancers -help 查看帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        return true;
    }
}
