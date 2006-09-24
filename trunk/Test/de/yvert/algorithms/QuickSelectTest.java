// arch-tag: a3b8f07c-1c7a-495e-bd44-93cde8126df6
package de.yvert.algorithms;

import java.util.Comparator;
import java.util.Random;

import junit.framework.TestCase;

public class QuickSelectTest extends TestCase
{

private Integer[] example()
{
	Integer[] result = new Integer[10];
	result[0] = new Integer(0);
	result[1] = new Integer(1);
	result[2] = new Integer(2);
	result[3] = new Integer(3);
	result[4] = new Integer(4);
	result[5] = new Integer(5);
	result[6] = new Integer(6);
	result[7] = new Integer(7);
	result[8] = new Integer(8);
	result[9] = new Integer(9);
	return result;
}

private <T> void swap(T[] data, int i, int j)
{ T t = data[i]; data[i] = data[j]; data[j] = t; }

private <T> void quickCheck(T[] data, int first, int afterLast, Comparator<T> comp, int pivot)
{
	for (int i = first; i < pivot; i++)
		assertTrue(comp.compare(data[i], data[pivot]) <= 0);
	for (int i = pivot+1; i < afterLast; i++)
		assertTrue(comp.compare(data[pivot], data[i]) <= 0);
}

private <T extends Comparable<T>> void quickCheck(T[] data, int pivot)
{
	quickCheck(data, 0, data.length, new Comparator<T>()
		{
			public int compare(T o1, T o2)
			{ return o1.compareTo(o2); }
		}, pivot);
}

public void testSimple1()
{
	Integer[] data = example();
	QuickSelect.quickSelect(data, 3);
	quickCheck(data, 3);
}

public void testSimple2()
{
	Integer[] data = example(); swap(data, 0, 3);
	QuickSelect.quickSelect(data, 3);
	quickCheck(data, 3);
}

public void testSimple3()
{
	Integer[] data = example(); swap(data, 0, 9); swap(data, 3, 5); swap(data, 6, 7);
	QuickSelect.quickSelect(data, 3);
	quickCheck(data, 3);
}

public void testSimple4()
{
	Integer[] data = example(); swap(data, 0, 9); swap(data, 3, 5); swap(data, 6, 7);
	QuickSelect.quickSelect(data, 0);
	quickCheck(data, 0);
}

public void testSimple5()
{
	Integer[] data = example(); swap(data, 0, 9); swap(data, 3, 5); swap(data, 6, 7);
	QuickSelect.quickSelect(data, 9);
	quickCheck(data, 9);
}

public void testInvalidParam1()
{
	try
	{
		Integer[] data = example();
		QuickSelect.quickSelect(data, 4, 3, 9);
		fail();
	}
	catch (IllegalArgumentException e)
	{/*Expected Exception*/}
}

public void testInvalidParam2()
{
	try
	{
		Integer[] data = example();
		QuickSelect.quickSelect(data, -1, 3, 2);
		fail();
	}
	catch (ArrayIndexOutOfBoundsException e)
	{/*Expected Exception*/}
}

public void testInvalidParam3()
{
	try
	{
		Integer[] data = example();
		QuickSelect.quickSelect(data, 0, data.length+1, 2);
		fail();
	}
	catch (ArrayIndexOutOfBoundsException e)
	{/*Expected Exception*/}
}

private void doubleTest(double[] data, int pivot)
{
	Double[] d = new Double[data.length];
	for (int i = 0; i < d.length; i++)
		d[i] = new Double(data[i]);
	QuickSelect.quickSelect(d, pivot);
	quickCheck(d, pivot);
}

public void testFailed1()
{
	double[] data = new double[]
		{-4.04, -2.72, -2.82, -2.31,
			-5.0, -4.86, -4.97, -2.18, -1.70,
			-1.18, -0.06, 0.36, -0.26};
	Double[] d = new Double[data.length];
	for (int i = 0; i < d.length; i++)
		d[i] = new Double(data[i]);
	int pivot = 5;
	QuickSelect.quickSelect(d, pivot);
	quickCheck(d, pivot);
//	Z_SORTER 0 23 11
}

public void testFailed2()
{
	double[] data = new double[]
		{-4, -2, -5, -4, -2, -1, -1};
	Double[] d = new Double[data.length];
	for (int i = 0; i < d.length; i++)
		d[i] = new Double(data[i]);
	int pivot = 1;
	QuickSelect.quickSelect(d, pivot);
	quickCheck(d, pivot);
//	Z_SORTER 0 23 11
}

public void testFailed3()
{
	double[] data = new double[]
		{-4, -2, -5, -4};
	Double[] d = new Double[data.length];
	for (int i = 0; i < d.length; i++)
		d[i] = new Double(data[i]);
	int pivot = 2;
	QuickSelect.quickSelect(d, pivot);
	quickCheck(d, pivot);
}

public void testFailed4()
{
	double[] data = new double[]
		{-4, -5};
	Double[] d = new Double[data.length];
	for (int i = 0; i < d.length; i++)
		d[i] = new Double(data[i]);
	int pivot = 1;
	QuickSelect.quickSelect(d, pivot);
	quickCheck(d, pivot);
//	Z_SORTER 0 23 11
}

public void testFailed5()
{
	double[] data = new double[] 
		{-2.13476089235256, -2.2702082715985608, -2.4686730998064785, -5.0,
			1.3523213221001458, 5.0, 4.108269063602341, 5.0, 5.0, 5.0, 5.0};
	doubleTest(data, 5);
//X_SORTER 0 11 5
}

public void testFailed6()
{
	double[] data = new double[]
		{-5.0, -5.0, -5.0, -2.747750761576997, -4.26391649459181, -4.956409056044822, 
			-5.0, -3.1706837253812212};
	doubleTest(data, 3);
}

public void testFailed7()
{
	double[] data = new double[]
		{6.0, 7.0, 0.0, 9.0, 6.0, 0.0, 7.0, 6.0, 6.0, 3.0};
	doubleTest(data, 5);
}

public void testRandom()
{
	double[] data = new double[10];
	Random rand = new Random(1234);
	for (int j = 0; j < 100; j++)
	{
		for (int i = 0; i < data.length; i++)
			data[i] = rand.nextInt(10);
//		System.out.println(java.util.Arrays.toString(data));
		doubleTest(data, 5);
	}
}

}
