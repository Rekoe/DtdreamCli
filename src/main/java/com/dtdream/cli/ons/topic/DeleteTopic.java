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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Description  
 * @author thomugo
 * @since 1.0.0
 * @date 2017/2/14
 */
@Command(name = "deleteTopic", description = "删除指定Topic")
public class DeleteTopic extends AnnotatedCommand{
    @Option(paraTag = "-F", comment = "命令输出格式", required = true)
    private String outputFormater = "json";
    @Option(paraTag = "-r", comment = "区域ID", required = true)
    private String regionId;
    @Option(paraTag = "-i", comment = "阿里云userID", required = true)
    private String userId;
    @Option(paraTag = "-t", comment = "Topic名称", required = true)
    private String topic;

    private JsonNode responseNode;
    private Logger logger = Logger.getLogger(DeleteTopic.class);

    public DeleteTopic(ICommandFactory factory, String[] parameters) {
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
            GetRequest req = Unirest.get(MessageQueueUtil.getTopicDeleteUrl());
            req.queryString("_accesskey", Config.getAccessKeyId());
            req.queryString("_signature", Config.getAccessKeySecret());
            req.queryString("_userId", userId);
            req.queryString("_regionId", regionId);
            req.queryString("topic", topic);
            long time = System.currentTimeMillis();
            req.queryString("__preventCache", time);
            try {
                HttpResponse<JsonNode> res = req.asJson();
                logger.info("send a deleteTopic request: " + req.getHttpRequest().getUrl());
                logger.info("httpStatus: " + res.getStatus());
                responseNode = res.getBody();
                MessageQueueUtil.printResponseMessage(responseNode);
                System.out.println("status:"+res.getBody().getObject().getString("status"));
                if (res.getStatus() == 200) {
                    if(res.getBody().getObject().getBoolean("success")){
                        logger.info("delete topic succeed");
                    }else if(StringUtils.equals(res.getBody().getObject().getString("status"), "BIZ_TOPIC_NOT_FOUND")){
                        logger.info("topic not exist");
                        System.out.println("topic not exist");
                    }else{
                        logger.error("delete topic: " + topic + "failed");
                        System.out.println("delete topic: " + topic + "failed");
                    }
                } else {
                    logger.error("delete topic failed");
                    System.out.println("delete topic failed");
                }
                logger.info(FormatUtil.formatJson(responseNode.toString()));
            } catch (UnirestException e) {
                logger.error("delete topic failed");
                System.out.println("delete topic failed");
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

    }
}
