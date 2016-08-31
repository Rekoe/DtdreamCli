package com.dtdream.cli.command;

/**
 * Created by thomugo on 2016/8/29.
 */
public abstract class Command implements CommandParser{
    private static final String BACK = "back";
    private static final String GOTO = "goto";
    protected String [] parameters;
    private boolean debug = true;
    private ICommandFactory factory;

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

    public final void execute() {
        System.out.print(CommandRecord.getInstance().getCommandListSize() + ":");
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
            CommandRecord.getInstance().setNextCommand(this.getNextCommand(this.factory, input));
        }

    }

    public final void displayParameters(){
        if(debug){
            if(parameters != null){
                System.out.printf("parse parameters: [ ");
                for (int i = 0; i < parameters.length; i++) {
                    System.out.printf(parameters[i] + " ");
                }
                System.out.println("]");
            }
        }
    }

}
