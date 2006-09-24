// arch-tag: 19803ac3-6b32-4a9d-83fe-15f1b46af57f
package de.yvert.geometry;

public class BoundingSphere
{

private static final double EPSILON = 1e-4f;

public Vector3 point;
public double radius;

public BoundingSphere(BoundingSphere other)
{
	set(other);
}

public BoundingSphere(Vector3 point, double radius)
{
	this.point = new Vector3(point);
	this.radius = radius;
	if (Double.isNaN(point.getV0()) || Double.isNaN(point.getV1()) ||
	    Double.isNaN(point.getV2()) || Double.isNaN(radius))
		throw new RuntimeException();
}

public BoundingSphere(Triangle t)
{
	setContaining(t);
}


public double getRadius()
{ return radius; }

public Vector3 getCenter()
{ return point; }

public BoundingSphere enlarge(double eps)
{
	radius += eps;
	return this;
}

public final BoundingSphere set(BoundingSphere other)
{
	point = new Vector3(other.point);
	radius = other.radius;
	return this;
}

public BoundingSphere setExact(Vector3 a, Vector3 b)
{
	Vector3 e = b.sub(a);
	Vector3 f = a.add(b).scaleAndSet(1/2.0);
	point = f;
	radius = e.getLength()/2.0+EPSILON;
	return this;
}

public BoundingSphere setExact(Vector3 a, Vector3 b, Vector3 c)
{
	Vector3 e = a.sub(c);
	Vector3 f = b.sub(c);
	
	double e2 = e.getSquaredLength();
	double f2 = f.getSquaredLength();
	double ef = e.multiply(f);
	
	double d = 2*(e2*f2 - ef*ef);
	double s0E = (e2-ef)*f2;
	double s1E = (f2-ef)*e2;
	
	double s0 = s0E / d;
	double s1 = s1E / d;
	
	// a + (s0-1)*e + s1*f
	e.scaleAndSet(s0-1).addAndSet(f.scaleAndSet(s1)).addAndSet(a);
	
	point = e;
	radius = Math.sqrt(s0*s0*e2 + 2*s0*s1*ef + s1*s1*f2)+EPSILON;
	return this;
}

public final BoundingSphere setContaining(Triangle t)
{
	if (setExact(t.a, t.b).contains(t.c)) return this;
	if (setExact(t.a, t.c).contains(t.b)) return this;
	if (setExact(t.b, t.c).contains(t.a)) return this;
	return setExact(t.a, t.b, t.c);
}

public double approxSquaredDistance(BoundingSphere other)
{ return point.squaredDistance(other.point); }

public double distance(BoundingSphere other)
{ return point.distance(other.point); }

public boolean contains(Vector3 a)
{ return point.squaredDistance(a) <= radius*radius; }

public double surfaceArea()
{ return 4*Math.PI*radius*radius; }

/**
 * Is being used by the bounding sphere hierarchy only!
 * @param ray the ray
 * @param maxDistance the maximum distance
 * @return something
 */
public double intersects2(Ray ray, double maxDistance)
{
	double b = ray.v.multiply(ray.p) - ray.v.multiply(point);
	double c = ray.p.squaredDistance(point) - radius*radius;
	
	double belowrt = b*b-c;
	if (belowrt < 0) return -1;
	
	double rt = Math.sqrt(belowrt);
	double min = -b-rt;
	double max = -b+rt;
	if (min > maxDistance) return -1;
	if (max < 0) return -1;
	
	return min > 0 ? min : 0;
}

public double getMinX()
{ return point.getX()-radius; }

public double getMaxX()
{ return point.getX()+radius; }

public double getMinY()
{ return point.getY()-radius; }

public double getMaxY()
{ return point.getY()+radius; }

public double getMinZ()
{ return point.getZ()-radius; }

public double getMaxZ()
{ return point.getZ()+radius; }

@Override
public String toString()
{ return point+", "+radius; }

}
