package com.dtdream.cli.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shumeng on 2016/11/7.
 */
public class StringUtil {
    private static final String REGEX = "\\B\"([^\"]*\\S+)\"\\B";
    private static final String REPLACE = "@";
    public static String [] getParameters(String commandline){
        String [] parameters = null;
        //System.out.printf(commandline);
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(commandline);
        //System.out.println(matcher.groupCount());
        Queue<String> strPara = new LinkedList<String>();
        //正则查找双引号包裹的字符串
        while (matcher.find()){
            //System.out.println(matcher.group().replace("\"\"",""));
            strPara.add(matcher.group().replaceAll("\"",""));
        }
        //将输入参数替换并保存为字符串数组
        if(strPara.size() > 0){
            String temp = matcher.replaceAll(REPLACE);
            String [] paras = temp.split(" ");
            for (int i = 0; i < paras.length; i++) {
                if(paras[i].equals(REPLACE)){
                    paras[i] = strPara.poll();
                }
            }
            parameters = paras;
        }else{
            parameters = commandline.split(" ");
        }


        return parameters;
    }

    public static String getCommand(String [] commandline){
        StringBuilder command = new StringBuilder();
        for(int i = 0; i < commandline.length; i++){
            command.append(commandline[i]);
            command.append(' ');
        }
        return command.toString();
    }
}
