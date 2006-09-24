// arch-tag: 2dc3d5c6-7388-46bf-90d7-fee555f32f46
package de.yvert.algorithms;

import de.yvert.geometry.Vector2;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Calculates the convex hull of a set of 2D points.
 * <p>
 * The algorithm takes <code>O(n log n)</code> for the initial sorting step
 * and <code>O(n)</code> to determine the convex hull.
 * <p>
 * In the face of rounding errors, it cannot be guaranteed that the result
 * is actually the convex hull of all points. However, any points lying
 * outside the hull are lying close to it. Further, it is guaranteed that
 * the result is a simple, convex polygon.
 * <p>
 * The implementation does not check whether two input points lie close to
 * each other.
 * <p>
 * The algorithm is taken from:
 * <pre>
 * Computational Geometry: Algorithms and Applications
 * Second Edition
 * M. de Berg and M. van Kreveld and M. Overmars and O. Schwarzkopf
 * Springer-Verlag
 * ISBN: 3-540-65620-0
 * </pre>
 * 
 * @author Ulf Ochsenfahrt
 */
public class ConvexHull2D
{

/**
 * Comparator used to sort the vertices in left-right order. Vertices with
 * identical x coordinate are sorted bottom-to-top.
 */
public static final Comparator<Vector2> LEFT_RIGHT = new Comparator<Vector2>()
	{
		public int compare(Vector2 p1, Vector2 p2)
		{
			if (p1.getX() < p2.getX()) return -1;
			if (p1.getX() > p2.getX()) return +1;
			if (p1.getY() < p2.getY()) return -1;
			if (p1.getY() > p2.getY()) return +1;
			return 0;
		}
	};

/**
 * Returns true if the three points form a real right turn (i.e. they do not
 * form a left turn, nor are they collinear).
 * <p>
 * May return an incorrect result if the points are very close to each other
 * or if one point lies very close to the line described by the other two.
 */
public static boolean isRightTurn(Vector2 a, Vector2 b, Vector2 c)
{
	double det = b.getX()*c.getY()+a.getX()*b.getY()+a.getY()*c.getX();
	det -= b.getY()*c.getX()+a.getX()*c.getY()+a.getY()*b.getX();
	return det < 0;
}

public static Vector2[] calculate(Vector2[] points)
{
	Vector2[] temp = new Vector2[points.length];
	System.arraycopy(points, 0, temp, 0, points.length);
	Arrays.sort(temp, LEFT_RIGHT);
	if (points.length < 3) return temp;
	
	Vector2[] upperHull = new Vector2[temp.length];
	int ucount = 0;
	upperHull[ucount++] = temp[0];
	upperHull[ucount++] = temp[1];
	for (int i = 2; i < temp.length; i++)
	{
		while ((ucount >= 2) && !isRightTurn(upperHull[ucount-2], upperHull[ucount-1], temp[i]))
			ucount--;
		upperHull[ucount++] = temp[i];
	}
	
	Vector2[] lowerHull = new Vector2[temp.length];
	int lcount = 0;
	lowerHull[lcount++] = temp[temp.length-1];
	lowerHull[lcount++] = temp[temp.length-2];
	for (int i = temp.length-3; i >= 0; i--)
	{
		while ((lcount >= 2) && !isRightTurn(lowerHull[lcount-2], lowerHull[lcount-1], temp[i]))
			lcount--;
		lowerHull[lcount++] = temp[i];
	}
	
	for (int i = 1; i < lcount-1; i++)
		upperHull[ucount++] = lowerHull[i];
	
	Vector2[] result = new Vector2[ucount];
	for (int i = 0; i < ucount; i++)
		result[i] = upperHull[i];
	
	return result;
}

public static void main(String[] args)
{
	Vector2[] points = new Vector2[5];
	points[0] = new Vector2(-1, 0);
	points[1] = new Vector2( 0, 0);
	points[2] = new Vector2( 0, 1.6);
	points[3] = new Vector2( 1, 3);
	points[4] = new Vector2( 2, 1);
	Vector2[] hull = calculate(points);
	
	for (int i = 0; i < hull.length; i++)
		System.out.println(hull[i]);
}

}
