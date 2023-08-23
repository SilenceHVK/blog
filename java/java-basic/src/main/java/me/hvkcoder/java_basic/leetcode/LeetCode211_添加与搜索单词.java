package me.hvkcoder.java_basic.leetcode;

import java.util.TreeMap;

/**
 * https://leetcode.cn/problems/design-add-and-search-words-data-structure/
 *
 * @author h_vk
 * @since 2023/8/23
 */
public class LeetCode211_添加与搜索单词 {
	public static void main(String[] args) {
		WordDictionary wordDictionary = new WordDictionary();
		wordDictionary.addWord("bad");
		wordDictionary.addWord("dad");
		wordDictionary.addWord("mad");
		System.out.println(wordDictionary.search("pad")); // 返回 False
		System.out.println(wordDictionary.search("bad")); // 返回 True
		System.out.println(wordDictionary.search(".ad")); // 返回 True
		System.out.println(wordDictionary.search("b..")); // 返回 True
	}


	public static class WordDictionary {
		private static class Node {
			public boolean isWord;
			private TreeMap<Character, Node> next;

			public Node() {
				this.next = new TreeMap<>();
			}
		}

		private Node root;

		public WordDictionary() {
			this.root = new Node();
		}

		public void addWord(String word) {
			Node cur = this.root;
			for (int i = 0; i < word.length(); i++) {
				char w = word.charAt(i);
				if (!cur.next.containsKey(w)) {
					cur.next.put(w, new Node());
				}
				cur = cur.next.get(w);
			}
			cur.isWord = true;
		}

		public boolean search(String word) {
			return match(word, this.root, 0);
		}

		private boolean match(String word, Node node, int index) {
			if (index == word.length()) return node.isWord;

			char c = word.charAt(index);
			if (c != '.') {
				if (node.next.containsKey(c)) {
					return match(word, node.next.get(c), index + 1);
				} else {
					return false;
				}
			} else {
				for (Character key : node.next.keySet()) {
					if (match(word, node.next.get(key), index + 1)) {
						return true;
					}
				}
				return false;
			}
		}
	}


}
