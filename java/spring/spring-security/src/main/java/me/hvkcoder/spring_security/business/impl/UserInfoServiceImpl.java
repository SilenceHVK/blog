package me.hvkcoder.spring_security.business.impl;

import me.hvkcoder.spring_security.business.UserInfoService;
import me.hvkcoder.spring_security.domain.UserInfo;
import me.hvkcoder.spring_security.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author h-vk
 * @since 2021/5/21
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoRepository userInfoRepository;

	@Override
	public UserInfo findUserInfoByUserName(String username) {
		return userInfoRepository.findUserInfoByUserName(username);
	}
}
