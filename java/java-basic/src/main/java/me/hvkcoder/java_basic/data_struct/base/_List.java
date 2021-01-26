package me.hvkcoder.java_basic.data_struct.base;

/**
 * @author h-vk
 * @since 2020/11/30
 */
public interface _List<E> {

	/**
     * 获取当前 size
     *
     * @return size
     */
	int getSize();

    /**
     *  集合是否为空
     *
     * @return
	 */
	boolean isEmpty();

    /**
     *  清空集合
	 */
	void clear();

    /**
     *  是否包含该元素
     *
     * @param element
     * @return
	 */
	boolean contains(E element);

    /**
     *  查询指定元素位置
     *
     * @param element
     * @return 元素索引
	 */
	int indexOf(E element);

    /**
     *  添加元素
     *
     * @param element
	 */
	void add(E element);

    /**
     *  添加元素到集合的指定位置
     *
     * @param index
     * @param element
	 */
	void add(int index, E element);

    /**
     *  获取指定位置的元素值
     *
     * @param index
     * @return 获取 index 的元素值
	 */
	E get(int index);

    /**
     *  更改指定位置的元素值
     *
     * @param index
     * @param element
     * @return 未更改的元素值
	 */
	E set(int index, E element);

    /**
     *  删除指定位置的元素
     *
     * @param index
     * @return 删除的元素值
	 */
	E remove(int index);

}
