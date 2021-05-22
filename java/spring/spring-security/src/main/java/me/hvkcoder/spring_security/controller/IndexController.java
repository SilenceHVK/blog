package me.hvkcoder.spring_security.controller;

import me.hvkcoder.spring_security.business.UserInfoService;
import me.hvkcoder.spring_security.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h-vk
 * @since 2021/5/21
 */
@RestController
public class IndexController {
	
	/**
	 * normal 或 admin 角色可访问的接口
	 *
	 * @return
	 */
	@RequestMapping("/access/normal")
	@PreAuthorize(value = "hasAnyRole('normal', 'admin')")
	public String accessNormal() {
		return "Hello User";
	}


	/**
	 * admin 角色可访问的接口
	 *
	 * @return
	 */
	@RequestMapping("/access/admin")
	@PreAuthorize(value = "hasAnyRole('admin')")
	public String accessAdmin() {
		return "Hello Admin";
	}
}
