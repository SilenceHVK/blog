package me.hvkcoder.java_basic.validation.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

/**
 * 字定义验证器
 *
 * @author h_vk
 * @since 2023/5/26
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {


	/**
	 * 验证规则
	 *
	 * @param value   object to validate
	 * @param context context in which the constraint is evaluated
	 * @return
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 定义正则表达式
		Pattern regex = Pattern.compile("158\\d{8}");
		return regex.matcher(value).matches();
	}
}
