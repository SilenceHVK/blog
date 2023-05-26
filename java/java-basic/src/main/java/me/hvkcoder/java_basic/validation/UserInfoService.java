package me.hvkcoder.java_basic.validation;

import lombok.Builder;

import javax.validation.Valid;

/**
 * @author h_vk
 * @since 2023/5/26
 */
@Builder
public class UserInfoService {


	/**
	 * 对入参进行校验
	 *
	 * @param userInfo
	 */
	public void setUserInfo(@Valid UserInfo userInfo) {
	}

	/**
	 * 对输出参数进行校验
	 *
	 * @return
	 */
	public @Valid UserInfo getUserInfo() {
		return UserInfo.builder().build();
	}
}
