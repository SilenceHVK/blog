package me.hvkcoder.java_basic.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/implement-trie-prefix-tree/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode208_实现Trie {
	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insert("apple");
		System.out.println(trie.search("apple"));
		System.out.println(trie.search("app"));
		System.out.println(trie.startsWith("app"));
		trie.insert("app");
		System.out.println(trie.search("app"));
	}


	private static class Trie {
		private Node root;

		public Trie() {
			root = new Node();
		}

		public void insert(String word) {
			Node cur = root;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (!cur.next.containsKey(c)) {
					cur.next.put(c, new Node());
				}
				cur = cur.next.get(c);
			}
			cur.isWord = true;
		}

		public boolean search(String word) {
			Node cur = root;
			for (int i = 0; i < word.length(); i++) {
				char c = word.charAt(i);
				if (!cur.next.containsKey(c)) {
					return false;
				}
				cur = cur.next.get(c);
			}
			return cur.isWord;
		}

		public boolean startsWith(String prefix) {
			Node cur = root;
			for (int i = 0; i < prefix.length(); i++) {
				char c = prefix.charAt(i);
				if (!cur.next.containsKey(c)) {
					return false;
				}
				cur = cur.next.get(c);
			}
			return true;
		}

		private static class Node {
			public boolean isWord;
			public Map<Character, Node> next;

			public Node() {
				this.isWord = false;
				next = new HashMap<>();
			}
		}
	}
}
