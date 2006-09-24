// arch-tag: 3a7f4275-e6ee-49bf-bd77-f6b135c2ef1f
package de.yvert.algorithms.triangulation;

import java.util.ArrayList;

import junit.framework.TestCase;
import de.yvert.geometry.Vector2;

public class HorrorTriangulationTest extends TestCase
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

/*private boolean contains(ArrayList<Triple> result, int a, int b, int c)
{
	for (Triple p : result)
		if (((p.a == a) && (p.b == b) && (p.c == c)) ||
				((p.a == b) && (p.b == c) && (p.c == a)) ||
				((p.a == c) && (p.b == a) && (p.c == b)))
			return true;
	return false;
}*/

public void testTriangulate1()
{
	Vector2[] data = new Vector2[4];
	data[0] = new Vector2( -24.55202, 6.578696 );
	data[1] = new Vector2(  -3.62958, 0.972544 );
	data[2] = new Vector2(  -3.62185, 0.970474 );
	data[3] = new Vector2( -24.54430, 6.576626 );
	
	ArrayList<Triple> result = collectTriangles(data);
	assertEquals(2, result.size());
//	assertTrue(contains(result, 0, 1, 2));
}

}
