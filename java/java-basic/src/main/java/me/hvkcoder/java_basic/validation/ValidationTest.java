package me.hvkcoder.java_basic.validation;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author h_vk
 * @since 2023/5/26
 */
@Slf4j
public class ValidationTest {
	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	Set<ConstraintViolation<Object>> result;


	@After
	public void printResult() {
		result.forEach(valid -> log.info("{}", valid.getMessage()));
	}


	/**
	 * Validator 基础验证
	 */
	@Test
	public void testBasicValidation() {
		result = validator.validate(
			UserInfo.builder()
				.userId(null)
				.age(11)
				.email("test")
				.phone("13612345678")
				.friends(new ArrayList<>())
				.build(),
			UserInfo.Group.class
		);
	}

	/**
	 * 级联验证
	 */
	@Test
	public void graphValidation() {
		result = validator.validate(
			UserInfo.builder()
				.friends(List.of(
					UserInfo.builder().build()
				))
				.build()
		);
	}

	/**
	 * 分组验证
	 */
	@Test
	public void testGroupValidation() {
		result = validator.validate(
			UserInfo.builder()
				.userId(null)
				.age(11)
				.email("test")
				.friends(new ArrayList<>())
				.build(), UserInfo.Group.class
		);
	}

	/**
	 * 对方法的输入参数进行校验
	 */
	@Test
	public void testParameterValidation() throws NoSuchMethodException {
		// 创建验证执行器
		ExecutableValidator executableValidator = validator.forExecutables();

		UserInfoService userInfoService = UserInfoService.builder().build();
		Method setUserInfoMethod = userInfoService.getClass().getMethod("setUserInfo", UserInfo.class);

		// 执行入参校验
		result = executableValidator.validateParameters(userInfoService, setUserInfoMethod, new Object[]{
			UserInfo.builder().build()
		});
	}

	/**
	 * 对方法的返回值进行校验
	 */
	@Test
	public void testReturnValidation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// 创建验证执行器
		ExecutableValidator executableValidator = validator.forExecutables();

		UserInfoService userInfoService = UserInfoService.builder().build();
		Method getUserInfoMethod = userInfoService.getClass().getMethod("getUserInfo");

		Object returnValue = getUserInfoMethod.invoke(userInfoService);
		// 执行返回值校验
		result = executableValidator.validateReturnValue(userInfoService, getUserInfoMethod, returnValue);
	}
}
