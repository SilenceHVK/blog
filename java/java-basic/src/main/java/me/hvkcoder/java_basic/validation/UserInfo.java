package me.hvkcoder.java_basic.validation;

import lombok.Builder;
import lombok.Data;
import me.hvkcoder.java_basic.validation.custom.Phone;
import org.hibernate.validator.constraints.Length;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.util.List;

/**
 * @author h_vk
 * @since 2023/5/26
 */
@Data
@Builder
@NotNull(message = "用户信息不能为空")
public class UserInfo {
	public interface LoginGroup {
	}

	public interface RegisterGroup {
	}

	// 定义组序列，及优先级
	@GroupSequence({
		LoginGroup.class,
		RegisterGroup.class,
		Default.class
	})
	public interface Group {
	}

	// 判断 null，只有在登录时才验证
	@NotNull(message = "用户Id不能为空", groups = {LoginGroup.class})
	private String userId;

	@NotEmpty(message = "用户名不能为空字符串") // 判断不去空格
	private String userName;

	@NotBlank(message = "密码不能为空") // 判断去空格
	@Length(min = 6, max = 10, message = "密码长度不能少于 6 位，多于 10 位")
	private String password;

	@Min(value = 18, message = "年龄不能小于 18 岁")
	@Max(value = 60, message = "年龄不能大于 20 岁")
	private Integer age;

	// 只有在注册时才验证
	@Email(message = "邮箱格式不正确", groups = RegisterGroup.class)
	private String email;

	@Phone(message = "手机号格式不正确", groups = LoginGroup.class)
	private String phone;


	@Size(min = 1, message = "不能少于 1 个好友")
	// @Valid 用于级联验证
	private List<@Valid UserInfo> friends;
}
