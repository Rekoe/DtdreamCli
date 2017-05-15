package com.dtdream.cli.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.X509TrustAll;
import com.aliyuncs.profile.DefaultProfile;

/**
 * Created by shumeng on 2016/12/5.
 */
public class RamClient {
    private static DefaultAcsClient client;
    private static Object lock=new Object();

    private RamClient(){}

    public static IAcsClient getInstance() {
        if(client == null){
            synchronized (lock){
                if(client == null){
                    try {
                        DefaultProfile.addEndpoint(Config.getRegion(), Config.getRegion(), "Ram", Config.getRamProductHost());
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                    DefaultProfile profile = DefaultProfile.getProfile(Config.getRegion(), Config.getAccessKeyId(), Config.getAccessKeySecret());
                    client = new DefaultAcsClient(profile);

                    //忽略https协议的ssl证书
                    X509TrustAll.ignoreSSLCertificate();
                }
            }
        }
        return client;
    }
}
