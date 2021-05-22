package me.hvkcoder.spring_security.business;

import me.hvkcoder.spring_security.domain.UserInfo;

/**
 * @author h-vk
 * @since 2021/5/21
 */
public interface UserInfoService {
	UserInfo findUserInfoByUserName(String username);
}
