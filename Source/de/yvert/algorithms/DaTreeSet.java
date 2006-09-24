// arch-tag: 5ab3287b-22c9-49ab-b175-3e099a717bd7
package de.yvert.algorithms;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Red-Black Tree based implementation of the <code>Set</code> interface.
 * 
 * This class guarantees that the map will be in ascending key order, sorted according 
 * to the natural order for the key's class (see Comparable), or by the comparator 
 * provided at creation time, depending on which constructor is used.
 * 
 * The algorithms are translations (from C to Java) of the algorithms described in the
 * <a href="http://en.wikipedia.org/wiki/Red-black_tree">Wikipedia article on Red-Black 
 * Trees</a>.
 * 
 * This class has special provisions to deal with mixed types. For example, it
 * is possible to find the left neighbor of a vertex in a set of lines. This is 
 * necessary for some computational geometry algorithms.
 */
public class DaTreeSet<Key> extends AbstractSet<Key>
{

private enum Color { RED, BLACK }

private static class Node<T>
{
	Color color;
	Node<T> left;
	Node<T> right;
	Node<T> parent;
	T value;
	
	private Node(T value)
	{
		this.color = Color.RED;
		this.value = value;
	}
}

private static class StandardComparator<T> implements Comparator<T>
{
	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2)
	{ return ((Comparable<T>) o1).compareTo(o2); }
}

private class KeyIterator implements Iterator<Key>
{
	private int expectedModCount = modCount;
	private Node<Key> lastNode = null;
	private Node<Key> nextNode = null;
	
	KeyIterator()
	{
		nextNode = root;
		if (nextNode != null)
		{
			while (nextNode.left != null)
				nextNode = nextNode.left;
		}
	}
	
	public boolean hasNext()
	{ return nextNode != null; }
	
	public Key next()
	{
		if (nextNode == null)
			throw new NoSuchElementException();
		if (expectedModCount != modCount)
			throw new ConcurrentModificationException();
		lastNode = nextNode;
		nextNode = successor(nextNode);
		return lastNode.value;
	}
	
	public void remove()
	{
		if (lastNode == null)
			throw new IllegalStateException();
		if (expectedModCount != modCount)
			throw new ConcurrentModificationException();
		
		// careful: removeNode can modify one other node besides the given one
		// but in our impl it will pick the predecessor of lastNode
		removeNode(lastNode);
		expectedModCount++;
		
		lastNode = null;
	}
}

private int modCount = 0;
private Class<Key> elementType;
private Comparator<Key> comp;
private int size;
private Node<Key> root;

public DaTreeSet(Class<Key> elementType, Comparator<Key> comp)
{
	this.elementType = elementType;
	this.comp = comp;
	this.size = 0;
	this.root = null;
}

public DaTreeSet(Class<Key> elementType)
{
	this.elementType = elementType;
	this.comp = new StandardComparator<Key>();
	this.size = 0;
	this.root = null;
}

private void typeCheck(Key key)
{ elementType.cast(key); }

/* Changes:
 *   x            y
 *  / \          / \
 * A   y   ->   x   C
 *    / \      / \
 *   B   C    A   B
 * and fixes the parent of x to point to y.
 */
private void rotateLeft(Node<Key> x)
{
	Node<Key> y = x.right;
	
	// change y's left sub-tree to x's right sub-tree
	x.right = y.left;
	if (x.right != null) x.right.parent = x;
	
	// x's parent must point to y instead of x
	y.parent = x.parent;
	if (x.parent == null)
		root = y;
	else
	{
		if (x == x.parent.left)
			x.parent.left = y;
		else
			x.parent.right = y;
	}
	
	// put x on y's left
	y.left = x;
	x.parent = y;
}

/* Changes
 *     x          y
 *    / \        / \
 *   y   C  ->  A   x
 *  / \            / \
 * A   B          B   C
 * and fixes the parent of x to point to y.
 */
private void rotateRight(Node<Key> x)
{
	Node<Key> y = x.left;
	
	// change y's right sub-tree to x's left sub-tree
	x.left = y.right;
	if (x.left != null) x.left.parent = x;
	
	// x's parent must point to y instead of x
	y.parent = x.parent;
	if (x.parent == null)
		root = y;
	else
	{
		y.parent = x.parent;
		if (x == x.parent.left)
			x.parent.left = y;
		else
			x.parent.right = y;
	}
	
	// put x on y's right
	y.right = x;
	x.parent = y;
}

private boolean insert(Node<Key> x)
{
	if (root == null)
	{
		root = x;
		size = 1;
		modCount++;
		return true;
	}
	
	Node<Key> n = root;
	while (true)
	{
		int cmp = comp.compare(x.value, n.value);
		if (cmp == 0)
			return false;
		if (cmp < 0)
		{
			if (n.left == null)
			{
				n.left = x;
				x.parent = n;
				size++;
				modCount++;
				return true;
			}
			n = n.left;
		}
		else
		{
			if (n.right == null)
			{
				n.right = x;
				x.parent = n;
				size++;
				modCount++;
				return true;
			}
			n = n.right;
		}
	}
}

private Node<Key> find(Key key)
{
	Node<Key> n = root;
	while (n != null)
	{
		int cmp = comp.compare(key, n.value);
		if (cmp == 0)
			return n;
		if (cmp < 0)
		{
			if (n.left == null)
				return null;
			n = n.left;
		}
		else
		{
			if (n.right == null)
				return null;
			n = n.right;
		}
	}
	return null;
}

private void print(Node<Key> node, int indent)
{
	if (node == null) return;
	for (int i = 0; i < indent; i++)
		System.out.print("  ");
	System.out.println(node.value+" "+node.color);
	print(node.left, indent+1);
	print(node.right, indent+1);
}

private void print(Node<Key> node)
{
	print(node, 0);
	System.out.println();
}

void print()
{ print(root); }

private Node<Key> sibling(Node<Key> x, Node<Key> p)
{
	if (x == p.left)
		return p.right;
	else
		return p.left;
}

private Node<Key> uncle(Node<Key> x)
{
	if (x.parent == x.parent.parent.left)
		return x.parent.parent.right;
	else
		return x.parent.parent.left;
}

private Node<Key> predecessor(Node<Key> x)
{
	if (x == null)
		return null;
	else if (x.left != null)
	{
		x = x.left;
		while (x.right != null)
			x = x.right;
		return x;
	}
	else
	{
		Node<Key> p = x.parent;
		while ((p != null) && (x == p.left))
		{
			x = p;
			p = x.parent;
		}
		return p;
	}
}

private Node<Key> successor(Node<Key> x)
{
	if (x == null)
		return null;
	else if (x.right != null)
	{
		x = x.right;
		while (x.left != null)
			x = x.left;
		return x;
	}
	else
	{
		Node<Key> p = x.parent;
		while ((p != null) && (x == p.right))
		{
			x = p;
			p = x.parent;
		}
		return p;
	}
}

/**
 * x must be RED!
 */
private boolean addNode(Node<Key> x)
{
	// insert node
	if (!insert(x)) return false;
	
	// Case 1: p == null
	while (x.parent != null)
	{
		// Case 2: p is BLACK
		if (x.parent.color == Color.BLACK)
			return true;
		else
		{
			Node<Key> u = uncle(x);
			// Case 3: u is RED
			if ((u != null) && (u.color == Color.RED))
			{
				x.parent.color = Color.BLACK;
				u.color = Color.BLACK;
				x.parent.parent.color = Color.RED;
				x = x.parent.parent;
				// Repeat!
			}
			else
			{
				// Case 4: prepare for 5
				if ((x == x.parent.right) && (x.parent == x.parent.parent.left))
				{
					rotateLeft(x.parent);
					x = x.left;
				}
				else if ((x == x.parent.left) && (x.parent == x.parent.parent.right))
				{
					rotateRight(x.parent);
					x = x.right;
				}
				
				// Case 5: p is RED, u is BLACK (gp must be BLACK)
				x.parent.color = Color.BLACK;
				x.parent.parent.color = Color.RED;
				if ((x == x.parent.left) && (x.parent == x.parent.parent.left))
					rotateRight(x.parent.parent);
				else
					rotateLeft(x.parent.parent);
				return true;
			}
		}
	}
	
	x.color = Color.BLACK;
  return true;
}

@Override
public boolean add(Key key)
{
	if (key == null) throw new NullPointerException();
	typeCheck(key);
	boolean result = addNode(new Node<Key>(key));
//	print(root);
	return result;
}

private void removeNode(Node<Key> x)
{
	size--;
	modCount++;
	
	// if x has two children, we find the predecessor of x, copy the key and delete that node
	// this operation changes neither the order nor the red-black-property
	if ((x.left != null) && (x.right != null))
	{
		Node<Key> y = predecessor(x);
		x.value = y.value;
		y.value = null;
		x = y;
	}
	
	// find the child
	Node<Key> y;
	if (x.left != null)
		y = x.left;
	else
		y = x.right;
	
	// move y up
	if (y != null) y.parent = x.parent;
	if (x.parent == null)
		root = y;
	else
	{
		if (x == x.parent.left)
			x.parent.left = y;
		else
			x.parent.right = y;
	}
	
	if (x.color == Color.RED)
		return;
	
	if ((y != null) && (y.color == Color.RED))
	{
		y.color = Color.BLACK;
		return;
	}
	
	Node<Key> p = x.parent;
	x = y;
	
	// Case 1: p is null
	while (p != null)
	{
		Node<Key> s = sibling(x, p);
		
		// Case 2: s is RED
		if (s.color == Color.RED)
		{
			p.color = Color.RED;
			s.color = Color.BLACK;
			if (x == p.left)
				rotateLeft(p);
			else
				rotateRight(p);
			
			s = sibling(x, p);
		}
		
		// Case 3: p, s, sl and sr are BLACK
		if ((p.color == Color.BLACK) && (s.color == Color.BLACK) &&
				((s.left == null) || (s.left.color == Color.BLACK)) &&
				((s.right == null) || (s.right.color == Color.BLACK)))
		{
			s.color = Color.RED;
			x = p;
			p = x.parent;
			// Repeat!
		}
		else
		{
			// Case 4: n, s, sl, sr are BLACK, p is RED
			if ((p.color == Color.RED) && (s.color == Color.BLACK) &&
	        ((s.left == null) || (s.left.color == Color.BLACK)) && 
	        ((s.right == null) || (s.right.color == Color.BLACK)))
			{
				s.color = Color.RED;
				p.color = Color.BLACK;
				return;
			}
			else
			{
				// Case 5:
				//   if x is p.left: s, sr are BLACK, sl is RED
				//   if x is p.right: s, sl are BLACK, sr is RED
	    	if ((x == p.left) && (s.color == Color.BLACK) &&
	    			((s.left != null) && (s.left.color == Color.RED)) &&
	    			((s.right == null) || (s.right.color == Color.BLACK)))
	    	{
	    		s.color = Color.RED;
	    		s.left.color = Color.BLACK;
	    		rotateRight(s);
	    	}
	    	else if ((x == p.right) && (s.color == Color.BLACK) &&
	    			((s.right != null) && (s.right.color == Color.RED)) && 
	    			((s.left == null) || (s.left.color == Color.BLACK)))
				{
	    		s.color = Color.RED;
	    		s.right.color = Color.BLACK;
	    		rotateLeft(s);
				}
	    	
	    	// x and p stay, s may change
	    	s = sibling(x, p);
	    	
	    	// Case 6:
	    	// if x is p.left: x and s are BLACK, sr is RED
	    	// if x is p.right: x and s are BLACK, sl is RED
				s.color = p.color;
				p.color = Color.BLACK;
				if (x == p.left)
				{
					s.right.color = Color.BLACK;
					rotateLeft(p);
				}
				else
				{
					s.left.color = Color.BLACK;
					rotateRight(p);
				}
				return;
	    }
		}
	}
}

@Override
public boolean remove(Object o)
{
	Node<Key> x = find(elementType.cast(o));
	if (x == null) return false;
	removeNode(x);
//	print(root);
	return true;
}

@Override
public void clear()
{
	size = 0;
	root = null;
}

@Override
public boolean contains(Object o)
{
	return find(elementType.cast(o)) != null;
}

@Override
public Iterator<Key> iterator()
{ return new KeyIterator(); }

@Override
public int size()
{ return size; }

/**
 * Finds the biggest element which is smaller than or equal to the given element.
 */
public <T> Key findLeftNeighbour(T element, Finder<T,Key> finder)
{
	Node<Key> n = root;
	Key result = null;
	while (n != null)
	{
		int cmp = finder.compare(element, n.value);
		if (cmp == 0)
			return n.value;
		if (cmp < 0)
		{
			if (n.left == null)
				return result;
			n = n.left;
		}
		else
		{
			result = n.value;
			if (n.right == null)
				return result;
			n = n.right;
		}
	}
	return result;
}


/* 
 * Verification functions.
 * Package private so that they can be called by DaTreeSetTest.
 */
void verifySum(Node<Key> n, int value, int current)
{
	if (n == null)
	{
		if (value != current)
			throw new RuntimeException("Invalid count!");
		return;
	}
	
	if (n.color == Color.BLACK)
		current++;
	else
	{
		if ((n.left != null) && (n.left.color == Color.RED))
			throw new RuntimeException("Invalid color!");
		if ((n.right != null) && (n.right.color == Color.RED))
			throw new RuntimeException("Invalid color!");
	}
	
	verifySum(n.left, value, current);
	verifySum(n.right, value, current);
}

int calculate(Node<Key> n)
{
	int result = 0;
	while (n != null)
	{
		if (n.color == Color.BLACK)
			result++;
		n = n.left;
	}
	return result;
}

void verify()
{
	int sum = calculate(root);
	verifySum(root, sum, 0);
}

}
