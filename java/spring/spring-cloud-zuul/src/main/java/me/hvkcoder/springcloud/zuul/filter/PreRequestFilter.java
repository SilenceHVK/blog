package me.hvkcoder.springcloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author h_vk
 * @since 2021/6/21
 */
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter {
  /**
   * 过滤器类型
   *
   * @return
   */
  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE;
  }

  /**
   * 过滤器顺序
   *
   * @return
   */
  @Override
  public int filterOrder() {
    return 0;
  }

  /**
   * 是否启用过滤器
   *
   * @return
   */
  @Override
  public boolean shouldFilter() {
    return true;
  }

  /**
   * 过滤器逻辑代码
   *
   * @return
   * @throws ZuulException
   */
  @Override
  public Object run() throws ZuulException {
    // 获取当前请求上下文
    RequestContext currentContext = RequestContext.getCurrentContext();
    currentContext.set("startTime", System.currentTimeMillis());
    log.info("过滤器已记录请求时间");
    return null;
  }
}
