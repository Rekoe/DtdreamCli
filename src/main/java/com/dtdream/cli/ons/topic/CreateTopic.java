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
 * @date 2017/2/13
 */
@com.dtdream.cli.annotation.Command(name = "createTopic", description = "创建新的topic")
public class CreateTopic extends AnnotatedCommand{
    @Option(paraTag = "-F", comment = "命令输出格式", required = true)
    private String outputFormater = "json";
    @Option(paraTag = "-r", comment = "区域ID", required = true)
    private String regionId;
    @Option(paraTag = "-i", comment = "阿里云userID", required = true)
    private String userId;
    @Option(paraTag = "-o", comment = "是否顺序队列", required = true)
    private Boolean order;
    @Option(paraTag = "-t", comment = "topic名称", required = true)
    private String topic;
    @Option(paraTag = "-k", comment = "appKey")
    private String appKey;
    @Option(paraTag = "-n", comment = "所属应用名")
    private String appName;
    @Option(paraTag = "-c", comment = "待插入集群")
    private String cluster;
    @Option(paraTag = "-R", comment = "备注")
    private String remark;

    private JsonNode responseNode;
    static Logger logger = Logger.getLogger(CreateTopic.class);

    public CreateTopic(ICommandFactory factory, String[] parameters) {
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
            GetRequest req = Unirest.get(MessageQueueUtil.getTopicCreateUrl());
            req.queryString("_signature", Config.getAccessKeySecret());
            req.queryString("_accesskey", Config.getAccessKeyId());
            req.queryString("_userId", userId);
            req.queryString("_regionId", regionId);
            req.queryString("topic", topic);
            req.queryString("order", order);
            req.queryString("appName", appName);
            req.queryString("cluster", cluster);
            long time = System.currentTimeMillis();
            req.queryString("__preventCache", time);
            if(StringUtils.isNotBlank(remark)){
                req.queryString("remark", remark);
            }
            if(StringUtils.isNotBlank(appKey)){
                req.queryString("appkey", appKey);
            }
            try {
                HttpResponse<JsonNode> res = req.asJson();
                logger.info("send a createTopic request: " + req.getHttpRequest().getUrl());
                logger.info("httpStatus: " + res.getStatus());
                logger.info(res.getBody().toString());
                if (res.getStatus() == 200) {
                    if(res.getBody().getObject().getBoolean("success")){
                        logger.info("createTopic [" + topic + "] succeed");
                        System.out.println("createTopic [" + topic + "] succeed");
                    }else{
                        logger.error("createTopic failed");
                        System.out.println("createTopic failed");
                    }
                } else {
                    logger.error("createTopic failed");
                    System.out.println("createTopic failed");
                }
                responseNode = res.getBody();
                MessageQueueUtil.printResponseMessage(responseNode);
                logger.info(FormatUtil.formatJson(responseNode.toString()));
            } catch (UnirestException e) {
                logger.error("createTopic failed");
                System.out.println("createTopic failed");
            } finally {
                CommandRecord.getInstance().popLastCommand();
            }
        }
    }

    @Override
    public void printText() {}

    @Override
    public void printJson() {}
}
