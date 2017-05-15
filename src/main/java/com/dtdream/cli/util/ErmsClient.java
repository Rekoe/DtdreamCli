package com.dtdream.cli.util;

import com.aliyuncs.DefaultAcsClient;

/**
 * Created by shumeng on 2016/11/22.
 */
public class ErmsClient {
    private static DefaultAcsClient client;
    private static Object lock=new Object();

    private ErmsClient(){}

}
