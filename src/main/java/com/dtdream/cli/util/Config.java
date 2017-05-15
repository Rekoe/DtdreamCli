package com.dtdream.cli.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by thomugo on 2016/8/29.
 */
public class Config {
    public static boolean debug;
    public static String outputFormat;
    public static String system;
    private static String endPoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static Boolean supportCname;
    private static Boolean SLDEnabled;
    private static String region;
    private static String ecsProductHost;
    private static String rdsProductHost;
    private static String slbProductHost;
    private static String ramProductHost;
    private static String zonea;
    private static String zoneb;
    private static String ramProtocol;

    public static ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

    public Config(String system){
        this.system = system;
        Properties pps = new Properties();
        InputStream in = null;
        try {
            if(StringUtils.equals(system, "windows")){
                //绝对路径
                //windows路径
                //in = new FileInputStream("D:\\workspace\\deploy\\DtdreamCli\\src\\main\\resources\\config.properties");
                //本地调试时使用
                in = Config.class.getClassLoader().getResourceAsStream("config.properties");
            }else if(StringUtils.equals(system, "linux")){
                //linux路径
                String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                path = path.substring(0,path.lastIndexOf('/')) + "/config/config.properties";
                //System.out.println(path);
                in = new FileInputStream(path);
            }else{
                System.out.println("系统名错误");
                System.out.println("不支持系统：" + system);
            }

            try {
                pps.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //从输入流中读取属性列表（键和元素对）
            debug = Boolean.parseBoolean(pps.getProperty("debug"));
            outputFormat = pps.getProperty("outputFormat");
            endPoint = pps.getProperty("endPoint");
            accessKeyId = pps.getProperty("accessKeyId");
            accessKeySecret = pps.getProperty("accessKeySecret");
            supportCname = Boolean.parseBoolean(pps.getProperty("supportCname"));
            SLDEnabled = Boolean.parseBoolean(pps.getProperty("SLDEnabled"));
            region = pps.getProperty("region");
            ecsProductHost = pps.getProperty("ecsProductHost");
            rdsProductHost = pps.getProperty("rdsProductHost");
            slbProductHost = pps.getProperty("slbProductHost");
            ramProductHost = pps.getProperty("ramProductHost");
            ramProtocol = pps.getProperty("ramProtocol");
            zonea = pps.getProperty("zonea");
            zoneb = pps.getProperty("zoneb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getEndPoint() {
        return endPoint;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static Boolean getSupportCname() {
        return supportCname;
    }

    public static Boolean getSLDEnabled() {
        return SLDEnabled;
    }

    public static String getEcsProductHost() {
        return ecsProductHost;
    }

    public static String getRegion() {
        return region;
    }

    public static String getZonea() {
        return zonea;
    }

    public static String getZoneb() {
        return zoneb;
    }

    public static String getRdsProductHost(){
        return rdsProductHost;
    }

    public static String getSlbProductHost(){
        return slbProductHost;
    }

    public static String getRamProductHost() {
        return ramProductHost;
    }

    public static String getRamProtocol() {
        return ramProtocol;
    }
}
