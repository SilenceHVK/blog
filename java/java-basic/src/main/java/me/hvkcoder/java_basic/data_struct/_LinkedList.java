package me.hvkcoder.java_basic.data_struct;

import me.hvkcoder.java_basic.data_struct.base._AbstractList;

/**
 * @author h-vk
 * @since 2020/11/30
 */
public class _LinkedList<E> extends _AbstractList<E> {

	/**
	 * 链表头
	 */
	private Node<E> head;

	/**
	 * 链表尾
	 */
	private Node<E> last;

	/**
	 * 链表 Node 结构
	 *
	 * @param <E>
	 */
	private class Node<E> {
		private E data;
		private Node<E> prev;
		private Node<E> next;

		public Node(E data) {
			this.data = data;
		}

		public Node(E data, Node<E> prev, Node<E> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

		@Override
		public String toString() {
			return "Node{" + "data=" + data + ", next=" + next + '}';
		}
	}

	@Override
	public void clear() {
		head = null;
		last = null;
		size = 0;
	}

	@Override
	public void add(int index, E element) {
		checkedIndexForAdd(index);
		Node<E> newNode = new Node<>(element);
		if (size == 0) {
			head = newNode;
			last = newNode;
		} else if (index == 0) {
			newNode.next = head;
			head = newNode;
		} else if (index == size) {
			last.next = newNode;
			last = newNode;
		} else {
			Node<E> prevNode = getNode(index - 1);
			newNode.next = prevNode.next;
			prevNode.next = newNode;
		}
		size++;
	}

	/**
	 * 根据 index 获取 Node 节点值
	 *
	 * @param index
	 * @return
	 */
	@Override
	public E get(int index) {
		return getNode(index).data;
	}

	/**
	 * 根据 index 获取 Node 节点
	 *
	 * @param index
	 * @return
	 */
	public Node<E> getNode(int index) {
		checkedIndex(index);
		Node<E> node = head;
		for (int i = 0; i < index; i++) {
			node = node.next;
		}
		return node;
	}

	@Override
	public E set(int index, E element) {
		Node<E> node = getNode(index);
		E oldValue = node.data;
		node.data = element;
		return oldValue;
	}

	@Override
	public E remove(int index) {
		checkedIndex(index);
		Node<E> removeNode = head;
		if (index == 0) {
			head = removeNode.next;
		} else if (index == size - 1) {
			Node<E> prevNode = getNode(index - 1);
			removeNode = last;
			prevNode.next = null;
			last = prevNode;
		} else {
			Node<E> prevNode = getNode(index - 1);
			removeNode = prevNode.next;
			prevNode.next = removeNode.next;
		}
		size--;
		return removeNode.data;
	}

	@Override
	public String toString() {
		return "_LinkedList{" + "head=" + head + ", last=" + last + ", size=" + size + '}';
	}
}
