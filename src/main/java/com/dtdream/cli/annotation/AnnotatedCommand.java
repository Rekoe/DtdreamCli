package com.dtdream.cli.annotation;

import com.dtdream.cli.command.CommandOutPuter;
import com.dtdream.cli.command.CommandParser;
import com.dtdream.cli.command.CommandRecord;
import com.dtdream.cli.command.ICommandFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by shumeng on 2016/12/7.
 */
public abstract class AnnotatedCommand extends com.dtdream.cli.command.Command implements CommandParser, CommandOutPuter{
    private Logger logger = Logger.getLogger(AnnotatedCommand.class);
    //private AnnotatedCommand command;
    private String COMMAND_NAME;
    private Class commandClass = this.getClass();
    private HashMap<String, Field> annotatedParameters = new HashMap<>();
    private List<Field> hasDefaultValueFiels = new ArrayList<>();
    //命令的必要参数
    private List<Field> requiredParameters = new ArrayList<>();

    public AnnotatedCommand(ICommandFactory factory, String [] parameters) {
        super(factory);
        this.parameters = parameters;
        if (commandClass.isAnnotationPresent(com.dtdream.cli.annotation.Command.class)) {
            com.dtdream.cli.annotation.Command command = (com.dtdream.cli.annotation.Command) commandClass.getAnnotation(com.dtdream.cli.annotation.Command.class);
            COMMAND_NAME = command.name();
        }
        getRequiredParameters();
    }

    /**
     * 生成帮助手册中的options选项
     * @return
     */
    private String getOptions(){
        String paraStr = "[";
        for (Field field : requiredParameters) {
            Option option = field.getAnnotation(Option.class);
            paraStr += option.paraTag() + ", ";
        }
        //当命令中存在必要参数时
        if(requiredParameters.size() > 0){
            paraStr = paraStr.substring(0,paraStr.length()-2);
        }
        paraStr += "]";
        return paraStr;
    }

    /**
     * Description: 为未初始化命令参数设置注解默认值
     *
     * @param
     * @return   checkParameters
     * @throws
     * @author   shumeng
     * @since    V1.0.0
     * @date     2016/12/8
     */
    public boolean setDefaultParameterValues(){
        for(Field field : hasDefaultValueFiels){
            Object value = null;
            try {
                field.setAccessible(true);
                value = field.get(this);
                String type = field.getType().toString();
                if (type.endsWith("String") && value==null) {
                    Option option = field.getAnnotation(Option.class);
                    setField(field, option.value());
                }else if(type.endsWith("int") || type.endsWith("Integer")){
                    if(value.equals(0)){
                        Option option = field.getAnnotation(Option.class);
                        setField(field, option.value());
                    }
                }else{
                    System.out.println("特殊类型，暂不支持设值！！！");
                    logger.error("特殊类型，暂不支持设值！！！");
                    System.out.println(field.getType()+"\t");
                    logger.error(field.getType()+"\t");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }

        }

        return checkParameters();
    }

    /**
     * 解析命令参数注解，获取命令必要参数并初始化annotatedParameters 和 requiredParameters
     */
    private void getRequiredParameters(){
        Field[] commandFields = commandClass.getDeclaredFields();
        for (Field field : commandFields) {
            if (field.isAnnotationPresent(Option.class)) {
                Option option = field.getAnnotation(Option.class);
                annotatedParameters.put(option.paraTag(), field);
                if(!StringUtils.isBlank(option.value())){
                    hasDefaultValueFiels.add(field);
                }
                if(option.required()){
                    requiredParameters.add(field);
                }
            }
        }
    }

    @Override
    /**
     * 生成命令帮助手册
     */
    public void help() {
        StringBuilder helpStr = new StringBuilder("NAME\n");
        if (commandClass.isAnnotationPresent(com.dtdream.cli.annotation.Command.class)) {
            com.dtdream.cli.annotation.Command command = (com.dtdream.cli.annotation.Command) commandClass.getAnnotation(com.dtdream.cli.annotation.Command.class);
            helpStr.append("    " + command.name() + "\n");
            helpStr.append("SYNOPSIS\n");
            helpStr.append("    " + command.name() + " [options]\n");
            helpStr.append("DESCRIPTION\n");
            helpStr.append("    --"+command.description() + "\n");
            helpStr.append("Options " + getOptions() + "\n");
            Field[] entityFields = commandClass.getDeclaredFields();
            for (Field field : entityFields) {
                if (field.isAnnotationPresent(Option.class)) {
                    Option option = field.getAnnotation(Option.class);
                    helpStr.append("    " + option.paraTag() + "\n");
                    helpStr.append("         --" + field.getName());
                    if(!StringUtils.isBlank(option.comment())){
                        helpStr.append(", " + option.comment() + "\n");
                    }
                }
            }
        }
        System.out.println(helpStr.toString());
    }

    /**
     * Description: 通过反射为对象属性设值
     *
     * @param field 对象属性
     * @param value 属性值
     * @throws IllegalAccessException
     * @author thomugo
     * @since V1.0.0
     * @date 2016/12/8
     */
    private void setField(Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        //得到此属性的类型
        String type = field.getType().toString();
        if (type.endsWith("String")) {
            logger.debug(field.getType()+"\t是string");
            //给属性设值
            field.set(this, value);
        }else if(type.endsWith("int") || type.endsWith("Integer")){
            logger.debug(field.getType()+"\t是int");
            field.set(this, Integer.parseInt((String)value)) ;
        }else if(type.endsWith("boolean") || type.endsWith("Boolean")) {
            logger.debug(field.getType() + "\t是boolean");
            field.set(this, BooleanUtils.toBoolean((String)value));
        }else{
            System.out.println("特殊类型，无法设值！！！");
            logger.error("特殊类型，无法设值！！！");
            System.out.println(field.getType()+"\t");
            logger.error(field.getType()+"\t");
        }
    }

    /**
     * 解析命令行
     * @param parameters
     * @return parametersNotNull
     */
    @Override
    public boolean parse(String[] parameters) {
        displayParameters();
        int paraNum = annotatedParameters.size();
        if(paraNum > 0){
            Set<String> tags =  annotatedParameters.keySet();
            int index = 1;
            while (parameters.length>index && parameters.length<=2*paraNum+1){
                if(StringUtils.equals(parameters[index], "-help") || StringUtils.equals(parameters[index], "--help")){
                    advice(null);
                    return false;
                } else {
                    int flag = index;
                    for (String tag : tags){
                        if(StringUtils.equals(tag, parameters[index])){
                            index++;
                            Field field = annotatedParameters.get(tag);
                            try {
                                setField(field, parameters[index]);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                return false;
                            }
                            index++;
                            break;
                        }
                    }
                    //输入参数没有匹配项
                    if(flag == index){
                        advice(COMMAND_NAME);
                        return false;
                    }
                }
            }
        }

        return parametersNotNull();
    }

    /**
     * 进行参数非空校验
     * @return setDefaultParameterValues
     */
    public boolean parametersNotNull() {
        for (Field field : requiredParameters) {
            Object value = null;
            try {
                field.setAccessible(true);
                value = field.get(this);
                //System.out.println("value: " + value);
                Option option = field.getAnnotation(Option.class);
                //System.out.println("avalue: " + option.value());
                if(value==null && StringUtils.isBlank(option.value())){
                    System.out.printf("参数错误，缺少必要参数：%s 不能为空，请输入：%s -help 查询帮助。\n", field.getName(), COMMAND_NAME);
                    CommandRecord.getInstance().popLastCommand();
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return setDefaultParameterValues();
    }
}
