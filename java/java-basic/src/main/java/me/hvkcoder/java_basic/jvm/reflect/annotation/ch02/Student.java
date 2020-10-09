package me.hvkcoder.java_basic.jvm.reflect.annotation.ch02;

import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.Constraints;
import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.DBTable;
import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.JdbcType;
import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.TableField;

/**
 * @author h-vk
 * @since 2020/10/5
 */
@DBTable("student")
public class Student {
    @TableField(value = "`id`", length = 38, comment = "用户ID", jdbcType = JdbcType.BIGINT, constraints = @Constraints(primaryKey = true))
    private Long id;

    @TableField(value = "`name`", length = 255, comment = "用户名", jdbcType = JdbcType.VARCHAR, constraints = @Constraints(allowNull = true))
    private String name;
}
