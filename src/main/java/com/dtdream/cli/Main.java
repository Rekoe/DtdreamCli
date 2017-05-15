package com.dtdream.cli;

import com.dtdream.cli.command.CommandExecutor;
import com.dtdream.cli.command.ICommandFactory;
import com.dtdream.cli.ecs.util.EcsCommandFactory;
import com.dtdream.cli.erms.util.ErmsCommandFactory;
import com.dtdream.cli.logo.Logo;
import com.dtdream.cli.logo.LogoCommandFactory;
import com.dtdream.cli.ons.util.MQCommandFactory;
import com.dtdream.cli.oss.util.OssCommandFactory;
import com.dtdream.cli.ram.util.RamCommandFactory;
import com.dtdream.cli.rds.util.RdsCommandFactory;
import com.dtdream.cli.slb.util.SlbCommandFactory;
import com.dtdream.cli.util.Config;
import com.dtdream.cli.util.DtdreamCommandFactory;
import com.dtdream.cli.util.OssClient;
import com.dtdream.cli.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by thomugo on 2016/8/29.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        HashMap<Long, String> hashMap = new HashMap<>();
        Long key1 = 1l;
        hashMap.put(key1,"test1");
        Long key = 1l;
        if(hashMap.containsKey(key)){
            System.out.println("success");
        }else{
            System.out.println("error");
        }


        DtdreamCommandFactory dtdreamCommandFactory = new DtdreamCommandFactory();
        Logo logo = null;
        //System.out.println("read config.properties:");
        Config config = new Config("windows");
        //Config config = new Config("linux");
        logo = new Logo(config.system);

        //System.out.println("AccessKeyId: " + Config.getAccessKeyId());
        //System.out.println("AccessKeySecret: " + Config.getAccessKeySecret());
        //System.out.println("EndPoint " + Config.getEndPoint());
        //System.out.println("SLDEnabled: " + Config.getSLDEnabled());
        //System.out.println("SupportCname: " + Config.getSupportCname());

        //交互模式
        if(args!=null && args.length>0) {
            if(args.length==1 && StringUtils.equals(args[0], "dtdream")) {//进入交互模式
                Scanner scanner = new Scanner(System.in);
                if(logo != null){
                    logo.show();
                }
                while (true) {
                    System.out.printf(">>>");
                    String line = scanner.nextLine();
                    if (StringUtils.equals(line, "exit") || StringUtils.equals(line, "quit")) {
                        logger.info("COMMAND: " + line);
                        //Do not forget to shut down the client finally to release all allocated resources.
                        OssClient.getInstance().shutdown();
                        break;
                    }
                    ///System.out.println(">>>" + line);
                    //String[] commandline = line.split(" ");
                    String[] commandline = StringUtil.getParameters(line);
                    int length = commandline.length;

                    if (commandline[0].equals("dtdream")) {
                        if (length > 1) {
                            logger.info("COMMAND: " + StringUtil.getCommand(commandline));
                            switch (commandline[1]) {
                                case "-help" :
                                case "--help" :
                                case "-h" :
                                case "-H" :
                                    String helpparams[] = Arrays.copyOfRange(commandline, 1, length);
                                    CommandExecutor helpExecutor = new CommandExecutor(dtdreamCommandFactory,
                                            helpparams);
                                    helpExecutor.execute();
                                    break;
                                case "logo" :
                                    String logoparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    ICommandFactory logofactory = new LogoCommandFactory(logo);
                                    CommandExecutor logoExecutor = new CommandExecutor(logofactory,
                                            logoparams);
                                    logoExecutor.execute();
                                    break;
                                case "ecs":
                                    //System.out.println("parse ecs command");
                                    ICommandFactory ecsfactory = new EcsCommandFactory();
                                    String ecsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor ecsExecutor = new CommandExecutor(ecsfactory, ecsparams);
                                    ecsExecutor.execute();
                                    break;
                                case "oss":
                                    //System.out.println("parse oss command");
                                    ICommandFactory ossfactory = new OssCommandFactory();
                                    String ossparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor ossExecutor = new CommandExecutor(ossfactory, ossparams);
                                    ossExecutor.execute();
                                    break;
                                case "erms":
                                    //System.out.println("parse oss command");
                                    ICommandFactory ermsfactory = new ErmsCommandFactory();
                                    String ermsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor ermsExecutor = new CommandExecutor(ermsfactory, ermsparams);
                                    ermsExecutor.execute();
                                    break;
                                case "rds":
                                    //System.out.println("parse oss command");
                                    ICommandFactory rdsfactory = new RdsCommandFactory();
                                    String rdsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor rdsExecutor = new CommandExecutor(rdsfactory, rdsparams);
                                    rdsExecutor.execute();
                                    break;
                                case "slb":
                                    //System.out.println("parse oss command");
                                    ICommandFactory slbfactory = new SlbCommandFactory();
                                    String slbparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor slbExecutor = new CommandExecutor(slbfactory, slbparams);
                                    slbExecutor.execute();
                                    break;
                                case "ram":
                                    //System.out.println("parse oss command");
                                    ICommandFactory ramfactory = new RamCommandFactory();
                                    String ramparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor ramExecutor = new CommandExecutor(ramfactory, ramparams);
                                    ramExecutor.execute();
                                    break;
                                case "mq":
                                    ICommandFactory mqfactory = new MQCommandFactory();
                                    String mqparams[] = Arrays.copyOfRange(commandline, 2, length);
                                    CommandExecutor mqExecutor = new CommandExecutor(mqfactory, mqparams);
                                    mqExecutor.execute();
                                    break;
                                default:
                                    System.out.println("不存在命令模块：" + commandline[1]);
                                    System.out.println("请输入：dtdream -help 查询帮助。");
                                    break;
                            }
                        }
                    }
                }//end while
            }else {//进入命令模式
                String[] commandline = args;
                int length = commandline.length;
                if (commandline[0].equals("dtdream")) {
                    if (length > 1) {
                        logger.info("COMMAND: " + StringUtil.getCommand(commandline));
                        switch (commandline[1]) {
                            case "-help" :
                            case "--help" :
                            case "-h" :
                            case "-H" :
                                String helpparams[] = Arrays.copyOfRange(commandline, 1, length);
                                CommandExecutor helpExecutor = new CommandExecutor(dtdreamCommandFactory,
                                        helpparams);
                                helpExecutor.execute();
                                break;
                            case "logo" :
                                String logoparams[] = Arrays.copyOfRange(commandline, 2, length);
                                ICommandFactory logofactory = new LogoCommandFactory(logo);
                                CommandExecutor logoExecutor = new CommandExecutor(logofactory,
                                        logoparams);
                                logoExecutor.execute();
                                break;
                            case "ecs":
                                //System.out.println("parse ecs command");
                                ICommandFactory ecsfactory = new EcsCommandFactory();
                                String ecsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ecsExecutor = new CommandExecutor(ecsfactory, ecsparams);
                                ecsExecutor.execute();
                                break;
                            case "oss":
                                //System.out.println("parse oss command");
                                ICommandFactory ossfactory = new OssCommandFactory();
                                String ossparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ossExecutor = new CommandExecutor(ossfactory, ossparams);
                                ossExecutor.execute();
                                break;
                            case "erms":
                                //System.out.println("parse oss command");
                                ICommandFactory ermsfactory = new ErmsCommandFactory();
                                String ermsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ermsExecutor = new CommandExecutor(ermsfactory, ermsparams);
                                ermsExecutor.execute();
                                break;
                            case "rds":
                                //System.out.println("parse oss command");
                                ICommandFactory rdsfactory = new RdsCommandFactory();
                                String rdsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor rdsExecutor = new CommandExecutor(rdsfactory, rdsparams);
                                rdsExecutor.execute();
                                break;
                            case "slb":
                                //System.out.println("parse oss command");
                                ICommandFactory slbfactory = new SlbCommandFactory();
                                String slbparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor slbExecutor = new CommandExecutor(slbfactory, slbparams);
                                slbExecutor.execute();
                                break;
                            case "ram":
                                //System.out.println("parse oss command");
                                ICommandFactory ramfactory = new RamCommandFactory();
                                String ramparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ramExecutor = new CommandExecutor(ramfactory, ramparams);
                                ramExecutor.execute();
                                break;
                            case "mq":
                                ICommandFactory mqfactory = new MQCommandFactory();
                                String mqparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor mqExecutor = new CommandExecutor(mqfactory, mqparams);
                                mqExecutor.execute();
                                break;
                            default:
                                System.out.println("不存在命令模块：" + commandline[1]);
                                System.out.println("请输入：dtdream -help 查询帮助。");
                                break;
                        }
                    }
                }
            }

        }else{//默认进入交互模式
            Scanner scanner = new Scanner(System.in);
            if(logo != null){
                logo.show();
            }
            while (true) {
                System.out.printf(">>>");
                String line = scanner.nextLine();
                if (StringUtils.equals(line, "exit") || StringUtils.equals(line, "quit")) {
                    logger.info("COMMAND: " + line);
                    //Do not forget to shut down the client finally to release all allocated resources.
                    OssClient.getInstance().shutdown();
                    break;
                }
                ///System.out.println(">>>" + line);
                //String[] commandline = line.split(" ");
                String[] commandline = StringUtil.getParameters(line);
                int length = commandline.length;

                if (commandline[0].equals("dtdream")) {
                    if (length > 1) {
                        logger.info("COMMAND: " + StringUtil.getCommand(commandline));
                        switch (commandline[1]) {
                            case "-help" :
                            case "--help" :
                            case "-h" :
                            case "-H" :
                                String helpparams[] = Arrays.copyOfRange(commandline, 1, length);
                                CommandExecutor helpExecutor = new CommandExecutor(dtdreamCommandFactory,
                                        helpparams);
                                helpExecutor.execute();
                                break;
                            case "logo" :
                                String logoparams[] = Arrays.copyOfRange(commandline, 2, length);
                                ICommandFactory logofactory = new LogoCommandFactory(logo);
                                CommandExecutor logoExecutor = new CommandExecutor(logofactory,
                                        logoparams);
                                logoExecutor.execute();
                                break;
                            case "ecs":
                                //System.out.println("parse ecs command");
                                ICommandFactory ecsfactory = new EcsCommandFactory();
                                String ecsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ecsExecutor = new CommandExecutor(ecsfactory, ecsparams);
                                ecsExecutor.execute();
                                break;
                            case "oss":
                                //System.out.println("parse oss command");
                                ICommandFactory ossfactory = new OssCommandFactory();
                                String ossparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ossExecutor = new CommandExecutor(ossfactory, ossparams);
                                ossExecutor.execute();
                                break;
                            case "erms":
                                //System.out.println("parse oss command");
                                ICommandFactory ermsfactory = new ErmsCommandFactory();
                                String ermsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ermsExecutor = new CommandExecutor(ermsfactory, ermsparams);
                                ermsExecutor.execute();
                                break;
                            case "rds":
                                //System.out.println("parse oss command");
                                ICommandFactory rdsfactory = new RdsCommandFactory();
                                String rdsparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor rdsExecutor = new CommandExecutor(rdsfactory, rdsparams);
                                rdsExecutor.execute();
                                break;
                            case "slb":
                                //System.out.println("parse oss command");
                                ICommandFactory slbfactory = new SlbCommandFactory();
                                String slbparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor slbExecutor = new CommandExecutor(slbfactory, slbparams);
                                slbExecutor.execute();
                                break;
                            case "ram":
                                //System.out.println("parse oss command");
                                ICommandFactory ramfactory = new RamCommandFactory();
                                String ramparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor ramExecutor = new CommandExecutor(ramfactory, ramparams);
                                ramExecutor.execute();
                                break;
                            case "mq":
                                ICommandFactory mqfactory = new MQCommandFactory();
                                String mqparams[] = Arrays.copyOfRange(commandline, 2, length);
                                CommandExecutor mqExecutor = new CommandExecutor(mqfactory, mqparams);
                                mqExecutor.execute();
                                break;
                            default:
                                System.out.println("不存在命令模块：" + commandline[1]);
                                System.out.println("请输入：dtdream -help 查询帮助。");
                                break;
                        }
                    }
                }
            }//end while
        }


    }
}
