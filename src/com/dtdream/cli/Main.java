package com.dtdream.cli;

import com.dtdream.cli.command.CommandExecutor;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.OssClient;
import com.dtdream.cli.util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by thomugo on 2016/8/29.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("read config.properties:");
        try {
            Config config = new Config("config.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AccessKeyId: " + Config.getAccessKeyId());
        System.out.println("AccessKeySecret: " + Config.getAccessKeySecret());
        System.out.println("EndPoint " + Config.getEndPoint());
        System.out.println("SLDEnabled: " + Config.getSLDEnabled());
        System.out.println("SupportCname: " + Config.getSupportCname());

        Scanner scanner = new Scanner(System.in);
        System.out.println("========================欢迎使用DtDream命令行工具====================");
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("exit")){

                //Do not forget to shut down the client finally to release all allocated resources.
                OssClient.getInstance().shutdown();
                break;
            }
            System.out.println(">>>" + line);
            String[] commandline= line.split(" ");
            int length = commandline.length;

            if(commandline[0].equals("dtdream")){
                if(length > 2){
                    switch (commandline[1]){
                        case "ecs" :
                            System.out.println("parse ecs command");
                            break;
                        case "oss" :
                            System.out.println("parse oss command");
                            ICommandFactory factory = new OssCommandFactory();
                            String original[] = Arrays.copyOfRange(commandline, 2, length);
                            CommandExecutor executor = new CommandExecutor(factory,original);
                            executor.execute();
                            break;
                        default: break;
                    }
                }
            }
        }
        System.out.println("bye!!!");

    }
}
