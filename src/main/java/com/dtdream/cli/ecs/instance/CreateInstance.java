package com.dtdream.cli.ecs.instance;

import com.aliyuncs.ecs.model.v20140526.CreateInstanceRequest;
import com.aliyuncs.ecs.model.v20140526.CreateInstanceResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.ecs.util.EcsUtil;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.EcsClient;
import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 * Created by thomugo on 2016/10/14.
 */
public class CreateInstance extends Command implements CommandParser {
    private final String COMMAND_NAME = "createInstance";
    private String regionId = Config.getRegion();
    private String zoneId;
    private String imageId;
    private String instanceType;
    private String securityGroupId;
    private String instanceName;
    private String description;
    private String diskCategory = "cloud";
    private String internetChargeType;
    private int internetMaxBandwidthIn = 200;
    private int internetMaxBandwidthOut = 0;
    private String vSwitchId;
    private String passwd;

    public CreateInstance(EcsCommandFactory factory, String [] parameters) {
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
            CreateInstanceRequest request = new CreateInstanceRequest();
            request.setRegionId(regionId);
            request.setZoneId(zoneId);
            request.setImageId(imageId);
            request.setInstanceType(instanceType);
            request.setSecurityGroupId(securityGroupId);
            request.setInstanceName(instanceName);
            request.setDescription(description);
            request.setInternetChargeType(internetChargeType);
            request.setSystemDiskCategory(diskCategory);
            if(StringUtils.isBlank(vSwitchId)){
                request.setInternetMaxBandwidthIn(internetMaxBandwidthIn);
                request.setInternetMaxBandwidthOut(internetMaxBandwidthOut);
            }
            request.setVSwitchId(vSwitchId);
            request.setPassword(passwd);
            try{
                CreateInstanceResponse response = EcsClient.getInstance().getAcsResponse(request);
                String instanceId = response.getInstanceId();
                System.out.println("instance named: " + instanceId + " was created" );
            }catch (ServerException e){
                e.printStackTrace();
            }catch (ClientException e){
                e.printStackTrace();
            }finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    createInstance 创建ECS实例。");
        System.out.println("SYNOPSIS");
        System.out.println("    createInstance [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    " +
                "--创建ECS实例，在创建实例时，必须要选择镜像，用来确定新创建实例的系统盘配置。镜像包含操作系统以及应用软件配置，基于镜像创建实例后，实例的系统盘即为此镜像的完全克隆。\n" +
                "\n" +
                "    --一个实例创建时必须指定加入一个安全组。安全组需要预先创建，可通过创建安全组接口创建，可以在新创建实例时指定，也可通过 修改实例属性 " +
                "的接口来完成实例所属安全组变更。在同一个安全组内的实例内网可以相互访问，但不同安全组之间默认有防火墙隔离，不可相互访问，但可通过安全组授权（通过授权安全组权限接口实现）来设置此安全组的防火墙权限。同一个安全组内的实例数量不能超过1000个，若组内实例数量超出限制，创建实例时若指定该安全组，会提示失败。\n" +
                "\n" +
                "    --在创建实例时，如果参数InternetChargeType的值设置为PayByBandwidth（按固定带宽付费），则InternetMaxBandwidthOut" +
                "的设置值即为所选的固定带宽值；如果参数InternetChargeType的值设置为PayByTraffic（按流量付费），则InternetMaxBandwidthOut只是一个带宽的上限设置，计费以发生的网络流量为依据。设置InternetChargeType和InternetMaxBandwidthOut时，应仔细核算可能发生的带宽费用。\n" +
                "\n" +
                "    --InternetMaxBandwidthIn的值在任何情况下都与计费无关，实例的入数据流量是免费的。\n" +
                "\n" +
                "    --创建实例时，可以选择是否为IO优化实例（IoOptimized）。\n" +
                "\n" +
                "    --实例创建时，系统会根据用户所指定镜像为实例分配一个相应大小的系统盘,系统盘容量=Max{40,ImageSize}；也可以指定系统盘的容量SystemDisk" +
                ".size参数，该参数必须大于等于max{40,ImageSize};同时，可以指定系统盘的种类：普通云盘（cloud）、高效云盘（cloud_efficiency）、SSD云盘（cloud_ssd）、本地 SSD 盘（ephemeral_ssd）。\n" +
                "\n" +
                "    --当实例选择为IO优化实例时，系统盘只能选择高效云盘（cloud_efficiency）及SSD云盘（cloud_ssd）\n" +
                "\n" +
                "ECS不支持单独创建或者添加本地SSD盘，因此本地SSD盘必须在创建实例时指定，实例创建完成后不能再添加。各个实例规格对不同磁盘种类大小的限制见 实例资源规格对照表。\n" +
                "\n" +
                "    --单块普通云盘（cloud）容量最大不能超过2000GB，单块高效云盘（cloud_efficiency）容量最大不超过32TB（32768GB），单块SSD云盘（cloud_ssd" +
                "）容量最大不能超过32TB（32768GB），单块本地 SSD 盘（ephemeral_ssd）容量最大不能超过 800GB。\n" +
                "\n" +
                "    --随实例创建的系统盘，其Portable属性为false，即不支持卸载和挂载操作。系统盘随实例的释放而释放，所以系统盘的DeleteWithInstance的属性为True。\n" +
                "\n" +
                "    --随实例创建的类型为普通云盘（cloud）、高效云盘（cloud_efficiency）或SSD云盘（cloud_ssd）的数据盘，其Portable属性为true" +
                "，即支持卸载和挂载操作。如果数据盘随实例的释放而释放，则DeleteWithInstance的属性的默认值为True；反之，如果数据盘不随实例的释放而释放，则DeleteWithInstance的属性的默认值为False\n" +
                "\n" +
                "    --随实例创建的本地 SSD 盘（ephemeral_ssd），其 Portable 属性为 false，即不支持卸载和挂载操作。本地 SSD 盘随实例的释放而释放，所以其 " +
                "DeleteWithInstance 的属性为 True。\n" +
                "\n" +
                "    --一个实例最多添加4块数据盘；数据盘选择本地 SSD 盘时，系统盘必须为本地 SSD 盘，同时一个实例的本地 SSD 盘总容量不超过 1TB（1024GB，不包括系统盘）。\n" +
                "\n" +
                "    --选择了本地SSD盘的实例，一旦创建后，不能修改实例规格。\n" +
                "\n" +
                "    --实例内存为512M时不能使用 Windows 操作系统；内存为 4G以上时不能使用 32位操作系统。\n" +
                "\n" +
                "    --当系统盘是普通云盘（cloud）、高效云盘（cloud_efficiency）或SSD云盘（cloud_ssd）时，数据盘不能是ephemeral_ssd。\n" +
                "\n" +
                "    --专有网络类型实例只能在创建时指定，这类实例必须且只能属于一个虚拟交换机。\n" +
                "\n" +
                "    --指定VSwitchId创建实例时，SecurityGroupId和VSwitchId要属于同一个专有网络。\n" +
                "\n" +
                "    --VSwitchId和PrivateIpAddress同时指定时，PrivateIpAddress要包含在虚拟交换机的CidrBlock之内。\n" +
                "\n" +
                "    --PrivateIpAddress依赖于VSwitchId，不能单独指定PrivateIpAddress。\n" +
                "\n" +
                "    --若实例付费类型为预付费的包年包月实例（PrePaid）,则在执行付款时默认会使用该账号下可用的优惠券（0折账号除外）\n" +
                "\n" +
                "    --创建完成后，实例为 Stopped 状态");
        System.out.println("Options [-r -g -t -s -v -z -D]");
        System.out.println("    -r");
        System.out.println("       --regionId, 实例所属的Region ID");
        System.out.println("    -z");
        System.out.println("       --zoneId, 可用区ID。");
        System.out.println("    -g");
        System.out.println("       --imageId, 镜像文件ID，表示启动实例时选择的镜像资源。");
        System.out.println("    -t");
        System.out.println("       --instanceType, 实例的资源规则 输入：describeInstanceTypes 查询帮助。");
        System.out.println("    -s");
        System.out.println("       --securityGroupId, 指定新创建实例所属于的安全组代码，同一个安全组内的实例之间可以互相访问。");
        System.out.println("    -n");
        System.out.println("       --instanceName, 实例的显示名称。");
        System.out.println("    -d");
        System.out.println("       --description, 实例的描述信息。");
        System.out.println("    -D");
        System.out.println("       --diskCategory, 系统磁盘类型系，可选值：\n" +
                "         cloud – 普通云盘\n" +
                "         cloud_efficiency – 高效云盘\n" +
                "         cloud_ssd – SSD云盘\n" +
                "         ephemeral_ssd - 本地 SSD 盘\n" +
                "         默认值：cloud。");
        System.out.println("    -I");
        System.out.println("       --internetChargeType, 网络计费类型，按流量计费还是按固定带宽计费。\n" +
                "         可选值：[PayByBandwidth, PayByTraffic]\n" +
                "         如用户不指定，默认是PayByBandwidth。。");
        System.out.println("    -i");
        System.out.println("       --internetMaxBandwidthIn, 公网入带宽最大值，单位为Mbps(Mega bit per second)，取值范围：[1,200]\n" +
                "如果客户不指定，AliyunAPI将自动将入带宽设置成200Mbps。。");
        System.out.println("    -o");
        System.out.println("       --internetMaxBandwidthOut, 公网出带宽最大值，单位为 Mbps(Mega bit per second)，取值范围：\n" +
                "         按带宽计费：[0, 100]。如果客户不指定，API 将自动将出带宽设置成 0Mbps。\n" +
                "         按流量计费：[1, 100]。如果客户不指定，会报错。\n" +
                "         如果需要可以通过阿里云工单系统申请将带宽范围设置成 1 ~ 200Mbps。。");
        System.out.println("    -v");
        System.out.println("       --vSwitchId, 如果是创建VPC类型的实例，需要指定虚拟交换机的ID。");
        System.out.println("    -p");
        System.out.println("       --password, 实例的初始密码。");



    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=23) {
            switch (parameters[index]){
                case "-help" :
                case "--help" :
                    help();
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                case "-r" :
                    index++;
                    if(parameters.length > index){
                        regionId = parameters[index];
                        index++;
                        break;
                    }else {
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-z" :
                    index++;
                    if(parameters.length > index){
                        zoneId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-D" :
                    index++;
                    if(parameters.length > index){
                        diskCategory = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-g" :
                    index++;
                    if(parameters.length > index){
                        imageId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length >  index){
                        instanceType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length >  index){
                        securityGroupId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-n" :
                    index++;
                    if(parameters.length >  index){
                        instanceName = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length >  index){
                        description = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-I" :
                    index++;
                    if(parameters.length >  index){
                        internetChargeType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length >  index){
                        internetMaxBandwidthIn = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-o" :
                    index++;
                    if(parameters.length >  index){
                        internetMaxBandwidthOut = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length >  index){
                        vSwitchId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length >  index){
                        passwd = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                default :
                    advice(COMMAND_NAME);
                    return false;

            }
        }
        return checkParameters();

    }

    @Override
    public boolean checkParameters() {
        if(StringUtils.isBlank(regionId) || StringUtils.isBlank(imageId) ||
                StringUtils.isBlank(instanceType) ||
                StringUtils.isBlank(securityGroupId) ||
                StringUtils.isBlank(zoneId) ||
                StringUtils.isBlank(diskCategory)){
            System.out.println("参数错误，缺少必要参数! 请输入: createInstance -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(StringUtils.equals(internetChargeType,"PayByTraffic") && internetMaxBandwidthOut <= 0){
            System.out.println("按流量计费时，带宽不能为0! 请输入: createInstance -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(!EcsUtil.getSupportDisks(regionId,zoneId).contains(diskCategory)){
            System.out.println("磁盘类型有错，该可用区不支持此种类型的磁盘，请输入：describeZones 查询帮助！！！");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        //判断vswitch 和 securityGroup 是否在一个虚拟专有网络下
        if(StringUtils.isNotBlank(vSwitchId)){
            boolean in = false;
            DescribeSecurityGroupsResponse.SecurityGroup securityGroup = EcsUtil.getSecurityGroup(securityGroupId);
            List<DescribeVSwitchesResponse.VSwitch> vSwitches = EcsUtil.getVSwitchs(securityGroup.getVpcId());
            for(DescribeVSwitchesResponse.VSwitch vSwitch : vSwitches){
                if(StringUtils.equals(vSwitchId, vSwitch.getVSwitchId())){
                    in = true;
                }
            }
            if(!in){
                System.out.println("vSwitch: " + vSwitchId + " 和 securityGroup: " + securityGroupId + " 不在同一个虚拟网络下！");
                System.out.println("请输入: createInstance -help 查询帮助");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
        }
        return true;
    }
}
