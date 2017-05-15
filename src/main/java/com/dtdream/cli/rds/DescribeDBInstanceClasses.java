package com.dtdream.cli.rds;

import com.dtdream.cli.command.Command;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Created by shumeng on 2016/12/5.
 */
public class DescribeDBInstanceClasses extends Command implements CommandParser{
    private final String COMMAND_NAME = "describeDBInstanceClasses";
    private String engine = "All";

    public DescribeDBInstanceClasses(RdsCommandFactory factory, String [] parameters) {
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
            if(StringUtils.equals(engine, "MySQL") || StringUtils.equals(engine, "All")){
                display_mysql();
            }
            if(StringUtils.equals(engine, "SQLServer") || StringUtils.equals(engine, "All")){
                display_sqlserver();
            }
            if(StringUtils.equals(engine, "PostgreSQL") || StringUtils.equals(engine, "All")){
                display_postgreSQL();
            }
            if(StringUtils.equals(engine, "PPAS") || StringUtils.equals(engine, "All")){
                display_ppas();
            }

            CommandRecord.getInstance().popLastCommand();
        }
    }

    @Override
    public void help() {
        System.out.println("NAME");
        System.out.println("    describeDBInstanceClasses");
        System.out.println("SYNOPSIS");
        System.out.println("    describeDBInstanceClasses [options] ");
        System.out.println("DESCRIPTION");
        System.out.println("Options [-e]");
        System.out.println("    --查询RDS实例类型");
        System.out.println("    -e");
        System.out.println("         --engine, 数据库类型。\n" +
                "         取值范围:[MySQL/SQLServer/PostgreSQL/PPAS；不填则返回所有]。");

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

    private void display_mysql(){
        System.out.println("RDS for MySQL:");
        System.out.println("新实例规格表：");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS");
        System.out.println("\trds.mysql.t1.small\t\t1\t\t1G\t\t300\t\t\t600\n" +
                "\trds.mysql.s1.small\t\t1\t\t2G\t\t600\t\t\t1000\n" +
                "\trds.mysql.s2.large\t\t2\t\t4G\t\t1200\t\t2000\n" +
                "\trds.mysql.s2.xlarge\t\t2\t\t8G\t\t2000\t\t4000\n" +
                "\trds.mysql.s3.large\t\t4\t\t8G\t\t2000\t\t5000\n" +
                "\trds.mysql.m1.medium\t\t4\t\t16G\t\t4000\t\t7000\n" +
                "\trds.mysql.c1.large\t\t8\t\t16G\t\t4000\t\t8000\n" +
                "\trds.mysql.c1.xlarge\t\t8\t\t32G\t\t8000\t\t12000\n" +
                "\trds.mysql.c2.xlarge\t\t16\t\t64G\t\t16000\t\t14000\n" +
                "\trds.mysql.c2.xlp2\t\t16\t\t96G\t\t24000\t\t16000\n" +
                "\trds.mysql.c2.2xlarge\t16\t\t128G\t32000\t\t16000\n" +
                "\trds.mysql.st.d13\t\t30\t\t220G\t64000\t\t20000");
        System.out.println();
        System.out.println("旧实例规格表：");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS\n" +
                "\trds.mys2.small\t\t\t2\t\t240M\t60\t\t\t150\n" +
                "\trds.mys2.mid\t\t\t4\t\t600M\t150\t\t\t300\n" +
                "\trds.mys2.standard\t\t6\t\t1200M\t300\t\t\t600\n" +
                "\trds.mys2.large\t\t\t8\t\t2400M\t600\t\t\t1200\n" +
                "\trds.mys2.xlarge\t\t\t9\t\t6000M\t1500\t\t3000\n" +
                "\trds.mys2.2xlarge\t\t10\t\t12000M\t2000\t\t6000\n" +
                "\trds.mys2.4xlarge\t\t11\t\t24000M\t2000\t\t12000\n" +
                "\trds.mys2.8xlarge\t\t13\t\t48000M\t2000\t\t14000");
        System.out.println();
    }

    private void display_sqlserver(){
        System.out.println("RDS for SQL Server:");
        System.out.println("新实例规格表：");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS\n" +
                "\trds.mssql.s1.small\t\t1\t\t2G\t\t600\t\t\t1000\n" +
                "\trds.mssql.s2.large\t\t2\t\t4G\t\t1200\t\t2000\n" +
                "\trds.mssql.s2.xlarge\t\t2\t\t8G\t\t2000\t\t4000\n" +
                "\trds.mssql.s3.large\t\t4\t\t8G\t\t2000\t\t5000\n" +
                "\trds.mssql.m1.medium\t\t4\t\t16G\t\t4000\t\t7000\n" +
                "\trds.mssql.c1.large\t\t8\t\t16G\t\t4000\t\t8000\n" +
                "\trds.mssql.c1.xlarge\t\t8\t\t32G\t\t8000\t\t12000\n" +
                "\trds.mssql.c2.xlarge\t\t16\t\t64G\t\t16000\t\t14000\n" +
                "\trds.mssql.c2.xlp2\t\t16\t\t96G\t\t24000\t\t16000\n" +
                "\trds.mssql..c2.2xlarge\t16\t\t128G\t32000\t\t16000\n" +
                "\trds.mssql.st.d13\t\t28\t\t220G\t64000\t\t20000");
        System.out.println();
        System.out.println("旧实例规格表：");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS\n" +
                "\trds.mss1.small\t\t\t6\t\t1000M\t100\t\t\t500\n" +
                "\trds.mss1.mid\t\t\t8\t\t2000M\t200\t\t\t1000\n" +
                "\trds.mss1.standard\t\t9\t\t4000M\t400\t\t\t2000\n" +
                "\trds.mss1.large\t\t\t10\t\t6000M\t600\t\t\t3000\n" +
                "\trds.mss1.xlarge\t\t\t11\t\t8000M\t800\t\t\t4000\n" +
                "\trds.mss1.2xlarge\t\t12\t\t12000M\t1200\t\t6000\n" +
                "\trds.mss1.4xlarge\t\t13\t\t24000M\t2000\t\t12000\n" +
                "\trds.mss1.8xlarge\t\t13\t\t48000M\t2000\t\t14000");
        System.out.println();
    }

    private void display_ppas(){
        System.out.println("RDS for SQL PPAS:");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS\n" +
                "\trds.ppas.t1.small\t\t1\t\t1G\t\t100\t\t\t600\n" +
                "\trds.ppas.s1.small\t\t1\t\t2G\t\t200\t\t\t1000\n" +
                "\trds.ppas.s2.large\t\t2\t\t4G\t\t400\t\t\t2000\n" +
                "\trds.ppas.s3.large\t\t4\t\t8G\t\t800\t\t\t5000\n" +
                "\trds.ppas.m1.medium\t\t4\t\t16G\t\t1500\t\t8000\n" +
                "\trds.ppas.c1.xlarge\t\t8\t\t32G\t\t2000\t\t12000\n" +
                "\trds.ppas.c2.xlarge\t\t16\t\t64G\t\t2000\t\t14000\n" +
                "\trds.ppas.c2.2xlarge\t\t16\t\t128G\t3000\t\t16000\n" +
                "\trds.ppas.st.d13\t\t\t30\t\t220G\t4000\t\t20000");
        System.out.println();
    }

    private void display_postgreSQL(){
        System.out.println("RDS for SQL PostgreSQL:");
        System.out.println("\t规格类型代码\t\t\t\tCPU/核\t内存\t\t最大连接数\t最大IOPS\n" +
                "\trds.pg.t1.small\t\t\t1\t\t1G\t\t100\t\t\t600\n" +
                "\trds.pg.s1.small\t\t\t1\t\t2G\t\t200\t\t\t1000\n" +
                "\trds.pg.s2.large\t\t\t2\t\t4G\t\t400\t\t\t2000\n" +
                "\trds.pg.s3.large\t\t\t4\t\t8G\t\t800\t\t\t5000\n" +
                "\trds.pg.c1.large\t\t\t8\t\t16G\t\t1500\t\t8000\n" +
                "\trds.pg.c1.xlarge\t\t8\t\t32G\t\t2000\t\t12000\n" +
                "\trds.pg.c2.xlarge\t\t16\t\t64G\t\t2000\t\t14000\n" +
                "\trds.pg.c2.2xlarge\t\t16\t\t128G\t3000\t\t16000\n" +
                "\trds.pg.st.d13\t\t\t30\t\t220G\t4000\t20000");
        System.out.println();
    }
}
