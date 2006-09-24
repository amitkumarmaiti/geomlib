package de.yvert.algorithms.triangulation;

import de.yvert.geometry.Vector2;

public class Contour
{

private final Vector2[] points;

public Contour(Vector2[] points)
{ this.points = points.clone(); }

public Contour reverse()
{
	for (int i = 0; i < points.length/2; i++)
	{
		Vector2 temp = points[i];
		points[i] = points[points.length-1-i];
		points[points.length-1-i] = temp;
	}
	return this;
}

public Vector2[] getPoints()
{ return points; }

public int length()
{ return points.length; }

public Vector2 get(int i)
{ return points[i]; }

}
