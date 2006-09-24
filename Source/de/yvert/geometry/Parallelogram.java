// arch-tag: 348a8102-f2d4-4f15-9ae4-42977da3079e
package de.yvert.geometry;

import de.yvert.cr.profiles.IntersectionResult;

/**
 * A parallelogram implementation. Use this instead of 
 * two adjacent triangles to reduce numerical errors. 
 * 
 * @author Stefan Goldmann
 *
 */
public class Parallelogram extends SceneItem
{

private static final double EPSILON = 1e-4f;
public Vector3 a,b,c;
public Vector3 u,v,n;

//4*n texture coordinates for each vertex and each texture
public Vector4[] texcoordparams = new Vector4[0];

public Parallelogram(Vector3 a, Vector3 b, Vector3 c)
{
	this.a = new Vector3(a);
	this.b = new Vector3(b);
	this.c = new Vector3(c);	
	u = new Vector3(b).subAndSet(a);
	v = new Vector3(c).subAndSet(b);
	n = u.cross(v).normalizeAndSet();
}

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{ geonormal.set(n); }

@Override
public double getMinX()
{
	return Math.min(a.getX(), Math.min(b.getX(), c.getX()));
}

@Override
public double getMaxX()
{
	return Math.max(a.getX(), Math.max(b.getX(), c.getX()));
}

@Override
public double getMinY()
{
	return Math.min(a.getY(), Math.min(b.getY(), c.getY()));
}

@Override
public double getMaxY()
{
	return Math.max(a.getY(), Math.max(b.getY(), c.getY()));
}

@Override
public double getMinZ()
{
	return Math.min(a.getZ(), Math.min(b.getZ(), c.getZ()));
}

@Override
public double getMaxZ()
{
	return Math.max(a.getZ(), Math.max(b.getZ(), c.getZ()));
}

@Override
public BoundingSphere getBoundingSphere()
{
	// Yes, it's suboptimal ...
	BoundingBox bb = this.getBoundingBox();
	return new BoundingSphere(bb.center, bb.max.sub(bb.center).getLength());
}

private double det3(Vector3 v1, Vector3 v2, Vector3 v3)
{ 
	return    v1.getX()*(v2.getY()*v3.getZ() - v3.getY()*v2.getZ()) 
			+ v1.getY()*(v3.getX()*v2.getZ() - v2.getX()*v3.getZ()) 
			+ v1.getZ()*(v2.getX()*v3.getY() - v2.getY()*v3.getX()); 
}

@Override
public double distance(Ray ray)
{
	Vector3 o = a, d = ray.v;
	Vector3 tmp = ray.p.sub(o);
	
	double det = det3(u,v,d);
	if (Math.abs(det) < EPSILON) return -1;
	
	double t = -det3(u,v,tmp)/det;
	if (t < EPSILON) return -1;
	
	double alpha = det3(tmp,v,d)/det;
	if ((alpha < 0) || (alpha > 1)) return -1;
	
	double beta  = det3(u,tmp,d)/det;
	if ((beta < 0) || (beta > 1)) return -1;
	
	return t;
}

@Override
public void triangulate(Triangulation tri)
{
	Triangle t1 = new Triangle(a,b,c);
	t1.setMaterial(material);
	tri.add(t1);
	
	Triangle t2 = new Triangle(a,c,a.add(v));
	t2.setMaterial(material);
	tri.add(t2);
}

}
