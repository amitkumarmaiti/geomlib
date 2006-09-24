// arch-tag: e2c6af87-5545-4980-8a03-5e429a507c7b
package de.yvert.algorithms.triangulation;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.yvert.geometry.Vector2;

public class PolygonTriangulationEdgesTest extends TestCase
{

static class Pair
{ int a, b; Pair(int a, int b) { this.a = a; this.b = b; } }

private ArrayList<Pair> collectEdges(Vector2[] data)
{
	final ArrayList<Pair> result = new ArrayList<Pair>();
	new PolygonTriangulator2D().calculate(data, new TriangulationAdapter()
		{
			@Override
			public void addEdge(int a, int b)
			{
//				System.out.println(a+" -> "+b);
				result.add(new Pair(a, b));
			}
		});
	return result;
}

private boolean contains(ArrayList<Pair> result, int a, int b)
{
	for (Pair p : result)
		if (((p.a == a) && (p.b == b)) || ((p.a == b) && (p.b == a))) return true;
	return false;
}

public void testTriangulate1()
{
	// triangle
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(10.0,  0.0);
	data[2] = new Vector2( 0.0, 10.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(0, result.size());
}

public void testTriangulate2()
{
	// end vertex that has a merge vertex as helper
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0,  1.0);
	data[2] = new Vector2( 0.0, -2.0);
	data[3] = new Vector2( 1.0,  1.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 0, 2));
}

public void testTriangulate3()
{
	// split vertex
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 1.0, -1.0);
	data[2] = new Vector2( 0.0,  2.0);
	data[3] = new Vector2(-1.0, -1.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 0, 2));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 2));
	assertTrue(contains(result, 0, 4));
	assertTrue(contains(result, 2, 4));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(3, result.size());
	assertTrue(contains(result, 0, 2));
	assertTrue(contains(result, 0, 4));
	assertTrue(contains(result, 2, 4));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 2));
	assertTrue(contains(result, 0, 3));
}

public void testTriangulate7()
{
	// regular right vertex
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 0.0, -2.0);
	data[2] = new Vector2( 1.0, -1.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(0, result.size());
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 2));
	assertTrue(contains(result, 0, 3));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(5, result.size());
	assertTrue(contains(result, 2, 5));
	assertTrue(contains(result, 1, 6));
	assertTrue(contains(result, 2, 4));
	assertTrue(contains(result, 0, 6));
	assertTrue(contains(result, 2, 6));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(4, result.size());
	assertTrue(contains(result, 3, 5));
	assertTrue(contains(result, 1, 6));
	assertTrue(contains(result, 2, 5));
	assertTrue(contains(result, 2, 6));
}


public void testTriangulateMonotone1()
{
	// monotone polygon
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2(-1.0, -1.0);
	data[2] = new Vector2( 0.0, -2.0);
	data[3] = new Vector2( 1.0, -1.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(1, result.size());
	assertTrue(contains(result, 1, 3));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 1, 4));
	assertTrue(contains(result, 2, 4));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 3));
	assertTrue(contains(result, 0, 2));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(5, result.size());
	assertTrue(contains(result, 0, 4));
	assertTrue(contains(result, 0, 5));
	assertTrue(contains(result, 0, 6));
	assertTrue(contains(result, 1, 3));
	assertTrue(contains(result, 1, 4));
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
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(5, result.size());
	assertTrue(contains(result, 0, 6));
	assertTrue(contains(result, 1, 6));
	assertTrue(contains(result, 2, 5));
	assertTrue(contains(result, 3, 5));
	assertTrue(contains(result, 1, 5));
}


public void testReverse1()
{
	// triangle
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2( 0.0,  0.0);
	data[1] = new Vector2( 0.0, 10.0);
	data[2] = new Vector2(10.0,  0.0);
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(0, result.size());
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
	
	ArrayList<Pair> result = collectEdges(data);
	assertEquals(5, result.size());
	assertTrue(contains(result, 0, 2));
	assertTrue(contains(result, 2, 7));
	assertTrue(contains(result, 3, 7));
	assertTrue(contains(result, 3, 6));
	assertTrue(contains(result, 3, 5));
}

}
