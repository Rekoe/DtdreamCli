/**
 * -----------------------------------------------------------------------------
 * Copyright © 2015 DtDream Science and Technology Co.,Ltd. All rights reserved.
 * -----------------------------------------------------------------------------
 * Product:
 * Module Name:
 * Date Created: 2017/2/16
 * Description:
 * -----------------------------------------------------------------------------
 * Modification History
 * DATE            Name           Description
 * -----------------------------------------------------------------------------
 * 2017/2/16      thomugo
 * -----------------------------------------------------------------------------
 */

package com.dtdream.cli.ons.topic;

import com.dtdream.cli.annotation.AnnotatedCommand;
import com.dtdream.cli.annotation.Option;
import com.dtdream.cli.command.Command;
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
 * @date 2017/2/16
 */
@com.dtdream.cli.annotation.Command(name = "getTopic", description = "查询指定topic的详细信息")
public class GetTopic extends AnnotatedCommand{
    @Option(paraTag = "-F", comment = "命令输出格式", required = true)
    private String outputFormater = "json";
    @Option(paraTag = "-r", comment = "区域ID", required = true)
    private String regionId;
    @Option(paraTag = "-i", comment = "阿里云userID", required = true)
    private String userId;
    @Option(paraTag = "-t", comment = "topicName")
    private String topic;

    private JsonNode responseNode;
    private Logger logger = Logger.getLogger(GetTopic.class);

    public GetTopic(ICommandFactory factory, String[] parameters) {
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
    public Command getNextCommand(ICommandFactory var1, String var2) {
        return null;
    }

    @Override
    public void doExecute() {
        if(parse(parameters)){
            GetRequest req = Unirest.get(MessageQueueUtil.getTopicGetUrl());
            req.queryString("_accesskey", Config.getAccessKeyId());
            req.queryString("_signature", Config.getAccessKeySecret());
            req.queryString("_userId", userId);
            req.queryString("_regionId", regionId);
            req.queryString("topic", topic);
            long time = System.currentTimeMillis();
            req.queryString("__preventCache", time);
            try {
                HttpResponse<String> resstr = req.asString();
                HttpResponse<JsonNode> res = req.asJson();
                logger.info("send a listTopic request: " + req.getHttpRequest().getUrl());
                logger.info("httpStatus: " + res.getStatus());
                responseNode = res.getBody();
                MessageQueueUtil.printResponseMessage(responseNode);
                if (res.getStatus() == 200) {
                    if(res.getBody().getObject().getBoolean("success")){
                        logger.info("get topic succeed");
                        if(StringUtils.equals("json", outputFormater)){
                            printJson();
                        }else{
                            printText();
                        }

                    }else{
                        logger.error("get topic failed");
                        System.out.println("get topic failed");
                    }
                } else {
                    logger.error("get topic failed");
                    System.out.println("get topic failed");
                }
                logger.info(FormatUtil.formatJson(responseNode.toString()));
            } catch (UnirestException e) {
                logger.error("get topic failed");
                System.out.println("get topic failed");
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
