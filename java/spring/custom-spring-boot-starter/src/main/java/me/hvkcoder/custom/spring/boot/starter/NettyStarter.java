package me.hvkcoder.custom.spring.boot.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
