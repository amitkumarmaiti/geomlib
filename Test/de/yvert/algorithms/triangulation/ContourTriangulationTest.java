package de.yvert.algorithms.triangulation;

import java.util.ArrayList;

import de.yvert.algorithms.triangulation.PolygonTriangulationTrianglesTest.Triple;
import de.yvert.geometry.Vector2;
import junit.framework.TestCase;

public class ContourTriangulationTest extends TestCase
{

private ArrayList<Triple> collectTriangles(Contour... data)
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

public void testSimpleContour()
{
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2(0, 0);
	data[1] = new Vector2(1, 0);
	data[2] = new Vector2(1, 1);
	data[3] = new Vector2(0, 1);
	Contour contour = new Contour(data);
	ArrayList<Triple> result = collectTriangles(contour);
	assertEquals(2, result.size());
	assertTrue(contains(result, 0, 1, 2));
	assertTrue(contains(result, 0, 2, 3));
}

public void testComplexContour1()
{
	Vector2[] data = new Vector2[3];
	data[0] = new Vector2(0, 0);
	data[1] = new Vector2(4, 0);
	data[2] = new Vector2(2, 3);
	Contour contour1 = new Contour(data);
	
	data = new Vector2[3];
	data[0] = new Vector2(1, 0.5);
	data[1] = new Vector2(2, 2);
	data[2] = new Vector2(3, 0.5);
	Contour contour2 = new Contour(data);
	
	ArrayList<Triple> result = collectTriangles(contour1, contour2);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 5, 3));
	assertTrue(contains(result, 0, 1, 5));
	assertTrue(contains(result, 1, 2, 5));
	assertTrue(contains(result, 2, 4, 5));
	assertTrue(contains(result, 0, 3, 2));
	assertTrue(contains(result, 3, 4, 2));
}

public void testComplexContour2()
{
	Vector2[] data = new Vector2[8];
	data[0] = new Vector2( -6.0, 1493.0 );
	data[1] = new Vector2( 1257.0, 1493.0 );
	data[2] = new Vector2( 1257.0, 1323.0 );
	data[3] = new Vector2( 727.0, 1323.0 );
	data[4] = new Vector2( 727.0, 0.0 );
	data[5] = new Vector2( 524.0, 0.0 );
	data[6] = new Vector2( 524.0, 1323.0 );
	data[7] = new Vector2( -6.0, 1323.0 );
	Contour contour = new Contour(data).reverse();
	ArrayList<Triple> result = collectTriangles(contour);
	assertEquals(6, result.size());
	assertTrue(contains(result, 0, 6, 7));
	assertTrue(contains(result, 1, 6, 0));
}

}
