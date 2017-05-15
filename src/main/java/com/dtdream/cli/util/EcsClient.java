package com.dtdream.cli.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * Created by thomugo on 2016/10/12.
 */
public class EcsClient {
    private static DefaultAcsClient client;
    private static Object lock=new Object();

    private EcsClient(){}

    public static IAcsClient getInstance() {
        if(client == null){
            synchronized (lock){
                if(client == null){
                   try {
                        DefaultProfile.addEndpoint(Config.getRegion(), Config.getRegion(), "Ecs", Config.getEcsProductHost());
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                    DefaultProfile profile = DefaultProfile.getProfile(Config.getRegion(), Config.getAccessKeyId(), Config.getAccessKeySecret());
                    client = new DefaultAcsClient(profile);
                }
            }
        }
        return client;
    }
}
