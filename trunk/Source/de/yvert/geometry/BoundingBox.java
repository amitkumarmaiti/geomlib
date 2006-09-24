// arch-tag: 93c11d84-9a20-46dc-b963-aa701eab2bf5
package de.yvert.geometry;

public class BoundingBox
{

private static final double EPSILON = 1e-10d;
private static final double BIG     = 1e+10d;

public Vector3 min, max;
public Vector3 center;

protected BoundingBox()
{/*OK*/}

public BoundingBox(BoundingBox data)
{
	this.min = new Vector3(data.min);
	this.max = new Vector3(data.max);
	this.center = data.center;
}

public BoundingBox(Vector3 min, Vector3 max)
{
	this.min = new Vector3(min);
	this.max = new Vector3(max);
	this.center = max.add(min).scaleAndSet(1/2.);
}

public BoundingBox(Triangle t)
{
	min = t.a.componentMin(t.b);
	min.componentMinAndSet(t.c);
	max = t.a.componentMax(t.b);
	max.componentMaxAndSet(t.c);
	center = max.add(min).scaleAndSet(1/2.);
}

public BoundingBox(Sphere s)
{
	Vector3 temp = new Vector3(s.radius, s.radius, s.radius);
	min = s.point.sub(temp);
	max = s.point.add(temp);
	center = max.add(min).scaleAndSet(1/2.);
}


public double getMinX()
{ return min.getX(); }

public double getMinY()
{ return min.getY(); }

public double getMinZ()
{ return min.getZ(); }

public double getMaxX()
{ return max.getX(); }

public double getMaxY()
{ return max.getY(); }

public double getMaxZ()
{ return max.getZ(); }

public void ensureContainment(BoundingBox other)
{
	min.componentMinAndSet(other.min);
	max.componentMaxAndSet(other.max);
}

public void ensureContainment(Vector3 point)
{
	min.componentMinAndSet(point);
	max.componentMaxAndSet(point);
}

public void ensureContainment(Triangle tri)
{
	ensureContainment(tri.a);
	ensureContainment(tri.b);
	ensureContainment(tri.c);
}

public BoundingBox enlarge(double eps)
{
	Vector3 veps = new Vector3(eps, eps, eps);
	min.subAndSet(veps);
	max.addAndSet(veps);
	return this;
}

public double approxSquaredDistance(BoundingBox other)
{
	return center.squaredDistance(other.center);
}

public double volume()
{
	return (max.getX()-min.getX())*
	       (max.getY()-min.getY())*
	       (max.getZ()-min.getZ());
}

/* Returns HALF the surface area! */
public double halfSurfaceArea()
{
	double dx = max.getX()-min.getX();
	double dy = max.getY()-min.getY();
	double dz = max.getZ()-min.getZ();
	return dx*dy + dx*dz + dy*dz;
}

public double surfaceArea()
{ return 2*halfSurfaceArea(); }

public boolean contains(Vector3 a)
{
	return (a.getV0() >= min.getV0()) &&
	       (a.getV1() >= min.getV1()) &&
	       (a.getV2() >= min.getV2()) &&
	       (a.getV0() <= max.getV0()) &&
	       (a.getV1() <= max.getV1()) &&
	       (a.getV2() <= max.getV2());
}

public static double oneOver(double x)
{
	if (x > 0)
	{
		if (x < EPSILON)
			return BIG;
		else
			return 1/x;
	}
	else
	{
		if (x > -EPSILON)
			return -BIG;
		else
			return 1/x;
	}
}

public final double intersects2(double px, double py, double pz, double vix, double viy, double viz, double maxDistance)
{
	double help;
	double tmin;
	double tmax;
	
	{
		double xmin = (min.getX()-px)*vix;
		double xmax = (max.getX()-px)*vix;
		if (vix < 0) { help = xmin; xmin = xmax; xmax = help; }
		tmin = xmin;
		tmax = xmax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	
	{
		double ymin = (min.getV1()-py)*viy;
		double ymax = (max.getV1()-py)*viy;
		if (viy < 0) { help = ymin; ymin = ymax; ymax = help; }
		if (ymin > tmin) tmin = ymin;
		if (ymax < tmax) tmax = ymax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	
	{
		double zmin = (min.getV2()-pz)*viz;
		double zmax = (max.getV2()-pz)*viz;
		if (viz < 0) { help = zmin; zmin = zmax; zmax = help; }
		if (zmin > tmin) tmin = zmin;
		if (zmax < tmax) tmax = zmax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	if (tmin > tmax) return -1;
	
	return tmin > 0 ? tmin : 0;
}

public double intersects2(Ray ray, Vector3 accel, double maxDistance)
{
	double help;
	double tmin;
	double tmax;
	
	{
		double dx = accel.getX();
		double xmin = (min.getV0()-ray.p.getV0())*dx;
		double xmax = (max.getV0()-ray.p.getV0())*dx;
		if (dx < 0) { help = xmin; xmin = xmax; xmax = help; }
		
		tmin = xmin;
		tmax = xmax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	
	{
		double dy = accel.getY();
		double ymin = (min.getV1()-ray.p.getV1())*dy;
		double ymax = (max.getV1()-ray.p.getV1())*dy;
		if (dy < 0) { help = ymin; ymin = ymax; ymax = help; }
		
		if (ymin > tmin) tmin = ymin;
		if (ymax < tmax) tmax = ymax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	
	{
		double dz = accel.getZ();
		double zmin = (min.getV2()-ray.p.getV2())*dz;
		double zmax = (max.getV2()-ray.p.getV2())*dz;
		if (dz < 0) { help = zmin; zmin = zmax; zmax = help; }
		
		if (zmin > tmin) tmin = zmin;
		if (zmax < tmax) tmax = zmax;
	}
	
	if (tmin > maxDistance) return -1;
	if (tmax < 0) return -1;
	if (tmin > tmax) return -1;
	
	return tmin > 0 ? tmin : 0;
}

public double intersects2(Ray ray, double maxDistance)
{
	double help;
	double dx = oneOver(ray.v.getV0());
	double xmin = (min.getV0()-ray.p.getV0())*dx;
	double xmax = (max.getV0()-ray.p.getV0())*dx;
	if (dx < 0) { help = xmin; xmin = xmax; xmax = help; }
	
	if (xmin > maxDistance) return -1;
	if (xmax < 0) return -1;
	
	double dy = oneOver(ray.v.getV1());
	double ymin = (min.getV1()-ray.p.getV1())*dy;
	double ymax = (max.getV1()-ray.p.getV1())*dy;
	if (dy < 0) { help = ymin; ymin = ymax; ymax = help; }
	
	if (ymin > maxDistance) return -1;
	if (ymax < 0) return -1;
	
	double dz = oneOver(ray.v.getV2());
	double zmin = (min.getV2()-ray.p.getV2())*dz;
	double zmax = (max.getV2()-ray.p.getV2())*dz;
	if (dz < 0) { help = zmin; zmin = zmax; zmax = help; }
	
	if (ymin > xmin) xmin = ymin;
	if (zmin > xmin) xmin = zmin;
	
	if (ymax < xmax) xmax = ymax;
	if (zmax < xmax) xmax = zmax;
	
	if (xmin > xmax) return -1;
	if (xmin > maxDistance) return -1;
	if (xmax < 0) return -1;
	return xmin > 0 ? xmin : 0;
}

public boolean intersects(Ray ray, double minDistance, double maxDistance)
{
	double help;
	double dx = oneOver(ray.v.getV0());
	double dy = oneOver(ray.v.getV1());
	double dz = oneOver(ray.v.getV2());
	
	double xmin = (min.getV0()-ray.p.getV0())*dx;
	double xmax = (max.getV0()-ray.p.getV0())*dx;
	if (dx < 0) { help = xmin; xmin = xmax; xmax = help; }
	
	double ymin = (min.getV1()-ray.p.getV1())*dy;
	double ymax = (max.getV1()-ray.p.getV1())*dy;
	if (dy < 0) { help = ymin; ymin = ymax; ymax = help; }
	
	double zmin = (min.getV2()-ray.p.getV2())*dz;
	double zmax = (max.getV2()-ray.p.getV2())*dz;
	if (dz < 0) { help = zmin; zmin = zmax; zmax = help; }
	
	if (ymin > xmin) xmin = ymin;
	if (zmin > xmin) xmin = zmin;
	
	if (ymax < xmax) xmax = ymax;
	if (zmax < xmax) xmax = zmax;
	
	if (xmin > xmax) return false;
	if (xmin > maxDistance) return false;
	if (xmax < minDistance) return false;
	return true;
}

public boolean intersects(Ray ray)
{ return intersects(ray, 0, Double.MAX_VALUE); }

public double intersectsMax(Ray ray)
{
	double help;
	double dx = oneOver(ray.v.getV0());
	double dy = oneOver(ray.v.getV1());
	double dz = oneOver(ray.v.getV2());
	
	double xmin = (min.getV0()-ray.p.getV0())*dx;
	double xmax = (max.getV0()-ray.p.getV0())*dx;
	if (dx < 0) { help = xmin; xmin = xmax; xmax = help; }
	
	double ymin = (min.getV1()-ray.p.getV1())*dy;
	double ymax = (max.getV1()-ray.p.getV1())*dy;
	if (dy < 0) { help = ymin; ymin = ymax; ymax = help; }
	
	double zmin = (min.getV2()-ray.p.getV2())*dz;
	double zmax = (max.getV2()-ray.p.getV2())*dz;
	if (dz < 0) { help = zmin; zmin = zmax; zmax = help; }
	
	if (ymin > xmin) xmin = ymin;
	if (zmin > xmin) xmin = zmin;
	
	if (ymax < xmax) xmax = ymax;
	if (zmax < xmax) xmax = zmax;
	
	if (xmin > xmax) return -1;
	if (xmax < 0) return -1;
	return xmax;
}

public double intersectsMin(Ray ray)
{
	double help;
	double dx = oneOver(ray.v.getV0());
	double dy = oneOver(ray.v.getV1());
	double dz = oneOver(ray.v.getV2());
	
	double xmin = (min.getV0()-ray.p.getV0())*dx;
	double xmax = (max.getV0()-ray.p.getV0())*dx;
	if (dx < 0) { help = xmin; xmin = xmax; xmax = help; }
	
	double ymin = (min.getV1()-ray.p.getV1())*dy;
	double ymax = (max.getV1()-ray.p.getV1())*dy;
	if (dy < 0) { help = ymin; ymin = ymax; ymax = help; }
	
	double zmin = (min.getV2()-ray.p.getV2())*dz;
	double zmax = (max.getV2()-ray.p.getV2())*dz;
	if (dz < 0) { help = zmin; zmin = zmax; zmax = help; }
	
	if (ymin > xmin) xmin = ymin;
	if (zmin > xmin) xmin = zmin;
	
	if (ymax < xmax) xmax = ymax;
	if (zmax < xmax) xmax = zmax;
	
	if (xmin > xmax) return -1;
	if (xmax < 0) return -1;
	return xmin;
}

@Override
public String toString()
{ return min+" to "+max; }

}
