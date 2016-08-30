package com.dtdream.cli.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by thomugo on 2016/8/29.
 */
public class Config {
    private static String endPoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static Boolean supportCname;
    private static Boolean SLDEnabled;

    public Config(String fileName) throws FileNotFoundException, IOException {
        Properties pps = new Properties();
        pps.load(Config.class.getClassLoader().getResourceAsStream(fileName));
        //从输入流中读取属性列表（键和元素对）
        endPoint = pps.getProperty("endPoint");
        accessKeyId = pps.getProperty("accessKeyId");
        accessKeySecret = pps.getProperty("accessKeySecret");
        supportCname = Boolean.parseBoolean(pps.getProperty("supportCname"));
        SLDEnabled = Boolean.parseBoolean(pps.getProperty("SLDEnabled"));
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

}
