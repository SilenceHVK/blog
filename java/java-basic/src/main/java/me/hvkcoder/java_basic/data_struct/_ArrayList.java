package me.hvkcoder.java_basic.data_struct;

import me.hvkcoder.java_basic.data_struct.base._AbstractList;

import java.util.Arrays;

/**
 * @author h-vk
 * @since 2020/11/30
 */
public class _ArrayList<E> extends _AbstractList<E> {
	/**
	 * 数组默认容量
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * 空数组实例
	 */
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

	/**
	 * 存储数据数组
	 */
	private transient Object[] elementData;

	public _ArrayList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}

	public _ArrayList(int initCapacity) {
		initCapacity = initCapacity < 0 ? DEFAULT_CAPACITY : initCapacity;
		this.elementData = new Object[initCapacity];
	}

	/**
	 * 清空集合
	 */
	@Override
	public void clear() {
//		for (int i = 0; i < elementData.length; i++) {
//			elementData[i] = null;
//		}
		Arrays.fill(elementData, null);
		this.size = 0;
	}

	/**
	 * 添加元素到集合的指定位置
	 *
	 * @param index
	 * @param element
	 */
	@Override
	public void add(int index, E element) {
		checkedIndexForAdd(index);
		grow();
		System.arraycopy(elementData, index, elementData, index + 1, size - index);
//		for (int i = size - index; i >= index; i--) {
//			elementData[i + 1] = elementData[i];
//		}
		elementData[index] = element;
		this.size++;
	}

	/**
	 * 获取指定位置的元素值
	 *
	 * @param index
	 * @return 获取 index 的元素值
	 */
	@Override
	public E get(int index) {
		checkedIndex(index);
		return (E) elementData[index];
	}

	/**
	 * 更改指定位置的元素值
	 *
	 * @param index
	 * @param element
	 * @return 未更改的元素值
	 */
	@Override
	public E set(int index, E element) {
		checkedIndex(index);
		Object oldValue = elementData[index];
		elementData[index] = element;
		return (E) oldValue;
	}

	/**
	 * 删除指定位置的元素
	 *
	 * @param index
	 * @return 删除的元素值
	 */
	@Override
	public E remove(int index) {
		checkedIndex(index);
		Object oldValue = elementData[index];
		System.arraycopy(elementData, index + 1, elementData, index, size - index);
//		for (int i = index; i < size; i++) {
//			elementData[i] = elementData[i + 1];
//		}
		this.size--;
		return (E) oldValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("_ArrayList{");
		sb.append("size=" + size);
		sb.append(", elementData=[");
		for (int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(",");
			}
			;
			sb.append(elementData[i]);
		}
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * 数组扩容
	 */
	public void grow() {
		grow(this.size + 1);
	}

	/**
	 * 数组扩容
	 *
	 * @param capacity
	 */
	public void grow(int capacity) {
		// 数组现有容量
		int oldCapacity = elementData.length;
		if (oldCapacity >= capacity) {
			return;
		}
		;
		// 数组容量扩大 1.5 倍
		int newCapacity = oldCapacity == 0 ? DEFAULT_CAPACITY : oldCapacity + (oldCapacity >> 1);
		this.elementData = Arrays.copyOf(elementData, newCapacity);
	}
}
