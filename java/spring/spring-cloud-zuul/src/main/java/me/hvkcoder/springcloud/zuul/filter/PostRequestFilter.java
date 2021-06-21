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
public class PostRequestFilter extends ZuulFilter {

  @Override
  public String filterType() {
    return FilterConstants.POST_TYPE;
  }

  @Override
  public int filterOrder() {
    return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext currentContext = RequestContext.getCurrentContext();
    Long startTime = (Long) currentContext.get("startTime");
    Long duration = System.currentTimeMillis() - startTime;
    log.info("{} 处理时长 {}", currentContext.getRequest().getRequestURI(), duration);
    return null;
  }
}
