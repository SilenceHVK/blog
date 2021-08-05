package me.hvkcoder.spring_security.security;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.mysql.cj.protocol.AuthenticationProvider;
import me.hvkcoder.spring_security.security.filter.VerificationCodeFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.firewall.HttpFirewall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Properties;

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

  @Autowired private CustomUserDetailService customUserDetailService;
  @Autowired private CasConfig casConfig;
  @Autowired private CasAuthenticationProvider authenticationProvider;
  @Autowired private AuthenticationEntryPoint authenticationEntryPoint;
  @Autowired private SingleSignOutFilter singleSignOutFilter;
  @Autowired private LogoutFilter logoutFilter;
  @Autowired private CasAuthenticationFilter casAuthenticationFilter;
  @Autowired private HttpFirewall allowUrlSemicolonHttpFirewall;

  /**
   * 设置加密对象
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 验证码生成类
   *
   * @return
   */
  @Bean
  public Producer captcha() {
    Properties properties = new Properties();
    properties.setProperty("kaptcha.image.width", "150");
    properties.setProperty("kaptcha.image.height", "50");
    properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
    properties.setProperty("kaptcha.textproducer.char.length", "5");
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    defaultKaptcha.setConfig(new Config(properties));
    return defaultKaptcha;
  }

  /**
   * 使用自定义验证
   *
   * @param http
   * @throws Exception
   */
  private void useCustomAuthorize(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            req ->
                req.antMatchers(casConfig.getNotAuthorizeAccess())
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(
            formLogin ->
                formLogin
                    // 登录页路径
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    // 处理登录请求
                    .loginProcessingUrl("/authentication/form")
                    // 处理登录成功响应
                    .successHandler(getAuthenticationSuccessHandler())
                    // 处理登录失败响应
                    .failureHandler(getAuthenticationFailureHandler())
                    .permitAll())
        // 在 UsernamePasswordAuthenticationFilter 之前添加自定义过滤器
        .addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  /**
   * 使用 CAS 授权验证
   *
   * @param http
   * @throws Exception
   */
  private void useCasAuthorize(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            req ->
                req.antMatchers(casConfig.getNotAuthorizeAccess())
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint))
        .addFilter(casAuthenticationFilter)
        .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, LogoutFilter.class)
        .httpBasic();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    if ("cas".equals(casConfig.getVerifyServer())) {
      useCasAuthorize(http);
    } else {
      useCustomAuthorize(http);
    }
  }

  /**
   * 处理授权失败响应
   *
   * @return
   */
  private AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return (request, response, exception) -> {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().println("{\"error_code\": 401, \"message\": \"用户名或密码错误\" }");
    };
  }

  /**
   * 处理授权成功响应
   *
   * @return
   */
  private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
    return (request, response, authentication) -> {
      response.setStatus(HttpStatus.OK.value());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().println("{\"error_code\": 0, \"message\": \"登录成功\" }");
    };
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 基于内容设置用户名和密码
    //	auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("123456")).roles("admin", "normal");
    //	auth.inMemoryAuthentication().withUser("root").password(passwordEncoder.encode("123456")).roles("admin");
    //	auth.inMemoryAuthentication().withUser("user").password(passwordEncoder.encode("123456")).roles("normal");

    if ("cas".equals(casConfig.getVerifyServer())) {
      auth.authenticationProvider(authenticationProvider);
    } else {
      // 指定 UserService
      auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }
  }
}
