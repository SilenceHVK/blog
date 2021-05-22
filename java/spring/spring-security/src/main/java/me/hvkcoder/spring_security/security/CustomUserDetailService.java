package me.hvkcoder.spring_security.security;

import me.hvkcoder.spring_security.business.UserInfoService;
import me.hvkcoder.spring_security.domain.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 加载用户信息
 *
 * @author h-vk
 * @since 2021/5/21
 */
@Component
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo userInfo = userInfoService.findUserInfoByUserName(username);
		if (userInfo == null) {
			throw new UsernameNotFoundException(username + " not found");
		}

		// 构建用户信息
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userInfo.getRole());
		return User.withUsername(username).password(userInfo.getEncryptedPassword()).authorities(authority).build();
	}
}
