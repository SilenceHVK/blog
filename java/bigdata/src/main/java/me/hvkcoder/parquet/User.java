package me.hvkcoder.parquet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author h_vk
 * @since 2021/9/14
 */
@Data
@ToString
@AllArgsConstructor
public class User {
	private String id;
	private String name;
	private Integer age;
}
