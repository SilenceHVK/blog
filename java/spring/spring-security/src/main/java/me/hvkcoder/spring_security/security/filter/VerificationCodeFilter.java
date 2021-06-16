package me.hvkcoder.spring_security.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 自定义验证码过滤器 OncePerRequestFilter 确保一次请求只会通过一次该过滤器
 *
 * @author h_vk
 * @since 2021/6/16
 */
@Slf4j
public class VerificationCodeFilter extends OncePerRequestFilter {
  private final String CAPTCHA = "captcha";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // 非登录验证请求不校验验证码
    if (!"/authentication/form".equals(request.getRequestURI())) {
      filterChain.doFilter(request, response);
    } else {
      HttpSession session = request.getSession();
      String requestCode = request.getParameter(CAPTCHA);
      String saveCode = session.getAttribute(CAPTCHA).toString();

      // 删除 session 中保存的验证码
      if (!StringUtils.isEmpty(saveCode)) {
        session.removeAttribute(CAPTCHA);
      }

      // 校验验证码
      if (StringUtils.isEmpty(requestCode) || StringUtils.isEmpty(saveCode) || !requestCode.equals(saveCode)) {
      	response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().println("{\"error_code\": 401, \"message\": \"验证码错误\" }");
			}else {
				filterChain.doFilter(request, response);
			}
    }
  }
}
