// arch-tag: 2a66383f-3bda-4406-bc15-a1f54a5b9db7
package de.yvert.geometry;

import de.yvert.cr.profiles.IntersectionResult;

public class Sphere extends SceneItem
{

private static final double EPSILON = 1e-4f;

public Vector3 point;
public double radius;

public Sphere(Sphere other)
{ set(other); }

public Sphere(Vector3 point, double radius)
{
	this.point = new Vector3(point);
	this.radius = radius;
}


public double getRadius()
{ return radius; }

public Vector3 getCenter()
{ return point; }

public final Sphere set(Sphere other)
{
	point = new Vector3(other.point);
	radius = other.radius;
	return this;
}

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{
	geonormal.set(result.hitpoint).subAndSet(point);
	geonormal.normalizeAndSet();
}

@Override
public double getMinX()
{ return point.getX()-radius; }

@Override
public double getMaxX()
{ return point.getX()+radius; }

@Override
public double getMinY()
{ return point.getY()-radius; }

@Override
public double getMaxY()
{ return point.getY()+radius; }

@Override
public double getMinZ()
{ return point.getZ()-radius; }

@Override
public double getMaxZ()
{ return point.getZ()+radius; }

@Override
public BoundingBox getBoundingBox()
{ return new BoundingBox(this); }

@Override
public BoundingSphere getBoundingSphere()
{ return new BoundingSphere(point, radius); }

@Override
public double distance(Ray ray)
{
	double a = ray.v.getSquaredLength();
	double b = ray.v.multiply(ray.p) - ray.v.multiply(point);
	double c = ray.p.squaredDistance(point) - radius*radius;
	double belowrt = b*b-a*c;
	if (belowrt < 0) return -1;
	double rt = Math.sqrt(belowrt);
	double first = (-b-rt)/a;
	if (first < 0)
	{
		double second = (-b+rt)/a;
		if (second < EPSILON) return -1;
		return second;
	}
	return first;
}

private void triangulate(Triangulation tri, int comega, int ctheta)
{
	Vector3[][] data = new Vector3[comega+1][ctheta+1];
	for (int i = 0; i <= comega; i++)
		for (int j = 0; j <= ctheta; j++)
		{
			double omega = (i*2*Math.PI)/comega;
			double theta = (j*Math.PI)/ctheta;
			
			double x = radius*Math.sin(omega)*Math.sin(theta);
			double y = radius*Math.cos(theta);
			double z = radius*Math.cos(omega)*Math.sin(theta);
			data[i][j] = new Vector3(x, y, z).addAndSet(point);
		}
	
	for (int i = 0; i < comega; i++)
		for (int j = 0; j < ctheta; j++)
		{
			Vector3 a = data[i][j];
			Vector3 b = data[i][j+1];
			Vector3 c = data[i+1][j+1];
			Vector3 d = data[i+1][j];
			
			Triangle t0 = new Triangle(a, b, c);
			t0.setMaterial(material);
			
			Triangle t1 = new Triangle(c, d, a);
			t1.setMaterial(material);
			
			tri.add(t0).add(t1);
		}
}

@Override
public void triangulate(Triangulation tri)
{
	triangulate(tri, 10, 5);
}

@Override
public String toString()
{ return "Sphere: "+point+", "+radius; }

}
