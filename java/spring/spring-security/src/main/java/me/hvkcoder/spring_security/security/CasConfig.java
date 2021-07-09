package me.hvkcoder.spring_security.security;

import lombok.Data;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;

/**
 * @author h_vk
 * @since 2021/7/8
 */
@Data
@Configuration
@ConfigurationProperties("cas")
public class CasConfig {

  private String verifyServer;

  @Value("${cas.server.prefix}")
  private String casServerPrefix;

  @Value("${cas.server.login}")
  private String casServerLogin;

  @Value("${cas.server.logout}")
  private String casServerLogout;

  @Value("${cas.client.login}")
  private String casClientLogin;

  @Value("${cas.client.logout}")
  private String casClientLogout;

  @Value("${cas.client.logout-relative}")
  private String casClientLogoutRelative;

  private String[] notAuthorizeAccess;

  /**
   * 配置 CAS Client 的属性
   *
   * @return
   */
  @Bean
  public ServiceProperties serviceProperties() {
    ServiceProperties serviceProperties = new ServiceProperties();
    // 与 CasAuthenticationFilter 监视的 URL 一致
    serviceProperties.setService(casClientLogin);
    // 是否关闭单点登录，默认为 false
    serviceProperties.setSendRenew(false);
    return serviceProperties;
  }

  /**
   * CAS 验证入口，提供用户浏览器重定向的地址
   *
   * @param serviceProperties
   * @return
   */
  @Bean
  @Primary
  public CustomAuthenticationEntryPoint authenticationEntryPoint(
      ServiceProperties serviceProperties) {
    CustomAuthenticationEntryPoint entryPoint = new CustomAuthenticationEntryPoint();
    entryPoint.setLoginUrl(casServerLogin);
    entryPoint.setServiceProperties(serviceProperties);
    return entryPoint;
  }

  /**
   * ticket 校验，需要提供 CAS Server 校验 ticket 的地址
   *
   * @return
   */
  @Bean
  public TicketValidator ticketValidator() {
    return new Cas20ProxyTicketValidator(casServerPrefix);
  }

  /**
   * 使用内存上的用户并分配权限
   *
   * @return
   */
  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
    inMemoryUserDetailsManager.createUser(
        User.withUsername("admin").password("").roles("USER").build());
    return inMemoryUserDetailsManager;
  }

  /**
   * CAS 验证处理逻辑
   *
   * @param serviceProperties
   * @param ticketValidator
   * @param userDetailsService
   * @return
   */
  @Bean
  public CasAuthenticationProvider casAuthenticationProvider(
      ServiceProperties serviceProperties,
      TicketValidator ticketValidator,
      UserDetailsService userDetailsService) {
    CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
    casAuthenticationProvider.setTicketValidator(ticketValidator);
    casAuthenticationProvider.setServiceProperties(serviceProperties);
    casAuthenticationProvider.setUserDetailsService(userDetailsService);
    //    casAuthenticationProvider.setAuthenticationUserDetailsService();
    casAuthenticationProvider.setKey("test");
    return casAuthenticationProvider;
  }

  /**
   * 提供 CAS 验证专用过滤器，过滤的验证逻辑由 CasAuthenticationProvider 提供
   *
   * @param serviceProperties
   * @param authenticationProvider
   * @return
   */
  @Bean
  public CasAuthenticationFilter casAuthenticationFilter(
      ServiceProperties serviceProperties, AuthenticationProvider authenticationProvider) {
    CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    casAuthenticationFilter.setServiceProperties(serviceProperties);
    casAuthenticationFilter.setAuthenticationManager(
        new ProviderManager(Arrays.asList(authenticationProvider)));
    return casAuthenticationFilter;
  }

  @Bean
  public HttpFirewall allowUrlSemicolonHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowSemicolon(true);
    // 确定路径中是否允许使用双斜杠“//”，
    firewall.setAllowUrlEncodedDoubleSlash(true);
    return firewall;
  }

  /**
   * 接受 CAS 服务器发出的注销请求
   *
   * @return
   */
  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
    SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
    singleSignOutFilter.setCasServerUrlPrefix(casServerPrefix);
    singleSignOutFilter.setIgnoreInitConfiguration(true);
    return singleSignOutFilter;
  }

  /**
   * 将注销请求转发到 CAS Server
   *
   * @return
   */
  @Bean
  public LogoutFilter logoutFilter() {
    LogoutFilter logoutFilter =
        new LogoutFilter(casServerLogout, new SecurityContextLogoutHandler());
    logoutFilter.setFilterProcessesUrl(casClientLogoutRelative);
    return logoutFilter;
  }
}
