package com.dtdream.cli.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;

/**
 * Created by thomugo on 2016/8/29.
 */
public class OssClient {
    private static OSSClient client;
    private static Object lock=new Object();

    private OssClient(){}

    public static OSSClient getInstance() {
        if(client == null){
            synchronized (lock){
                if(client == null){
                    ClientConfiguration clientConfiguration = new ClientConfiguration();
                    clientConfiguration.setSupportCname(true); //设置支持自定义域名
                    clientConfiguration.setSLDEnabled(true);  //设置开启二级域名
                    client = new OSSClient(Config.getEndPoint(), Config.getAccessKeyId(), Config.getAccessKeySecret(), clientConfiguration);
                }
            }
        }
        return client;
    }

}
