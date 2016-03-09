/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr&iacute;guez
 *
 * Programming Assignment #2
 *
 *
 * Kevin Liu
 */
package Kevin_Ruoxi_Liu;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RBT<K extends Comparable<K>, V>{
	Scanner sc = new Scanner(System.in);
	private Node root = null;
	private int logicalSize = 0;

	public void add(K key, V value) {
		if (logicalSize == 0) {
			root = new Node(null, key, value, true);
			root.leftChild = new Node(root, null, null, false);
			root.rightChild = new Node(root, null, null, false);
			root.red = false;
			logicalSize++;
		} else {
			Node currentNode = root;

			while (currentNode.key != null) {

				if (currentNode.leftChild.key == null && currentNode.key.compareTo(key) > 0) {
					Node added = new Node(currentNode, key, value, true);
					added.leftChild = new Node(added, null, null, false);
					added.rightChild = new Node(added, null, null, false);
					currentNode.leftChild = added;
					addFixOper(currentNode.leftChild);
					return;
				}
				if (currentNode.rightChild.key == null && currentNode.key.compareTo(key) < 0) {
					Node added = new Node(currentNode, key, value, true);
					added.leftChild = new Node(added, null, null, false);
					added.rightChild = new Node(added, null, null, false);
					currentNode.rightChild = added;
					addFixOper(currentNode.rightChild);
					return;
				}

				if (currentNode.key.compareTo(key) < 0) {
					currentNode = currentNode.rightChild;
					System.out.println("moved right");
					System.out.println(value);

				} else if (currentNode.key.compareTo(key) > 0) {
					currentNode = currentNode.leftChild;
					System.out.println("moved left");
					System.out.println(value);

				} else if (currentNode.key.compareTo(key) == 0) {
					System.out.println("key exists");
					return;
				}

			}
		}
		root.red = false;
	}

	public V lookup(K key) {
		V searchValue = null;
		Node currentNode = root;
		while (currentNode.key != null) {
			if (currentNode.key.compareTo(key) > 0) {
				if (currentNode.leftChild.key == null) {
					break;
				}
				currentNode = currentNode.leftChild;
			}
			if (currentNode.key.compareTo(key) < 0) {
				if (currentNode.rightChild.key == null) {
					break;
				}
				currentNode = currentNode.rightChild;
			}

			if (currentNode.key.compareTo(key) == 0 && currentNode.key != null) {
				break;
			}

		}

		if (currentNode.key.compareTo(key) == 0 && currentNode.key != null) {
			searchValue = (V) currentNode.value;
		}
		return searchValue;
	}

	private int addCaseNum(Node currentNode) {
		if (currentNode.equals(root)) {
			return 0;
		}
		if (!currentNode.parent.red) {
			return 1;
		} else if (currentNode.parent.red && uncleOf(currentNode).red) {
			return 2;
		} else if (currentNode.parent.red && !uncleOf(currentNode).red && isInternal(currentNode)) {
			return 3;
		} else if (currentNode.parent.red && !uncleOf(currentNode).red && !isInternal(currentNode)) {
			return 4;
		}
		return -1;
	}

	private void addFixOper(Node currentNode) {
		int caseNum = addCaseNum(currentNode);

		switch (caseNum) {

		case 0:
			root.red = false;
			break;
		case 1:
			addCase1(currentNode);
			break;
		case 2:
			addCase2(currentNode);
			break;
		case 3:
			addCase3(currentNode);
			break;
		case 4:
			addCase4(currentNode);
			addFixOper(root);
			break;
		}

	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	public V remove(K key) {
		V removedValue = null;
		Node currentNode = root;
		while (currentNode.key != null) {
			if (currentNode.key.compareTo(key) > 0) {
				if (currentNode.leftChild.key == null) {
					break;
				}
				currentNode = currentNode.leftChild;
			}
			if (currentNode.key.compareTo(key) < 0) {
				if (currentNode.rightChild.key == null) {
					break;
				}
				currentNode = currentNode.rightChild;
			}

			if (currentNode.key.compareTo(key) == 0 && currentNode.key != null) {
				break;
			}

		}

		removeHelper(currentNode);

		if (currentNode.key != null && currentNode.key.compareTo(key) == 0) {
			removedValue = (V) currentNode.value;
		}

		return null;
	}

	private void removeHelper(Node node) {
		int num = removeNum(node);
		Node temp;
		switch (num) {
		case 1:
			System.out.println("remove 1 hit");
			if (isLeft(node)) {
				removeFixOper(node);
				node.parent.leftChild = new Node(node.parent, null, null, false);

			} else {
				removeFixOper(node);
				node.parent.rightChild = new Node(node.parent, null, null, false);

			}

			logicalSize--;
			break;
		case 2:
			System.out.println("remove 2 hit");
			if (isLeft(node)) {
				if (node.leftChild.key != null) {
					removeFixOper(node.parent.leftChild);
					temp = node.leftChild;
					temp.parent = node.parent;
					node.parent.leftChild = temp;

				} else {
					removeFixOper(node.parent.rightChild);
					temp = node.rightChild;
					temp.parent = node.parent;
					node.parent.leftChild = temp;

				}

			} else {
				removeFixOper(node.parent.leftChild);
				if (node.leftChild.key != null) {
					temp = node.leftChild;
					temp.parent = node.parent;
					node.parent.rightChild = temp;

				} else {
					removeFixOper(node.parent.rightChild);
					temp = node.rightChild;
					temp.parent = node.parent;
					node.parent.rightChild = temp;

				}
			}
			logicalSize--;
			break;
		case 3:
			System.out.println("remove 3 hit");
			Node iOS = iOSuc(node);
			Node temp2 = null;
			keyValSwap(node, iOS);
			temp = node;
			temp = node.rightChild;
			while (temp.leftChild.key != null) {
				if (temp.leftChild.key != null) {
					temp = temp.leftChild;
				}
			}

			temp2 = temp;

			temp2.rightChild.parent = temp.parent;
			temp.parent.leftChild = temp2;

			temp.parent.leftChild = new Node(temp.parent, null, null, false);
			removeFixOper(temp.parent);

			break;
		}
	}

	private int removeNum(Node currentNode) {
		int caseNum = -1;
		if (currentNode.leftChild.key == null && currentNode.rightChild.key == null) {
			caseNum = 1;
		}
		if ((currentNode.rightChild.key != null && currentNode.leftChild.key == null)
				|| currentNode.rightChild.key == null && currentNode.leftChild.key != null) {
			caseNum = 2;
		}
		if (currentNode.rightChild.key != null && currentNode.leftChild.key != null) {
			caseNum = 3;
		}
		return caseNum;
	}

	private Node iOSuc(Node node) {
		Node currentNode = node.rightChild;
		while (!leaf(currentNode.leftChild)) {
			currentNode = currentNode.leftChild;
		}
		return currentNode;
	}

	private void removeFixOper(Node node) {
		int num = removeCaseNum(node);
		switch (num) {
		case 0:
			System.out.println("rCase0");
			removeCase0(node);
			break;
		case 1:
			System.out.println("rCase1");
			removeCase1(node);
			break;
		case 2:
			System.out.println("rCase2");
			removeCase2(node);
			break;
		case 3:
			System.out.println("rCase3");
			removeCase3(node);
			break;
		case 4:
			System.out.println("rCase4");
			removeCase4(node);
			break;
		}

	}

	private int removeCaseNum(Node currentNode) {
		int num = -1;
		Node sibling = sibling(currentNode);

		if (currentNode.equals(root)) {
			root.red = false;
		} else if (sibling.red) {
			num = 1;
		} else if (!sibling.red && !sibling.leftChild.red && !sibling.rightChild.red) {
			num = 2;
		} else if (!sibling.red && internalChild(currentNode).red && !externalChild(currentNode).red) {
			num = 3;
		} else if (!sibling.red && !internalChild(currentNode).red && externalChild(currentNode).red) {
			num = 4;
		}

		return num;
	}

	private void removeCase0(Node node) {
		if (node.equals(root)) {
			System.out.println("remove case 0");
			root.red = false;
		}
	}

	private void removeCase1(Node node) {
		System.out.println("remove case 1");
		if (isLeft(node)) {
			colorSwap(sibling(node), node.parent);
			leftRotate(node.parent);

		} else {
			colorSwap(sibling(node), node.parent);
			rightRotate(node.parent);

		}
		System.out.println(toPrettyString());
		removeFixOper(node);

	}

	private void removeCase2(Node node) {

		System.out.println("remove case 2");
		sibling(node).red = true;
		if (!node.parent.red) {
			removeFixOper(node.parent);
		} else {
			removeFixOper(node.parent);
		}

	}

	private void removeCase3(Node node) {
		System.out.println("remove case 3");

		if (isLeft(node)) {
			colorSwap(node.parent.rightChild.rightChild, node.parent.rightChild);
			rightRotate(node.parent.rightChild);

		} else {
			colorSwap(node.parent.leftChild.leftChild, node.parent.leftChild);
			leftRotate(node.parent.leftChild);

		}

		removeCase4(node);

	}

	private void removeCase4(Node node) {
		System.out.println("remove case 4");
		Node sibling = uncleOf(node.leftChild);
		if (isLeft(node)) {
			colorSwap(node, sibling);

			leftRotate(node.parent);
		} else {
			colorSwap(node, sibling);

			rightRotate(node.parent);
		}

		removeFixOper(root);

	}

	private void addCase1(Node currentNode) {
		System.out.println("add case 1");
	}

	private void addCase2(Node currentNode) {
		System.out.println("add case 2 triggered");
		currentNode.parent.red = false;
		uncleOf(currentNode).red = false;
		currentNode.parent.parent.red = true;
		if (currentNode.parent.parent.equals(root)) {
			currentNode.parent.parent.red = false;
		}
		addFixOper(currentNode.parent.parent);

	}

	private void addCase3(Node currentNode) {
		System.out.println("add case 3 triggered");
		if (isLeft(currentNode)) {
			rightRotate(currentNode.parent);
			addCase4(currentNode.rightChild);
		} else {
			leftRotate(currentNode.parent);
			addCase4(currentNode.leftChild);
		}

	}

	private void addCase4(Node currentNode) {
		System.out.println("add case 4 triggered");
		boolean tempRed;
		if (isLeft(currentNode.parent)) {
			colorSwap(currentNode.parent, currentNode.parent.parent);
			rightRotate(currentNode.parent.parent);

		} else {
			colorSwap(currentNode.parent, currentNode.parent.parent);
			leftRotate(currentNode.parent.parent);

		}
	}

	private void keyValSwap(Node node1, Node node2) {
		K tempK = (K) node1.key;
		V tempV = (V) node1.value;
		node1.key = node2.key;
		node2.key = tempK;
		node1.value = node2.value;
		node2.value = tempV;
	}

	private void colorSwap(Node node1, Node node2) {
		boolean tempRed = node1.red;
		node1.red = node2.red;
		node2.red = tempRed;
	}

	private void leftRotate(Node node) {
		Node rightNode = node.rightChild;
		replaceNode(node, rightNode);
		node.rightChild = rightNode.leftChild;
		if (rightNode.leftChild != null) {
			rightNode.leftChild.parent = node;
		}
		rightNode.leftChild = node;
		node.parent = rightNode;
	}

	private void rightRotate(Node node) {
		Node leftNode = node.leftChild;
		replaceNode(node, leftNode);
		node.leftChild = leftNode.rightChild;
		if (leftNode.rightChild != null) {
			leftNode.rightChild.parent = node;
		}
		leftNode.rightChild = node;
		node.parent = leftNode;
	}

	private void replaceNode(Node pivot, Node<K, V> child) {
		if (pivot.equals(root)) {
			root = child;
		} else {
			if (isLeft(pivot)) {
				pivot.parent.leftChild = child;
			} else {
				pivot.parent.rightChild = child;
			}
		}
		if (child != null) {
			child.parent = pivot.parent;
		}
	}

	private boolean isInternal(Node currentNode) {
		boolean internal = true;
		if (currentNode.parent.leftChild.equals(currentNode)
				&& currentNode.parent.parent.leftChild.equals(currentNode.parent)) {
			internal = false;
		} else if (currentNode.parent.rightChild.equals(currentNode)
				&& currentNode.parent.parent.rightChild.equals(currentNode.parent)) {
			internal = false;
		}

		return internal;

	}

	private Node internalChild(Node node) {
		Node internalChild = node;
		if (isLeft(internalChild)) {
			internalChild = node.rightChild;
		} else {
			internalChild = node.leftChild;
		}
		return internalChild;
	}

	private Node externalChild(Node node) {
		Node externalChild = node;
		if (isLeft(externalChild)) {
			externalChild = externalChild.leftChild;
		} else {
			externalChild = externalChild.rightChild;
		}
		return externalChild;
	}

	private Node uncleOf(Node currentNode) {
		Node uncle = null;
		if (!currentNode.parent.equals(root)) {
			if (isLeft(currentNode.parent)) {
				uncle = currentNode.parent.parent.rightChild;
			} else {
				uncle = currentNode.parent.parent.leftChild;
			}
		}
		return uncle;
	}

	private boolean isLeft(Node currentNode) {
		boolean left = false;
		if (!currentNode.equals(root)) {
			if (currentNode.parent.leftChild.equals(currentNode)) {
				left = true;
			}
		}
		return left;
	}

	public void breadth() {
		Queue<Node> queue = new LinkedList<Node>();
		Node[][] prettyArray = new Node[1000][1000];
		if (root == null)
			return;
		queue.add(root);

		while (!queue.isEmpty()) {
			Node node = queue.remove();
			System.out.print(node.value + " ");
			if (node.leftChild != null)
				queue.add(node.leftChild);
			if (node.rightChild != null)
				queue.add(node.rightChild);

		}

	}

	public int depth() {
		return depth(root);
	}

	public int depth(Node node) {
		if (node == null) {
			return 0;
		} else {
			int leftHalf = depth(node.leftChild);
			int rightHalf = depth(node.rightChild);
			if (leftHalf > rightHalf) {
				return leftHalf + 1;
			} else {
				return rightHalf + 1;
			}
		}
	}

	public String toPrettyString() {
		int depth = depth(root);
		String sxcString = "";
		Node[][] sxcArray = new Node[depth][(int) Math.pow(2, depth)];
		sxcArray[0][0] = root;
		for (int i = 1; i < depth; i++) {
			for (int j = 0; j < Math.pow(2, i - 1); j++) {
				if (sxcArray[i - 1][j] != null) {
					sxcArray[i][2 * j] = sxcArray[i - 1][j].leftChild;
					sxcArray[i][(2 * j) + 1] = sxcArray[i - 1][j].rightChild;
				}
			}
		}

		String gap = "------";
		// System.out.println();
		// System.out.println(depth);
		int levels = depth - 1;
		for (int i = 0; i < depth; i++) {
			sxcString = sxcString + "\n";
			for (int x = 0; x < (Math.pow(2, (levels)) - Math.pow(2, i)) / 2; x++) {
				sxcString = sxcString + gap;
			}

			String r = "";
			String val = "";

			for (int j = 0; j < Math.pow(2, i); j++) {
				if (sxcArray[i][j] != null) {
					if (sxcArray[i][j].red) {
						r = "r";
					} else {
						r = "b";
					}
					if (sxcArray[i][j].value != null) {
						val = "" + sxcArray[i][j].value;
					} else {
						val = "n";
					}
				}
				if (sxcArray[i][j] != null)
					sxcString = sxcString + "(" + val + "," + r + ")";
				if (sxcArray[i][j] == null)
					sxcString = sxcString + "(   )";
			}
		}

		return sxcString;
	}

	private Node sibling(Node node) {
		Node sibling = null;
		if (node.parent == null) {
			return null;
		}
		if (node.parent.leftChild.equals(node)) {
			sibling = node.parent.rightChild;
		} else {
			sibling = node.parent.leftChild;
		}
		return sibling;
	}

	private boolean leaf(Node currentNode) {
		boolean leaf = false;
		if (currentNode.key == null && currentNode.value == null) {
			leaf = true;
		}
		return leaf;
	}

	private class Node<K extends Comparable<K>, V> {
		protected boolean red;
		protected V value;
		protected Node parent;
		protected Node leftChild;
		protected Node rightChild;
		protected K key;

		private Node(Node p, K k, V v, boolean r) {
			red = r;
			this.key = k;
			this.parent = p;
			this.value = v;

		}

	}
}
