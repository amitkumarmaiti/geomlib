package de.yvert.algorithms;

import junit.framework.TestCase;
import de.yvert.algorithms.PolygonClipper3D.XYPlane;
import de.yvert.algorithms.PolygonClipper3D.YZPlane;
import de.yvert.geometry.Vector3;

public class PolygonClipper3DTest extends TestCase
{

private void assertSimilar(Vector3 a, Vector3 b, double delta)
{
	assertEquals(a.getX(), b.getX(), delta);
	assertEquals(a.getY(), b.getY(), delta);
	assertEquals(a.getZ(), b.getZ(), delta);
}

public void testSimple()
{
	Vector3 a = new Vector3(0, 0, 0);
	Vector3 b = new Vector3(0, 0, 5);
	Vector3 c = new Vector3(1, 0, 0);
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	Vector3[] points = clipper.getPoints();
	assertNotNull(points);
	assertEquals(3, points.length);
	assertSimilar(a, points[0], 0);
	assertSimilar(b, points[1], 0);
	assertSimilar(c, points[2], 0);
}

public void testXYnoclip()
{
	Vector3 a = new Vector3(0, 0, 0);
	Vector3 b = new Vector3(0, 0, 5);
	Vector3 c = new Vector3(1, 0, 0);
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	clipper.clip(new XYPlane(false, 5));
	Vector3[] points = clipper.getPoints();
	assertNotNull(points);
	assertEquals(3, points.length);
	assertSimilar(c, points[0], 0);
	assertSimilar(a, points[1], 0);
	assertSimilar(b, points[2], 0);
}

public void testXYclip()
{
	Vector3 a = new Vector3(0, 0, 0);
	Vector3 b = new Vector3(0, 0, 5);
	Vector3 c = new Vector3(1, 0, 0);
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	clipper.clip(new XYPlane(false, 4));
	Vector3[] points = clipper.getPoints();
	assertNotNull(points);
	assertEquals(4, points.length);
	assertSimilar(c, points[0], 0);
	assertSimilar(a, points[1], 0);
	assertSimilar(new Vector3(0,0,4), points[2], 0);
	assertSimilar(new Vector3(0.2,0,4), points[3], 1e-7d);
}

public void testYZnoclip()
{
	Vector3 a = new Vector3(0, 0, 0);
	Vector3 b = new Vector3(0, 0, 5);
	Vector3 c = new Vector3(1, 0, 0);
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	clipper.clip(new YZPlane(false, 4));
	Vector3[] points = clipper.getPoints();
	assertNotNull(points);
	assertEquals(3, points.length);
	assertSimilar(c, points[0], 0);
	assertSimilar(a, points[1], 0);
	assertSimilar(b, points[2], 0);
}

public void testYZclip()
{
	Vector3 a = new Vector3(0, 0, 0);
	Vector3 b = new Vector3(0, 0, 5);
	Vector3 c = new Vector3(1, 0, 0);
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	clipper.clip(new YZPlane(false, 0.5));
	Vector3[] points = clipper.getPoints();
	assertNotNull(points);
	assertEquals(4, points.length);
	assertSimilar(new Vector3(0.5, 0, 0), points[0], 0);
	assertSimilar(a, points[1], 0);
	assertSimilar(b, points[2], 0);
	assertSimilar(new Vector3(0.5, 0, 2.5), points[3], 0);
}

}
