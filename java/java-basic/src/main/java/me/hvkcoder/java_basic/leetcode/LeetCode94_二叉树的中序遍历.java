package me.hvkcoder.java_basic.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * https://leetcode-cn.com/problems/binary-tree-inorder-traversal/ 二叉树中序遍历：左子树 ---> 根结点 ---> 右子树
 *
 * @author h-vk
 * @since 2020/11/20
 */
public class LeetCode94_二叉树的中序遍历 {

	public static void main(String[] args) {
		TreeNode treeNode = new TreeNode(1, null, new TreeNode(2, new TreeNode(3), null));
		System.out.println("中序递归:" + inorderTraversalByRecursive(treeNode));
		System.out.println("中序非递归:" + inorderTraversal(treeNode));
	}

	/**
	 * 中序非递归 将左子树入栈，再取出放入结果，再遍历右子树
	 *
	 * @param root
	 * @return
	 */
	public static List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> list = new ArrayList<>();

		Stack<TreeNode> stack = new Stack<>();
		TreeNode node = root;
		while (!stack.isEmpty() || node != null) {
			while (node != null) {
				stack.add(node);
				node = node.left;
			}
			node = stack.pop();
			list.add(node.val);
			node = node.right;
		}
		return list;
	}

	/**
	 * 中序递归
	 *
	 * @param root
	 * @return
	 */
	public static List<Integer> inorderTraversalByRecursive(TreeNode root) {
		List<Integer> list = new ArrayList<>();
		inorderTraversal(root, list);
		return list;
	}

	public static void inorderTraversal(TreeNode root, List<Integer> list) {
		if (root == null) {
			return;
		}
		inorderTraversal(root.left, list);
		list.add(root.val);
		inorderTraversal(root.right, list);
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
