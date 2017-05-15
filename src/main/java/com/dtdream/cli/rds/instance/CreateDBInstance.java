package com.dtdream.cli.rds.instance;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.rds.model.v20140815.CreateDBInstanceRequest;
import com.aliyuncs.rds.model.v20140815.CreateDBInstanceResponse;
import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.RdsClient;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * Created by shumeng on 2016/12/5.
 */
public class CreateDBInstance extends Command implements CommandParser{
    private final String COMMAND_NAME = "createDBInstance";
    private String regionId = Config.getRegion();
    private String zoneId;
    private String engine;
    private String engineVersion;
    private String instanceClass;
    private int instanceStorage;
    private String instanceNetType;
    private String description;
    private String securityIPList;
    private String payType = "Postpaid";
    private String period;
    private String usedTime;
    //private String clientToken;
    private String instanceNetworkType;
    private String vpcId;
    private String vswitchId;
    private String privateIpAddress;

    public CreateDBInstance(RdsCommandFactory factory, String [] parameters) {
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
            CreateDBInstanceRequest request = new CreateDBInstanceRequest();
            request.setRegionId(regionId);
            request.setZoneId(zoneId);
            request.setEngine(engine);
            request.setEngineVersion(engineVersion);
            request.setDBInstanceClass(instanceClass);
            request.setDBInstanceStorage(instanceStorage);
            request.setDBInstanceNetType(instanceNetType);
            request.setDBInstanceDescription(description);
            request.setSecurityIPList(securityIPList);
            request.setPayType(payType);
            request.setPeriod(period);
            request.setUsedTime(usedTime);
            request.setInstanceNetworkType(instanceNetworkType);
            request.setVPCId(vpcId);
            request.setVSwitchId(vswitchId);
            request.setPrivateIpAddress(privateIpAddress);
            request.setClientToken(UUID.randomUUID().toString());
            try{
                CreateDBInstanceResponse response = RdsClient.getInstance().getAcsResponse(request);
                System.out.println("A new dbinstance was created!!");
                System.out.println("    DBInstanceID(实例ID): " + response.getDBInstanceId());
                System.out.println("    OrderId(订单ID): " + response.getOrderId());
                System.out.println("    ConnectionString(数据库连接地址): " + response.getConnectionString());
                System.out.println("    Port(数据库连接端口): " + response.getPort());

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
        System.out.println("    createDBInstance");
        System.out.println("SYNOPSIS");
        System.out.println("    createDBInstance [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("    --创建RDS实例列表");
        System.out.println("Options [-r -e -v -c -s -S -t(Internet)] || [-r -e -v -c -s -S -t(Intranet) -T VPC -i -I]");
        System.out.println("    -r ");
        System.out.println("         --regionId, 区域ID");
        System.out.println("    -z ");
        System.out.println("         --zoneId, 可用区ID");
        System.out.println("    -e");
        System.out.println("         --engine, 数据库类型。\n" +
                "         取值范围:[MySQL/SQLServer/PostgreSQL/PPAS；不填则返回所有]。");
        System.out.println("    -v");
        System.out.println("         --engineVersion, 数据库版本号，取值如下：\n" +
                "         MySQL：5.5/5.6；\n" +
                "         SQLServer：2008r2/2012（2012默认为单机实例）；\n" +
                "         PostgreSQL：9.4；\n" +
                "         PPAS：9.3");
        System.out.println("    -c");
        System.out.println("         --instanceClass, 实例规格。");
        System.out.println("    -T");
        System.out.println("         --instanceNetworkType, 实例网络类型.\n" +
                "         类型范围：[VPC：返回VPC实例;Classic：返回Classic实例；不填，默认返回所有]");
        System.out.println("    -t");
        System.out.println("         --instanceNetType, 实例的网络连接类型：Internet代表公网，Intranet代表私网。");
        System.out.println("    -d");
        System.out.println("         --descriptions, 实例描述信息");
        System.out.println("    -s");
        System.out.println("         --securityIPList, " +
                "允许访问该实例下所有数据库的IP名单，以逗号隔开，不可重复，最多1000个；支持格式：%，0.0.0.0/0，10.23.12.24（IP），或者10.23.12.24/24（CIDR" +
                "模式，无类域间路由，/24表示了地址中前缀的长度，范围[1,32]）其中，0.0.0.0/0，表示不限制。");
        System.out.println("    -S");
        System.out.println("         --instanceStorage, 自定义存储空间，取值范围：MySQL为[5,2000]，sql " +
                "server为[10，2000]，PostgreSQL和PPAS为[5,2000]。每5G进行递增。单位：GB.");
        System.out.println("    -p");
        System.out.println("         --payType, 付费类型。Postpaid：后付费实例；Prepaid：预付费实例。专有云采用后付费类型。");
        System.out.println("    -P");
        System.out.println("         --period, 若付费类型为Prepaid则该入参必须传入。指定预付费实例为包年或者包月类型，Year：包年；Month：包月。");
        System.out.println("    -u");
        System.out.println("         --usedTime, 若付费类型为Prepaid则该入参必须传入。指定购买时长，可按需传入1，2，3等数值。");

        System.out.println("    -i");
        System.out.println("         --vpcId");
        System.out.println("    -I");
        System.out.println("         --vswitchId");
        System.out.println("    -a");
        System.out.println("         --privateIpAddress");

    }

    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int index = 1;
        while (parameters.length>index && parameters.length<=35){
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
                case "-e" :
                    index++;
                    if(parameters.length > index){
                        engine = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-v" :
                    index++;
                    if(parameters.length > index){
                        engineVersion = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-c" :
                    index++;
                    if(parameters.length > index){
                        instanceClass = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-t" :
                    index++;
                    if(parameters.length > index){
                        instanceNetType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-T" :
                    index++;
                    if(parameters.length > index){
                        instanceNetworkType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-d" :
                    index++;
                    if(parameters.length > index){
                        description = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-s" :
                    index++;
                    if(parameters.length > index){
                        securityIPList = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-S" :
                    index++;
                    if(parameters.length > index){
                        instanceStorage = Integer.parseInt(parameters[index]);
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-p" :
                    index++;
                    if(parameters.length > index){
                        payType = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-P" :
                    index++;
                    if(parameters.length > index){
                        period = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-u" :
                    index++;
                    if(parameters.length > index){
                        usedTime = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-i" :
                    index++;
                    if(parameters.length > index){
                        vpcId = parameters[index];
                        index++;
                        break;
                    }else{
                        advice(COMMAND_NAME);
                        return false;
                    }
                case "-I" :
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
                        privateIpAddress = parameters[index];
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
                StringUtils.isBlank(engine) ||
                StringUtils.isBlank(engineVersion) ||
                StringUtils.isBlank(instanceClass) ||
                StringUtils.isBlank(instanceNetType) ||
                StringUtils.isBlank(securityIPList) ||
                StringUtils.isBlank(payType)
                ){
            System.out.println("参数错误，缺少必要参数，请输入：createDBInstance -help 查询帮助。");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(StringUtils.equals(instanceNetType, "Intranet")){
            if(StringUtils.isBlank(instanceNetworkType) ||
                    StringUtils.isBlank(vpcId) ||
                    StringUtils.isBlank(vswitchId)){
                System.out.println("创建私网类型rds实例时 instanceNetworkType, vpcId, vswitchId 不能为空");
            }
        }
        if(instanceStorage<5 || instanceStorage>2000){
            System.out.println("InstanceStorage 参数不合法。存贮空间范围应在[5,2000], 请输入：createDBInstance -help 查询帮助");
            CommandRecord.getInstance().popLastCommand();
            return false;
        }
        if(StringUtils.equals(payType, "Prepaid")){
            if(StringUtils.isBlank(period) || StringUtils.isBlank(usedTime)){
                System.out.println("付费类型为：Prepaid 预付费类型是参数：period 和 usedTime不能为空，请输入createDBInstance -help 查询帮助。");
                CommandRecord.getInstance().popLastCommand();
                return false;
            }
        }
        return true;
    }
}
