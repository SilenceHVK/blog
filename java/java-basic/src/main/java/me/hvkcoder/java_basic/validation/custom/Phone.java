package me.hvkcoder.java_basic.validation.custom;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义手机验证注解
 *
 * @author h_vk
 * @since 2023/5/26
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)// 指定注解校验器
public @interface Phone {
	// 验证返回的信息
	String message() default "手机号不等你为空";

	// 验证的组别
	Class<?>[] groups() default {};

	// 校验的有效负载
	Class<? extends Payload>[] payload() default {};
}
