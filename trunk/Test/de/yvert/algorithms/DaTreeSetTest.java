// arch-tag: 09774b7c-76ec-4e5b-99a0-1bbef5cbbe6f
package de.yvert.algorithms;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DaTreeSetTest extends TestCase
{

private static final int[] NUMS = new int[] {1,34,9,3,17,4,2,6,7,19};

DaTreeSet<Integer> set;

@Override
public void setUp()
{
	set = new DaTreeSet<Integer>(Integer.class);
}

private void fill()
{
	for (int i = 0; i < NUMS.length; i++)
		set.add(new Integer(NUMS[i]));
}

public void testAdd1()
{
	set.add(new Integer(5));
	Assert.assertEquals(1, set.size());
}

public void testAdd2()
{
	set.add(new Integer(5));
	Assert.assertEquals(true, set.contains(new Integer(5)));
}

public void testAdd3()
{
	set.add(new Integer(5));
	set.add(new Integer(3));
	Assert.assertEquals(true, set.contains(new Integer(5)));
	Assert.assertEquals(true, set.contains(new Integer(3)));
}

public void testAdd4()
{
	set.add(new Integer(5));
	set.add(new Integer(5));
	Assert.assertEquals(1, set.size());
}

public void testAddMulti5()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	Assert.assertEquals(nums.length, set.size());
	for (int i = 0; i < nums.length; i++)
		Assert.assertEquals(true, set.contains(new Integer(nums[i])));
}

public void testAddMulti6()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
	{
		set.add(new Integer(nums[i]));
		set.verify();
	}
}

public void testAddMulti7()
{
	int[] nums = new int[] {1,2,3,4,5,6,7,-1,-2,-3,-4,0,8,9,10,-5,0,1,2,-3,11};
	for (int i = 0; i < nums.length; i++)
	{
		set.add(new Integer(nums[i]));
		set.verify();
	}
}

public void testAddMulti8()
{
	int[] nums = new int[] {10,9,8,7,6,5,4,3,2};
	for (int i = 0; i < nums.length; i++)
	{
		set.add(new Integer(nums[i]));
		set.verify();
	}
}

public void testRemove1()
{
	set.add(new Integer(5));
	set.add(new Integer(3));
	set.remove(new Integer(5));
	Assert.assertEquals(1, set.size());
	Assert.assertEquals(true, set.contains(new Integer(3)));
	Assert.assertEquals(false, set.contains(new Integer(5)));
}

public void testRemove2()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	Assert.assertEquals(nums.length, set.size());
	set.remove(new Integer(9));
	set.remove(new Integer(17));
	set.remove(new Integer(7));
	set.remove(new Integer(1));
	Assert.assertEquals(nums.length-4, set.size());
}

public void testRemove3()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	Assert.assertEquals(nums.length, set.size());
	set.remove(new Integer(9)); set.verify();
	set.remove(new Integer(17)); set.verify();
	set.remove(new Integer(7)); set.verify();
	set.remove(new Integer(1)); set.verify();
	Assert.assertEquals(nums.length-4, set.size());
}

public void testRemove4()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	Assert.assertEquals(nums.length, set.size());
	try
	{
		set.remove(new Object());
		Assert.fail();
	}
	catch (ClassCastException e)
	{/*Expected Exceptpion*/}
}

public void testRemove5()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	Assert.assertEquals(nums.length, set.size());
	Assert.assertEquals(false, set.remove(new Integer(20)));
}

public void testRemove6()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	int[] rums = new int[] {0,7,33,14,3,2,9,15,5,5,1};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	for (int i = 0; i < rums.length; i++)
		set.remove(new Integer(rums[i]));
}

public void testContains1()
{
	Assert.assertEquals(false, set.contains(new Integer(3)));
}

public void testContains2()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
//	set.print();
	Assert.assertEquals(true, set.contains(new Integer(2)));
	Assert.assertEquals(true, set.contains(new Integer(17)));
	Assert.assertEquals(false, set.contains(new Integer(5)));
}

public void testClear()
{
	set.add(new Integer(5));
	Assert.assertEquals(1, set.size());
	set.clear();
	Assert.assertEquals(0, set.size());
	Assert.assertEquals(false, set.contains(new Integer(5)));
}

public void testIterator0()
{
	Iterator<Integer> it = set.iterator();
	Assert.assertEquals(false, it.hasNext());
}

public void testIterator1()
{
	Iterator<Integer> it = set.iterator();
	try
	{
		it.next();
		Assert.fail();
	}
	catch (NoSuchElementException e)
	{/*Expected Exception*/}
}

public void testIterator2()
{
	set.add(new Integer(5));
	Iterator<Integer> it = set.iterator();
	Assert.assertEquals(true, it.hasNext());
}

public void testIterator3()
{
	set.add(new Integer(5));
	Iterator<Integer> it = set.iterator();
	Assert.assertEquals(5, it.next().intValue());
}

public void testIterator4()
{
	set.add(new Integer(5));
	Iterator<Integer> it = set.iterator();
	set.add(new Integer(6));
	try
	{
		it.next();
		Assert.fail();
	}
	catch (ConcurrentModificationException e)
	{/*Expected Exception*/}
}

public void testIterator5()
{
	int[] nums = new int[] {1,34,9,3,17,4,2,6,7,19};
	int[] cums = new int[] {1,2,3,4,6,7,9,17,19,34};
	for (int i = 0; i < nums.length; i++)
		set.add(new Integer(nums[i]));
	
//	set.print();
	Iterator<Integer> it = set.iterator();
	for (int i = 0; i < cums.length; i++)
	{
		Assert.assertEquals(cums[i], it.next().intValue());
	}
}

public void testIterator6()
{
	fill();
	Iterator<Integer> it = set.iterator();
	try
	{
		it.remove();
		Assert.fail();
	}
	catch (IllegalStateException e)
	{/*Expected Exception*/}
}

public void testIterator7()
{
	fill();
	Iterator<Integer> it = set.iterator();
	it.next();
	set.add(new Integer(35));
	try
	{
		it.remove();
		Assert.fail();
	}
	catch (ConcurrentModificationException e)
	{/*Expected Exception*/}
}

public void testIterator8()
{
	fill();
	Iterator<Integer> it = set.iterator();
	it.next();
	Assert.assertEquals(true, set.contains(new Integer(1)));
	it.remove();
	Assert.assertEquals(false, set.contains(new Integer(1)));
}

public static Test suite()
{
	return new TestSuite(DaTreeSetTest.class);
}

}
