package me.hvkcoder.spring_security.security;

import cn.hutool.json.JSONUtil;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义认证入口
 *
 * @author h-vk
 * @since 2021/1/20
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

  private ServiceProperties serviceProperties;
  private String loginUrl;
  private boolean encodeServiceUrlWithSessionId = true;

  public CustomAuthenticationEntryPoint() {}

  /**
   * 判断是否为 ajax 请求
   *
   * @param request
   * @return
   */
  private static boolean isAjaxRequest(HttpServletRequest request) {
    String accept = request.getHeader("accept");
    if (accept != null && accept.indexOf("application/json") != -1) {
      return true;
    }

    String xRequestedWith = request.getHeader("X-Requested-With");
    if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
      return true;
    }

    String uri = request.getRequestURI().toLowerCase();
    if (".json".equals(uri) || ".xml".equals(uri)) {
      return true;
    }
    return false;
  }

  @Override
  public void afterPropertiesSet() {
    Assert.hasLength(this.loginUrl, "loginUrl must be specified");
    Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
    Assert.notNull(
        this.serviceProperties.getService(), "serviceProperties.getService() cannot be null.");
  }

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
      throws IOException, ServletException {
    if (isAjaxRequest(request)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      Map<String, Object> map = new HashMap<>();
      map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
      map.put("loginUrl", this.getLoginUrl());
      response.getWriter().write(JSONUtil.toJsonStr(map));
    } else {
      String urlEncodedService = this.createServiceUrl(request, response);
      String redirectUrl = this.createRedirectUrl(urlEncodedService);
      response.sendRedirect(redirectUrl);
    }
  }

  protected String createServiceUrl(HttpServletRequest request, HttpServletResponse response) {
    return CommonUtils.constructServiceUrl(
        (HttpServletRequest) null,
        response,
        this.serviceProperties.getService(),
        (String) null,
        this.serviceProperties.getArtifactParameter(),
        this.encodeServiceUrlWithSessionId);
  }

  protected String createRedirectUrl(String serviceUrl) {
    return CommonUtils.constructRedirectUrl(
        this.loginUrl,
        this.serviceProperties.getServiceParameter(),
        serviceUrl,
        this.serviceProperties.isSendRenew(),
        false);
  }

  protected void preCommence(HttpServletRequest request, HttpServletResponse response) {}

  public final String getLoginUrl() {
    return this.loginUrl;
  }

  public final void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public final ServiceProperties getServiceProperties() {
    return this.serviceProperties;
  }

  public final void setServiceProperties(ServiceProperties serviceProperties) {
    this.serviceProperties = serviceProperties;
  }

  protected boolean getEncodeServiceUrlWithSessionId() {
    return this.encodeServiceUrlWithSessionId;
  }

  public final void setEncodeServiceUrlWithSessionId(boolean encodeServiceUrlWithSessionId) {
    this.encodeServiceUrlWithSessionId = encodeServiceUrlWithSessionId;
  }
}
