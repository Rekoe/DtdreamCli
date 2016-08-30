package com.dtdream.cli.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomugo on 2016/8/29.
 */
public class CommandRecord {
    //保存命令的队列
    private List<Command> commandTrace = new ArrayList();
    //保存用户输入的map
    private Map<String, Object> recordMap = new HashMap();
    //单例模式的问题记录
    private static CommandRecord commandRecord = new CommandRecord();


    private CommandRecord() {
    }

    public static CommandRecord getInstance() {
        return commandRecord;
    }

    public void setRecord(String key, Object value) {
        this.recordMap.put(key, value);
    }

    public Map<String, Object> getRecords() {
        return this.recordMap;
    }

    public void setNextCommand(Command command) {
        this.commandTrace.add(command);
    }

    public void popLastCommand() {
        int len = this.commandTrace.size();
        this.commandTrace.remove(len - 1);
    }

    public Command getNext() {
        int len = this.commandTrace.size();
        return (Command)this.commandTrace.get(len - 1);
    }

    public boolean hasNext() {
        int len = this.commandTrace.size();
        return (len > 0) ? true : false;
    }

    public void cleanAllCommands() {
        this.commandTrace.clear();
    }

    public Command getGotoCommand(int index) {
        if(index < this.commandTrace.size()) {
            this.commandTrace = this.commandTrace.subList(0, index);
        }
        return this.getNext();
    }

    public int getCommandListSize() {
        return this.commandTrace.size();
    }
}
