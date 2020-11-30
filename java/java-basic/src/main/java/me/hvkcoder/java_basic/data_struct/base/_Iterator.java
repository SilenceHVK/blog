package me.hvkcoder.java_basic.data_struct.base;

/**
 * @author h-vk
 * @since 2020/11/30
 */
public interface _Iterator<E> {
	/**
	 * TODO: 是否包含下一个元素
	 *
	 * @return
	 */
	boolean hasNext();

	/**
	 * TODO: 获取元素
	 *
	 * @return
	 */
	E next();

}
