package me.hvkcoder.java_basic.leetcode.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * https://leetcode-cn.com/problems/binary-tree-postorder-traversal/
 * 二叉树后序遍历：左子树 ---> 右子树 ---> 根结点
 *
 * @author h-vk
 * @since 2020/11/22
 */
public class LeetCode145_二叉树的后序遍历 {
	public static void main(String[] args) {
		TreeNode treeNode = new TreeNode(1, null, new TreeNode(2, new TreeNode(3), null));
		System.out.println("后序递归:" + inorderTraversalByRecursive(treeNode));
		System.out.println("后序非递归:" + inorderTraversal(treeNode));
	}

	/**
	 * 后序递归
	 *
	 * @param root
	 * @return
	 */
	public static List<Integer> inorderTraversalByRecursive(TreeNode root) {
		List<Integer> list = new ArrayList<>();
		inorderTraversal(root, list);
		return list;
	}

	public static List<Integer> inorderTraversal(TreeNode root, List<Integer> list) {
		if (root != null) {
			inorderTraversal(root.left, list);
			inorderTraversal(root.right, list);
			list.add(root.val);
		}
		return list;
	}

	/**
	 * 后序非递归
	 * 左子树入栈，取出栈中节点，判断是否右子树为null或右子树是否与当它的当前节点相同，
	 * 如果是：记录节点值，并记录当前值的节点
	 * 如果不是：将取出的节点重新入栈，并将节点设置其右子树，重新遍历
	 *
	 * @param root
	 * @return
	 */
	public static List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> list = new ArrayList<>();
		Stack<TreeNode> stack = new Stack<>();
		TreeNode node = null;
		while (!stack.isEmpty() || root != null) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}

			root = stack.pop();
			if (root.right == null || root.right == node) {
				list.add(root.val);
				node = root;
				root = null;
			} else {
				stack.push(root);
				root = root.right;
			}
		}
		return list;
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
