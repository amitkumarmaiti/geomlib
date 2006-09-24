// arch-tag: 4980ac5e-2cd8-43d0-b4f7-38c0efae49bf
package de.yvert.geometry;

import de.yvert.cr.profiles.IntersectionResult;

public class Ring extends SceneItem
{

private static final double EPSILON = 1e-4f;

public Vector3 point;
public Vector3 normal;
public double innerradius;
public double outerradius;

public Ring(Ring other)
{ set(other); }

public Ring(Vector3 point, Vector3 normal, double innerradius, double outerradius)
{
	this.point = new Vector3(point);
	this.normal = new Vector3(normal).normalizeAndSet();
	this.innerradius = innerradius;
	this.outerradius = outerradius;
}


public Vector3 getCenter()
{ return point; }

public Vector3 getNormal()
{ return normal; }

public double getInnerRadius()
{ return innerradius; }

public double getOuterRadius()
{ return outerradius; }

public final Ring set(Ring other)
{
	point = new Vector3(other.point);
	normal = new Vector3(other.normal);
	innerradius = other.innerradius;
	outerradius = other.outerradius;
	return this;
}

//public double surfaceArea()
//{ return 2*Math.PI*(outerradius*outerradius-innerradius*innerradius); }

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{ geonormal.set(normal); }

@Override
public double getMinX()
{ return point.getX()-outerradius; }

@Override
public double getMaxX()
{ return point.getX()+outerradius; }

@Override
public double getMinY()
{ return point.getY()-outerradius; }

@Override
public double getMaxY()
{ return point.getY()+outerradius; }

@Override
public double getMinZ()
{ return point.getZ()-outerradius; }

@Override
public double getMaxZ()
{ return point.getZ()+outerradius; }

@Override
public BoundingBox getBoundingBox()
{
	return new BoundingBox(new Vector3(getMinX(), getMinY(), getMinZ()), 
			new Vector3(getMaxX(), getMaxY(), getMaxZ()));
}

@Override
public BoundingSphere getBoundingSphere()
{
	return new BoundingSphere(point, outerradius);
}

@Override
public double distance(Ray ray)
{
	double d = normal.multiply(ray.v);
	if ((d < EPSILON) && (d > -EPSILON)) return -1;
	double e = normal.multiply(point)-normal.multiply(ray.p);
	if ((e < 0) ^ (d < 0)) return -1;
	double t = e/d;
	if (t < 0) return -1;
	Vector3 hitp = ray.v.scale(t).addAndSet(ray.p);
	double dist = hitp.distance(point);
	if ((dist < innerradius) || (dist > outerradius)) return -1;
	return dist;
}

private void triangulate(Triangulation tri, int comega)
{
	Vector3[] indata = new Vector3[comega+1];
	Vector3[] outdata = new Vector3[comega+1];
	
	Vector3 u = normal.findAnyOrthogonal();
	Vector3 v = normal.cross(u);
	
	for (int i = 0; i <= comega; i++)
	{
		double omega = (i*2*Math.PI)/comega;
		
		Vector3 w = u.scale(Math.sin(omega)).addAndSet(v, Math.cos(omega));
		indata[i] = new Vector3(w).scaleAndSet(innerradius).addAndSet(point);
		outdata[i] = new Vector3(w).scaleAndSet(outerradius).addAndSet(point);
	}
	
	for (int i = 0; i < comega; i++)
	{
		Vector3 a = indata[i];
		Vector3 b = indata[i+1];
		Vector3 c = outdata[i+1];
		Vector3 d = outdata[i];
		
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
	triangulate(tri, 10);
}

@Override
public String toString()
{ return point+", "+normal+", "+innerradius+", "+outerradius; }

}
