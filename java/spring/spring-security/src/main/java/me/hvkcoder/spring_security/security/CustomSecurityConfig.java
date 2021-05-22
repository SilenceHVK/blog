package me.hvkcoder.spring_security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义 Spring Security 配置
 *
 * @author h-vk
 * @since 2021/5/21
 */
@Configuration
@EnableWebSecurity // 启动 Security 认证
// 启动方法级别的验证 —> prePostEnable 为 true 表示可以  @PreAuthorize 和 @PostAuthorize 注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailService customUserDetailService;

	/**
	 * 设置加密对象
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 基于内容设置用户名和密码
//		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("123456")).roles("admin", "normal");
//		auth.inMemoryAuthentication().withUser("root").password(passwordEncoder.encode("123456")).roles("admin");
//		auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("123456")).roles("normal");

		// 指定 UserService
		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
	}
}
