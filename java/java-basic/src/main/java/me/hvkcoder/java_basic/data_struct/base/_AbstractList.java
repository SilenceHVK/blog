package me.hvkcoder.java_basic.data_struct.base;

/**
 * @author h-vk
 * @since 2020/11/30
 */
public abstract class _AbstractList<E> implements _List<E> {
	/**
	 * 未找到元素返回值
	 */
	protected static final int ELEMENT_NOT_FOUND = -1;

	/**
	 * 线性列表中元素的个数
	 */
	protected int size;


	/**
	 * 获取当前 size
	 *
	 * @return size
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 *  集合是否为空
	 *
	 * @return
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 *  是否包含该元素
	 *
	 * @param element
	 * @return
	 */
	@Override
	public boolean contains(E element) {
		return indexOf(element) == ELEMENT_NOT_FOUND;
	}

	/**
	 *  查询指定元素位置
	 *
	 * @param element
	 * @return 元素索引
	 */
	@Override
	public int indexOf(E element) {
		_Iterator<E> iterator = this.iterator();
		int index = -1;
		if (element == null) {
			while (iterator.hasNext()) {
				E data = iterator.next();
				index++;
				if (data == null) {
					return index;
				}
			}
		} else {
			while (iterator.hasNext()) {
				E data = iterator.next();
				index++;
				if (element.equals(data)) {
					return index;
				}
			}
		}
		return ELEMENT_NOT_FOUND;
	}

	/**
	 *  添加元素至线性表末尾
	 *
	 * @param element
	 */
	@Override
	public void add(E element) {
		add(size, element);
	}

	/**
	 *  判断数组是否越界
	 *
	 * @param index
	 */
	protected void checkedIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("index: " + index + " , size:" + size);
		}
	}

	/**
	 *  添加时 判断数组是否越界
	 *
	 * @param index
	 */
	protected void checkedIndexForAdd(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("index: " + index + " , size:" + size);
		}
	}


	/**
	 *  创建迭代器
	 *
	 * @return
	 */
	public _Iterator<E> iterator() {
		return new Itr();
	}

	/**
	 *  迭代器内部类
	 */
	private class Itr implements _Iterator<E> {
		/**
		 * 当前元素下标
		 */
		private int index;

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public E next() {
			E data = get(index);
			index++;
			return data;
		}
	}

}
