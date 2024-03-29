package me.hvkcoder.java_basic.io.netty.custom.codec;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author h_vk
 * @since 2024/3/29
 */
@Data
public class InvokerProtocol implements Serializable {
	@Serial
	private static final long serialVersionUID = -3113587718295415460L;

	/**
	 * 类名
	 */
	private String className;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 参数类型
	 */
	private Class<?>[] params;

	/**
	 * 参数值
	 */
	private Object[] values;
}
