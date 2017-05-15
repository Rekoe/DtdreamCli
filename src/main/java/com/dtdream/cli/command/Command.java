package com.dtdream.cli.command;

import com.dtdream.cli.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by thomugo on 2016/8/29.
 */
public abstract class Command{
    Logger logger = Logger.getLogger(Command.class);
    private static final String BACK = "back";
    private static final String GOTO = "goto";
    protected String [] parameters;
    private boolean debug = Config.debug;
    protected ICommandFactory factory;

    public Command(ICommandFactory factory) {
        this.factory = factory;
    }

    public abstract String getInput();

    public abstract boolean checkInput(String var1);

    public abstract String getCommandKey();

    public abstract Command getNextCommand(ICommandFactory var1, String var2);

    public abstract void doExecute();

    public abstract void help();

    public boolean isBack(String input) {
        return input.equalsIgnoreCase("back");
    }

    public boolean isGoto(String input) {
        return input.startsWith("goto");
    }

    /**
     * Description: 命令的执行
     *
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    public final void execute() {
        //System.out.print(CommandRecord.getInstance().getCommandListSize() + ":");
        String input = this.getInput();
        if(input == null){
            doExecute();
        }else if(this.isBack(input)) {
            CommandRecord.getInstance().popLastCommand();
        } else if(this.isGoto(input)) {
            String index = input.substring(5);
            try {
                CommandRecord.getInstance().getGotoCommand(Integer.valueOf(index).intValue());
            } catch (Exception var) {
                System.out.println("If you want to goto another question, please type \'goto x\', \'x\' is a number.");
            }
        } else if(this.checkInput(input)) {
            CommandRecord.getInstance().setRecord(this.getCommandKey(), input);
            doExecute();
            CommandRecord.getInstance().setNextCommand(this.getNextCommand(this.factory, input));
        }else{
            CommandRecord.getInstance().deleteCommand(this);
        }

    }

    /**
     * Description: 打印用户输入
     *
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    protected final void displayParameters(){
        if(debug){
            if(parameters != null){
                System.out.printf("parse parameters: [ ");
                for (int i = 0; i < parameters.length; i++) {
                    System.out.printf(parameters[i] + " ");
                }
                System.out.println("]");
            }
        }
        if(parameters != null){
            logger.debug("parse parameters: [ ");
            for (int i = 0; i < parameters.length; i++) {
                logger.debug(parameters[i] + " ");
            }
            logger.debug("]");
        }
    }

    /**
     * Description: 命令提示函数
     *
     * @param commandName 命令名称
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    public void advice(String commandName){
        if(StringUtils.isEmpty(commandName)){
            help();
            CommandRecord.getInstance().popLastCommand();
        }else if(StringUtils.isNotBlank(commandName)){
            System.out.println("参数错误：请输入: " + commandName +"  -help 查看帮助");
            CommandRecord.getInstance().popLastCommand();
        }
    }

}
