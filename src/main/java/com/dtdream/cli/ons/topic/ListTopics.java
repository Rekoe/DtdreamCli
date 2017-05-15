/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2017/2/14
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2017/2/14      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ons.topic;

import com.dtdream.cli.annotation.AnnotatedCommand;
import com.dtdream.cli.annotation.Command;
import com.dtdream.cli.annotation.Option;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ons.util.MessageQueueUtil;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.FormatUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 * @date 2017/2/14
 */
@Command(name = "listTopics", description = "列出当前用户所有Topic")
public class ListTopics extends AnnotatedCommand{
    @Option(paraTag = "-F", comment = "命令输出格式", required = false)
    private String outputFormater = "json";
    @Option(paraTag = "-r", comment = "区域ID", required = true)
    private String regionId;
    @Option(paraTag = "-i", comment = "阿里云userID", required = true)
    private String userId;
    @Option(paraTag = "-t", comment = "topicName")
    private String topic;

    private JsonNode responseNode;
    private Logger logger = Logger.getLogger(ListTopics.class);

    public ListTopics(ICommandFactory factory, String[] parameters) {
        super(factory, parameters);
    }

    @Override
    public boolean checkParameters() {
        return true;
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
    public com.dtdream.cli.command.Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        if(parse(parameters)){
                GetRequest req = Unirest.get(MessageQueueUtil.getTopicListUrl());
                req.queryString("_signature", Config.getAccessKeySecret());
                req.queryString("_accesskey", Config.getAccessKeyId());
                req.queryString("_userId", userId);
                req.queryString("_regionId", regionId);
                if (StringUtils.isNotBlank(topic)) {
                    req.queryString("topic", topic);
                }
                long time = System.currentTimeMillis();
                req.queryString("__preventCache", time);
                try {
                    HttpResponse<String> res = req.asString();
                    //HttpResponse<JsonNode> res = req.asJson();
                    logger.info("send a listTopic request: " + req.getHttpRequest().getUrl());
                    //responseNode = res.getBody();
                    //logger.info(FormatUtil.formatJson(res.getBody().toString()));
                    //MessageQueueUtil.printResponseMessage(responseNode);
                    //System.out.println(req.getUrl());
                    System.out.println(res.getBody().toString());
                    if (res.getStatus() == 200) {
                        /*if (res.getBody().getObject().getBoolean("success")) {
                            logger.info("listTopic succeed");
                            logger.info(FormatUtil.formatJson(res.getBody().getObject().getJSONArray("data").toString()));
                            //System.out.println(res.getBody().getObject().getJSONArray("data").getJSONObject(0).getLong("id"));
                            if (StringUtils.equals("json", outputFormater)) {
                                printJson();
                            } else {
                                printText();
                            }

                        } else {
                            logger.error("listTopic failed");
                            System.out.println("listTopic failed");
                        }*/
                    } else {
                        logger.error("listTopic failed");
                        System.out.println("listTopic failed");
                    }
                    //logger.info(FormatUtil.formatJson(responseNode.toString()));
                } catch (UnirestException e) {
                    logger.error("listTopic failed");
                    System.out.println("listTopic failed");
                } finally {
                    CommandRecord.getInstance().popLastCommand();
                }

        }
    }



    @Override
    public void printText() {

    }

    @Override
    public void printJson() {
        FormatUtil.printJson(responseNode.getObject().getJSONArray("data").toString());
    }


}


