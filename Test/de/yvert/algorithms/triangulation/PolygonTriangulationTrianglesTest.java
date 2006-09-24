// arch-tag: f1faa994-d8e8-4960-9722-0128096560b1
package de.yvert.algorithms.triangulation;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.yvert.geometry.Vector2;

public class PolygonTriangulationTrianglesTest extends TestCase
{

static class Triple
{ int a, b, c; Triple(int a, int b, int c) { this.a = a; this.b = b; this.c = c; } }

private ArrayList<Triple> collectTriangles(Vector2[] data)
{
	final ArrayList<Triple> result = new ArrayList<Triple>();
	new PolygonTriangulator2D().calculate(data, new TriangulationAdapter()
		{
			@Override
			public void addTriangle(int a, int b, int c)
			{
//				System.out.println(a+" -> "+b+" -> "+c);
				result.add(new Triple(a, b, c));
			}
		});
	return result;
}

private boolean contains(ArrayList<Triple> result, int a, int b, int c)
{
	for (Triple p : result)
		if (((p.a == a) && (p.b == b) && (p.c == c)) ||
				((p.a == b) && (p.b == c) && (p.c == a)) ||
				((p.a == c) && (p.b == a) && (p.c == b)))
			return true;
	return false;
}

public void testTriangulate1()
{
	// triangle
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(10.0,  0.0);
	data[2] = new Vector2( 0.0, 10.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 0, 1, 2));
}

public void testTriangulate2()
{
	// end vertex that has a merge vertex as helper
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  1.0);
	data[2] = new Vector2( 0.0, -2.0);
	data[3] = new Vector2( 1.0,  1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 2, 3, 0));
}

public void testTriangulate3()
{
	// split vertex
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 1.0, -1.0);
	data[2] = new Vector2( 0.0,  2.0);
	data[3] = new Vector2(-1.0, -1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 2, 3, 0));
}

public void testTriangulate4()
{
	// merge vertex that has a merge vertex as helper
	Vector2[] data = new Vector2[6];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  1.0);
	data[2] = new Vector2( 1.0, -2.0);
	data[3] = new Vector2( 3.0,  2.0);
	data[4] = new Vector2( 2.0,  1.0);
	data[5] = new Vector2( 1.0,  2.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(4, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 4, 5));
	assertTrue(contains(result, 2, 4, 0));
	assertTrue(contains(result, 2, 3, 4));
}

public void testTriangulate5()
{
	// merge vertex whose left neighbour has a merge vertex as helper
	Vector2[] data = new Vector2[6];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  2.0);
	data[2] = new Vector2(-2.0,  1.0);
	data[3] = new Vector2(-3.0,  2.0);
	data[4] = new Vector2(-1.0, -2.0);
	data[5] = new Vector2( 1.0,  1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(4, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 2, 4));
	assertTrue(contains(result, 2, 3, 4));
	assertTrue(contains(result, 4, 5, 0));
}

public void testTriangulate6()
{
	// regular left vertex that has a merge vertex as helper
	Vector2[] data = new Vector2[5];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  1.0);
	data[2] = new Vector2(-1.5, -1.0);
	data[3] = new Vector2( 0.0, -2.0);
	data[4] = new Vector2( 1.0,  1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 2, 3));
	assertTrue(contains(result, 0, 3, 4));
}

public void testTriangulate7()
{
	// regular right vertex
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 0.0, -2.0);
	data[2] = new Vector2( 1.0, -1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 0, 1, 2));
}

public void testTriangulate8()
{
	// regular right vertex whose neighbour has a merge vertex as helper
	Vector2[] data = new Vector2[5];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  1.0);
	data[2] = new Vector2( 0.0, -2.0);
	data[3] = new Vector2( 1.5, -1.0);
	data[4] = new Vector2( 1.0,  1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 2, 3));
	assertTrue(contains(result, 0, 3, 4));
}


public void testTriangulate9()
{
	// wicked testcase (tests LEFT_RIGHT_COMPARATOR)
	Vector2[] data = new Vector2[8];
	data[0] = new Vector2(199.0, -200.0);
	data[1] = new Vector2(280.0, -230.0);
	data[2] = new Vector2(280.0, -190.0);
	data[3] = new Vector2(350.0, -200.0);
	data[4] = new Vector2(350.0, -100.0);
	data[5] = new Vector2(240.0, -100.0);
	data[6] = new Vector2(240.0, -200.0);
	data[7] = new Vector2(220.0, -150.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 1, 6));
	assertTrue(contains(result, 1, 2, 6));
	assertTrue(contains(result, 2, 3, 4));
	assertTrue(contains(result, 2, 4, 5));
	assertTrue(contains(result, 2, 5, 6));
	assertTrue(contains(result, 0, 6, 7));
}

public void testTriangulate10()
{
	// another wicked testcase (tests LEFT_RIGHT_COMPARATOR)
	Vector2[] data = new Vector2[7];
	data[0] = new Vector2(265.0, -243.0);
	data[1] = new Vector2(577.0, -389.0);
	data[2] = new Vector2(663.0, -65.0);
	data[3] = new Vector2(543.0, -147.0);
	data[4] = new Vector2(517.0, -271.0);
	data[5] = new Vector2(550.0, -199.0);
	data[6] = new Vector2(505.0, -315.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(5, result.size());
	assertTrue(contains(result, 0, 1, 6));
	assertTrue(contains(result, 1, 2, 6));
	assertTrue(contains(result, 2, 5, 6));
	assertTrue(contains(result, 2, 3, 5));
	assertTrue(contains(result, 3, 4, 5));
}


public void testTriangulateMonotone1()
{
	// monotone polygon
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0, -1.0);
	data[2] = new Vector2( 0.0, -2.0);
	data[3] = new Vector2( 1.0, -1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 1, 3));
	assertTrue(contains(result, 1, 2, 3));
}

public void testTriangulateMonotone2()
{
	// monotone polygon with two regular left and one regular right just below
	Vector2[] data = new Vector2[5];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0, -1.0);
	data[2] = new Vector2(-3.0, -2.0);
	data[3] = new Vector2( 0.0, -4.0);
	data[4] = new Vector2( 1.0, -3.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 1, 4));
	assertTrue(contains(result, 1, 2, 4));
	assertTrue(contains(result, 2, 3, 4));
}

public void testTriangulateMonotone3()
{
	// monotone polygon with a corner to the right (cut off)
	Vector2[] data = new Vector2[5];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  3.0);
	data[2] = new Vector2( 0.0, -3.0);
	data[3] = new Vector2( 1.0, -2.0);
	data[4] = new Vector2( 2.0, -1.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 2, 3));
	assertTrue(contains(result, 0, 3, 4));
}


public void testTriangulateDoubleVertex1()
{
	// a triangle cut out from a triangle (duplicate vertices along a "seam" edge)
	Vector2[] data = new Vector2[8];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-3.0, -3.0);
	data[2] = new Vector2( 3.0, -3.0);
	data[3] = new Vector2( 2.0, -2.0);
	data[4] = new Vector2(-2.0, -2.0);
	data[5] = new Vector2( 0.0, -1.0);
	data[6] = data[3];
	data[7] = data[2];
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 1, 4));
	assertTrue(contains(result, 0, 4, 5));
	assertTrue(contains(result, 0, 5, 6));
	assertTrue(contains(result, 0, 6, 7));
	assertTrue(contains(result, 1, 2, 3));
	assertTrue(contains(result, 1, 3, 4));
}

public void testTriangulateDoubleVertex2()
{
	// a triangle cut out from a triangle (duplicate vertices along a "seam" edge)
	Vector2[] data = new Vector2[8];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-3.0, -3.0);
	data[2] = new Vector2( 3.0, -3.0);
	data[3] = data[0];
	data[4] = new Vector2( 0.0, -1.0);
	data[5] = new Vector2( 2.0, -2.0);
	data[6] = new Vector2(-2.0, -2.0);
	data[7] = data[4];
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 6, 7));
	assertTrue(contains(result, 0, 1, 6));
	assertTrue(contains(result, 1, 5, 6));
	assertTrue(contains(result, 1, 2, 5));
	assertTrue(contains(result, 3, 4, 5));
	assertTrue(contains(result, 3, 5, 2));
	
/*	assertTrue(contains(result, 0, 6));
	assertTrue(contains(result, 1, 6));
	assertTrue(contains(result, 2, 5));
	assertTrue(contains(result, 3, 5));
	assertTrue(contains(result, 1, 5));*/
}


public void testReverse1()
{
	// triangle
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 0.0, 10.0);
	data[2] = new Vector2(10.0,  0.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 0, 2, 1));
}

public void testReverse2()
{
	// a triangle cut out from a triangle (duplicate vertices along a "seam" edge)
	Vector2[] data = new Vector2[8];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 0.0, -1.0);
	data[2] = new Vector2(-2.0, -2.0);
	data[3] = new Vector2( 2.0, -2.0);
	data[4] = data[1];
	data[5] = data[0];
	data[6] = new Vector2( 3.0, -3.0);
	data[7] = new Vector2(-3.0, -3.0);
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 2, 1));
	assertTrue(contains(result, 3, 2, 7));
	assertTrue(contains(result, 2, 0, 7));
	assertTrue(contains(result, 5, 4, 3));
	assertTrue(contains(result, 3, 7, 6));
	assertTrue(contains(result, 5, 3, 6));
}

}
