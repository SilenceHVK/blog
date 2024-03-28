package me.hvkcoder.custom.spring.boot.starter;

import me.hvkcoder.custom.spring.boot.starter.callback.CallBack;
import me.hvkcoder.custom.spring.boot.starter.callback.CallBackRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author h_vk
 * @since 2024/3/11
 */
@Configuration
@EnableConfigurationProperties({NettyProperties.class})
@ConditionalOnExpression("${netty.server.enabled:true}")
public class NettyStarter {
	@Bean
	public NettyServer nettyServer(NettyProperties nettyProperties) {
		return new NettyServer(nettyProperties);
	}

	@Bean
	public CallBackRegistry callBackRegistry(List<CallBack> callBackList) {
		CallBackRegistry registry = new CallBackRegistry();
		callBackList.forEach(registry::register);
		return registry;
	}
}
