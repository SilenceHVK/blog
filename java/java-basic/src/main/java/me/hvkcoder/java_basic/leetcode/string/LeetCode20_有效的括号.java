package me.hvkcoder.java_basic.leetcode.string;

import java.util.Stack;

/**
 * https://leetcode-cn.com/problems/valid-parentheses/
 *
 * @author h-vk
 * @since 2021/3/13
 */
public class LeetCode20_有效的括号 {
	public static boolean isValid(String s) {
		if (s.length() % 2 == 1) return false;

		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '{' || c == '[' || c == '(') {
				stack.push(c);
			} else {
				if (stack.isEmpty()) {
					return false;
				}
				Character pop = stack.pop();
				if (c == '}' && pop != '{') {
					return false;
				} else if (c == ']' && pop != '[') {
					return false;
				} else if (c == ')' && pop != '(') {
					return false;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(isValid("()[]{}"));
	}
}
