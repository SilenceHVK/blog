package me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据字段注解
 *
 * @author h-vk
 * @since 2020/10/9
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    /**
     * 字段映射名称
     */
    String value() default "";

    /**
     * 字段长度
     */
    int length() default 0;

    /**
     * 字段备注
     */
    String comment() default "";

    /**
     * 字段类型
     */
    JdbcType jdbcType() default JdbcType.VARCHAR;

    /**
     * 字段约束
     */
    Constraints constraints() default @Constraints;
}
