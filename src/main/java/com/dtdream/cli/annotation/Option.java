package com.dtdream.cli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shumeng on 2016/12/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option{
    /**
     * 参数默认值
     * @return
     */
    String value() default "";

    /**
     * 参数简称
     * @return
     */
    String paraTag();

    /**
     * 参数说明
     * @return
     */
    String comment() default "";

    /**
     * 是否为必要参数
     * @return true/false
     */
    boolean required() default false;
}
