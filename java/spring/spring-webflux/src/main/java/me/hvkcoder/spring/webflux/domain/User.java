package me.hvkcoder.spring.webflux.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author h_vk
 * @since 2022/1/27
 */
@Data
@Document("user")
public class User {
	@Id
	private String id;
	private String name;
	private Integer age;
}
