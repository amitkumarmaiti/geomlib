package de.yvert.algorithms;

import de.yvert.geometry.BoundingBox;
import de.yvert.geometry.Vector3;

/**
 * Clips a polygon against any number of planes.
 * This is an implementation of the Sutherland-Hodgeman clipping algorithm.
 */
public class PolygonClipper3D
{

	public static abstract class Plane
	{
		/**
		 * A plane subdivides 3-space into two parts, the "in" part and the "out" part.
		 * Everything in the "out" part will be clipped.
		 */
		Plane()
		{/*Only subclassable within this package!*/}
		public abstract boolean in(Vector3 point);
		public abstract Vector3 clip(Vector3 a, Vector3 b);
	}
	
/*	public static class AnyPlane extends Plane
	{
		Vector3 normal;
		Vector3 point;
		public AnyPlane(Vector3 normal, Vector3 point)
		{
			this.normal = new Vector3(normal);
			this.point = new Vector3(point);
		}
		public boolean in(Vector3 point)
		{
			return false;
		}
	}*/
	
	public static class XYPlane extends Plane
	{
		private final boolean left;
		private final double where;
		public XYPlane(boolean left, double where)
		{
			this.left = left;
			this.where = where;
		}
		@Override
		public boolean in(Vector3 point)
		{
			if (left)
				return point.getZ() >= where;
			else
				return point.getZ() <= where;
		}
		@Override
		public Vector3 clip(Vector3 a, Vector3 b)
		{
			double t = (where-a.getZ())/(b.getZ()-a.getZ());
			double x = a.getX()+t*(b.getX()-a.getX());
			double y = a.getY()+t*(b.getY()-a.getY());
			double z = where;
			return new Vector3(x, y, z);
		}
	}
	
	public static class XZPlane extends Plane
	{
		private final boolean left;
		private final double where;
		public XZPlane(boolean left, double where)
		{
			this.left = left;
			this.where = where;
		}
		@Override
		public boolean in(Vector3 point)
		{
			if (left)
				return point.getY() >= where;
			else
				return point.getY() <= where;
		}
		@Override
		public Vector3 clip(Vector3 a, Vector3 b)
		{
			double t = (where-a.getY())/(b.getY()-a.getY());
			double x = a.getX()+t*(b.getX()-a.getX());
			double y = where; //a.getY()+t*(b.getY()-a.getY());
			double z = a.getZ()+t*(b.getZ()-a.getZ());
			return new Vector3(x, y, z);
		}
	}
	
	public static class YZPlane extends Plane
	{
		private final boolean left;
		private final double where;
		public YZPlane(boolean left, double where)
		{
			this.left = left;
			this.where = where;
		}
		@Override
		public boolean in(Vector3 point)
		{
			if (left)
				return point.getX() >= where;
			else
				return point.getX() <= where;
		}
		@Override
		public Vector3 clip(Vector3 a, Vector3 b)
		{
			double t = (where-a.getX())/(b.getX()-a.getX());
			double x = where; //a.getX()+t*(b.getX()-a.getX());
			double y = a.getY()+t*(b.getY()-a.getY());
			double z = a.getZ()+t*(b.getZ()-a.getZ());
			return new Vector3(x, y, z);
		}
	}

private int count;
private Vector3[] points;
private Vector3[] other;

public PolygonClipper3D(Vector3... points)
{
	this.points = new Vector3[2*points.length];
	for (int i = 0; i < points.length; i++)
		this.points[i] = new Vector3(points[i]);
	this.count = points.length;
}

public Vector3[] getPoints()
{
	Vector3[] result = new Vector3[count];
	for (int i = 0; i < count; i++)
		result[i] = new Vector3(points[i]);
	return result;
}

public void clip(Plane plane)
{
	if (count == 0) return;
	int newcount = 0;
	Vector3[] newpoints = other;
	if ((newpoints == null) || (newpoints.length < 2*points.length))
		newpoints = new Vector3[2*points.length];
	int index = count-1;
	boolean isIn = plane.in(points[count-1]);
	for (int i = 0; i < count; i++)
	{
		boolean nextIn = plane.in(points[i]);
		if (isIn)
		{
			newpoints[newcount++] = points[index];
			if (!nextIn)
				newpoints[newcount++] = plane.clip(points[index], points[i]);
		}
		else if (nextIn)
			newpoints[newcount++] = plane.clip(points[index], points[i]);
		index = i;
		isIn = nextIn;
	}
	
	other = points;
	points = newpoints;
	count = newcount;
}

public void clip(BoundingBox box)
{
	clip(new YZPlane(true, box.getMinX()));
	clip(new YZPlane(false, box.getMaxX()));
	clip(new XZPlane(true, box.getMinY()));
	clip(new XZPlane(false, box.getMaxY()));
	clip(new XYPlane(true, box.getMinZ()));
	clip(new XYPlane(false, box.getMaxZ()));
}

}
