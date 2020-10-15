package me.hvkcoder.java_basic.jvm.reflect.annotation.ch02;

import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.DBTable;
import me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database.TableField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author h-vk
 * @since 2020/10/5
 */
public class TableCreator {

    private static String createTable(String classPath) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder(" CREATE TABLE ");
        Class<?> aClass = Class.forName(classPath);
        // 加载 DBTable 注解
        DBTable dbTable = aClass.getAnnotation(DBTable.class);
        if (dbTable == null || "".equals(dbTable.value())) {
            throw new RuntimeException("请指定创建表名");
        }
        sb.append(dbTable.value()).append("( \n ");
        // 反射获取除继承的所有字段
        Field[] fields = aClass.getDeclaredFields();
        if (fields.length == 0) {
            throw new RuntimeException("请指定数据表字段");
        }
        // 记录主键
        List<String> primaryKeys = new ArrayList<>();
        Arrays.stream(fields).forEach(field -> {
					TableField tableField = field.getAnnotation(TableField.class);
					if (tableField == null || "".equals(tableField.value())) {
						throw new RuntimeException("请指定数据表字段");
					}
					// 拼接字段SQL
					sb.append("\t ").append(tableField.value()).append(" ").append(tableField.jdbcType().toString())
						.append("(").append(tableField.length()).append(")");
					// 字段为空
					if (!tableField.constraints().allowNull()) {
						sb.append(" NOT NULL ");
					}
					sb.append(" COMMENT ").append("'").append(tableField.comment()).append("'")
						.append(",\n ");
					// 字段为主键
					if (tableField.constraints().primaryKey()) {
						primaryKeys.add(tableField.value());
					}
				});

        // 主键
        if (primaryKeys.size() > 0) {
            sb.append("\tPRIMARY KEY (");
            primaryKeys.forEach(sb::append);
            sb.append(") USING BTREE \n");
        }
        return sb.append(")").toString();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        String sql = createTable("me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.Student");
        System.out.println(sql);
    }
}
