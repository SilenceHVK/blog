package me.hvkcoder.spring_security.repository;

import me.hvkcoder.spring_security.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author h-vk
 * @since 2021/5/21
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
	UserInfo findUserInfoByUserName(String username);
}
