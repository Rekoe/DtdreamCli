/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2017/2/13
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2017/2/13      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ons.util;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpHost;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 * @date 2017/2/13
 */
public class MessageQueueUtil {
    private static final String hostName = "100.67.76.9";
    private static final int port = 10003;
    //private static final String baseUrl = "http://mq.console.aliyun.ga";
    private static final String baseUrl = "http://mq.console.aliyun.com";
    private static final String topicCreateUrl = baseUrl + "/json/topic/create";
    private static final String topicListUrl = baseUrl + "/json/topic/list";
    private static final String TopicDeleteUrl = baseUrl + "/json/topic/delete";
    private static final String topicGetUrl = baseUrl + "json/topic/get";

    static {
        HttpHost httpHost = new HttpHost(MessageQueueUtil.getHostName(), MessageQueueUtil.getPort());
        Unirest.setProxy(httpHost);
    }

    public static String getTopicCreateUrl() {
        return topicCreateUrl;
    }

    public static String getTopicListUrl() {
        return topicListUrl;
    }

    public static String getTopicDeleteUrl() {
        return TopicDeleteUrl;
    }

    public static String getTopicGetUrl() {
        return topicGetUrl;
    }

    public static String getHostName() {
        return hostName;
    }

    public static int getPort() {
        return port;
    }

    public static void printResponseMessage(JsonNode node){
        System.out.println(node.getObject().getString("message"));
    }



}
