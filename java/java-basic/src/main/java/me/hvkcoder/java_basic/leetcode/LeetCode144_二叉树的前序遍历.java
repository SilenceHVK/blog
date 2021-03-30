package me.hvkcoder.java_basic.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * https://leetcode-cn.com/problems/binary-tree-preorder-traversal/
 * 二叉树前序遍历：根结点 ---> 左子树 ---> 右子树
 *
 * @author h-vk
 * @since 2020/11/20
 */
public class LeetCode144_二叉树的前序遍历 {

	public static void main(String[] args) {
		TreeNode treeNode = new TreeNode(1, null, new TreeNode(2, new TreeNode(3), null));
		System.out.println("前序递归:" + preorderTraversalByRecursive(treeNode));
		System.out.println("前序非递归:" + preorderTraversal(treeNode));
	}

	/**
	 * 前序非递归
	 * 将右子树入栈，并添加根节点值，再取左子树
	 *
	 * @param root
	 * @return
	 */
	private static List<Integer> preorderTraversal(TreeNode root) {
		List<Integer> list = new ArrayList<>();
		if (root == null) {
			return list;
		}
		Stack<TreeNode> stack = new Stack<>();
		TreeNode node = root;
		while (!stack.isEmpty() || node != null) {
			while (node != null) {
				list.add(node.val);
				stack.add(node.right);
				node = node.left;
			}
			node = stack.pop();
		}
		return list;
	}

	/**
	 * 前序递归
	 *
	 * @param root
	 * @return
	 */
	private static List<Integer> preorderTraversalByRecursive(TreeNode root) {
		List<Integer> list = new ArrayList<>();
		preTraversal(root, list);
		return list;
	}

	private static void preTraversal(TreeNode root, List<Integer> list) {
		if (root == null) {
			return;
		}
		list.add(root.val);
		preTraversal(root.left, list);
		preTraversal(root.right, list);
	}

	private static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode() {
		}

		TreeNode(int val) {
			this.val = val;
		}

		TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}
}
