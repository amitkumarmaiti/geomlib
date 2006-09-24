// arch-tag: 94ec8bec-924c-4e0e-a618-65e3a978bc53
package de.yvert.algorithms.triangulation;

import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.LEFT_RIGHT_COMPARATOR;
import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.TOP_DOWN_COMPARATOR;
import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.above;
import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.addEdge;
import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.isClockwise;
import static de.yvert.algorithms.triangulation.PolygonTriangulator2D.label;
import junit.framework.TestCase;
import de.yvert.geometry.Vector2;

public class BasicPolygonTest extends TestCase
{

public void testAbove1()
{
	Vertex a = new Vertex(0, new Vector2(1, 1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	assertTrue(above(a, b));
}

public void testAbove2()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(1, 1));
	assertFalse(above(a, b));
}

public void testAbove3()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(1, 0));
	assertTrue(above(a, b));
}

public void testAbove4()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(1, 0));
	assertFalse(above(b, a));
}

public void testAbove5()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	assertTrue(above(a, b));
}

public void testAbove6()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(0, new Vector2(0, 0));
	try
	{
		above(a, b);
		fail();
	}
	catch (RuntimeException e)
	{/*Expected Exception*/}
}


public void testClockwise1()
{
	Vector2 a = new Vector2(0, 0);
	Vector2 b = new Vector2(1, 1);
	Vector2 c = new Vector2(3, 2);
	assertEquals(1, isClockwise(a, b, c));
}

public void testClockwise2()
{
	Vector2 a = new Vector2(0, 0);
	Vector2 b = new Vector2(1, 1);
	Vector2 c = new Vector2(1, 2);
	assertEquals(-1, isClockwise(a, b, c));
}

public void testClockwise3()
{
	Vector2 a = new Vector2(0, 0);
	Vector2 b = new Vector2(1, 1);
	Vector2 c = new Vector2(2, 2);
	assertEquals(0, isClockwise(a, b, c));
}

public void testClockwise4()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(1, 1));
	Vertex c = new Vertex(2, new Vector2(2, 2));
	assertEquals(0, isClockwise(a, b, c));
}

public void testClockwise5()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2( 0, -1));
	Vertex c = new Vertex(0, new Vector2( 1,  0));
	assertEquals(-1, isClockwise(a, b, c));
}


public void testTopDown1()
{
	Vertex a = new Vertex(0, new Vector2(0, 0));
	Vertex b = new Vertex(1, new Vector2(1, 1));
	assertEquals(1, TOP_DOWN_COMPARATOR.compare(a, b));
}

public void testTopDown2()
{
	Vertex a = new Vertex(0, new Vector2(1, 1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	assertEquals(-1, TOP_DOWN_COMPARATOR.compare(a, b));
}

public void testTopDown3()
{
	Vertex a = new Vertex(0, new Vector2(1, 1));
	assertEquals(0, TOP_DOWN_COMPARATOR.compare(a, a));
}


public void testLeftRight0()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2(-1, -1));
	Edge e1 = new Edge(a, b);
	assertEquals(0, LEFT_RIGHT_COMPARATOR.compare(e1, e1));
}

public void testLeftRight1()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2( 0, -1));
	Vertex c = new Vertex(0, new Vector2( 1,  0));
	Vertex d = new Vertex(0, new Vector2( 1, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(-1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight2()
{
	Vertex a = new Vertex(0, new Vector2( 1,  0));
	Vertex b = new Vertex(0, new Vector2( 1, -1));
	Vertex c = new Vertex(0, new Vector2( 0,  0));
	Vertex d = new Vertex(0, new Vector2( 0, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight3()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2(-1, -1));
	Vertex c = new Vertex(0, new Vector2(-2, -1));
	Vertex d = new Vertex(0, new Vector2(-1, -2));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight4()
{
	Vertex a = new Vertex(0, new Vector2(-2, -1));
	Vertex b = new Vertex(0, new Vector2(-1, -2));
	Vertex c = new Vertex(0, new Vector2( 0,  0));
	Vertex d = new Vertex(0, new Vector2(-1, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(-1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight5()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2( 1, -1));
	Vertex c = new Vertex(0, new Vector2( 2,  0));
	Vertex d = new Vertex(0, new Vector2( 1, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(-1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight6()
{
	Vertex a = new Vertex(0, new Vector2( 2,  0));
	Vertex b = new Vertex(0, new Vector2( 1, -1));
	Vertex c = new Vertex(0, new Vector2( 0,  0));
	Vertex d = new Vertex(0, new Vector2( 1, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}

public void testLeftRight7()
{
	Vertex a = new Vertex(0, new Vector2( 0,  0));
	Vertex b = new Vertex(0, new Vector2(-1, -1));
	Vertex c = new Vertex(0, new Vector2( 0,  0));
	Vertex d = new Vertex(0, new Vector2( 1, -1));
	Edge e1 = new Edge(a, b), e2 = new Edge(c, d);
	assertEquals(-1, LEFT_RIGHT_COMPARATOR.compare(e1, e2));
}


public void testLabel1()
{
	Vertex a = new Vertex(0, new Vector2(1, 1));
	Vertex b = new Vertex(1, new Vector2(0, 2));
	Vertex c = new Vertex(2, new Vector2(0, 0));
	assertEquals(VertexState.START, label(a, b, c));
}

public void testLabel2()
{
	Vertex a = new Vertex(0, new Vector2(-1, 1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	Vertex c = new Vertex(2, new Vector2(1, 1));
	assertEquals(VertexState.END, label(a, b, c));
}

public void testLabel3()
{
	Vertex a = new Vertex(0, new Vector2(2, 2));
	Vertex b = new Vertex(1, new Vector2(1, 1));
	Vertex c = new Vertex(2, new Vector2(0, 2));
	assertEquals(VertexState.MERGE, label(a, b, c));
}

public void testLabel4()
{
	Vertex a = new Vertex(0, new Vector2(-1, -1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	Vertex c = new Vertex(2, new Vector2(1, -1));
	assertEquals(VertexState.SPLIT, label(a, b, c));
}

public void testLabel5()
{
	Vertex a = new Vertex(0, new Vector2(0, 1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	Vertex c = new Vertex(2, new Vector2(0, -1));
	assertEquals(VertexState.REGULAR_LEFT, label(a, b, c));
}

public void testLabel6()
{
	Vertex a = new Vertex(0, new Vector2(0, -1));
	Vertex b = new Vertex(1, new Vector2(0, 0));
	Vertex c = new Vertex(2, new Vector2(0, 1));
	assertEquals(VertexState.REGULAR_RIGHT, label(a, b, c));
}


public void testAddEdge()
{
	HalfEdge e1 = new HalfEdge(), e2 = new HalfEdge(), e3 = new HalfEdge(), e4 = new HalfEdge();
	Vertex v1 = new Vertex(), v2 = new Vertex();
	e1.next = e2;
	e2.prev = e1;
	e2.origin = v1;
	
	e3.next = e4;
	e4.prev = e3;
	e4.origin = v2;
	
	addEdge(e2, e4, null);
	
	HalfEdge f1 = e1.next;
	HalfEdge f2 = e3.next;
	
	assertSame(f1.origin, v1);
	assertSame(f1.twin, f2);
	assertSame(f1.next, e4);
	assertSame(f1.prev, e1);
	
	assertSame(f2.origin, v2);
	assertSame(f2.twin, f1);
	assertSame(f2.next, e2);
	assertSame(f2.prev, e3);
	
	assertSame(e2.prev, f2);
	assertSame(e4.prev, f1);
}

}
