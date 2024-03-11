package me.hvkcoder.custom.spring.boot.starter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author h_vk
 * @since 2024/3/11
 */
@Data
@ConfigurationProperties(prefix = "netty.server")
public class NettyProperties {
	@Value("true")
	private Boolean enabled;

	@Value("0.0.0.0")
	private String host;

	@Value("12138")
	private Integer port;
}
