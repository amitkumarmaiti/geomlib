// arch-tag: a0c2ab93-b996-43d9-ad84-bddd11bec5e4
package de.yvert.geometry;

import de.yvert.algorithms.PolygonClipper3D;
import de.yvert.cr.profiles.IntersectionResult;

public final class Triangle extends SceneItem
{

public Vector3 a, b, c;
public Vector3 n;

// 3*n texture coordinates for each vertex and each texture
public Vector4[] texcoordparams = new Vector4[0];

// shading normals for each vertex
public Vector3[] vertexNormals = new Vector3[3];

public Triangle()
{
	// Ok
}

public Triangle(Vector3 a, Vector3 b, Vector3 c, boolean flip)
{
	this.a = a;
	this.b = b;
	this.c = c;
	if (flip) flip();
	calculateNormal();
}

public Triangle(Vector3 a, Vector3 b, Vector3 c)
{ this(a, b, c, false); }


private void flip()
{
	Vector3 temp = c;
	c = a;
	a = temp;
}

public Vector3 getNormalVector(Vector3 dest)
{
	dest.set(n);
	return dest;
}

@Override
public void triangulate(Triangulation tri)
{
	tri.add(this);
}

public final void calculateNormal()
{
	n = b.sub(a).crossAndSet(c.sub(a));
	n.normalizeAndSet();
}

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{ geonormal.set(n); }

/*
 * Finds the smallest bounding box inside box that complete surrounds
 * those parts of this object that overlap box.
 * Returns 0 if box wasn't changed.
 * Returns 1 if the box was changed.
 * Returns -1 if this object doesn't overlap the box
 * (the resulting box would contain no triangle area, only a line segment 
 * or point of the triangle).
 * 
 * Example:
 * +--+--+       +--+--+
 * | / \ |       | / \ |
 * |/   \|       |/   \|
 * +     +       +     +
 * |\--  |\   => |\--  |\
 * |   \-+ \     +---\-+ \
 * |     |\-+           \-+
 * +-----+
 */
public int clip(BoundingBox box)
{
	PolygonClipper3D clipper = new PolygonClipper3D(a, b, c);
	clipper.clip(box);
	Vector3[] points = clipper.getPoints();
	if (points.length < 3) return -1;
	box.min.set(points[0]);
	box.max.set(points[0]);
	for (int i = 1; i < points.length; i++)
		box.ensureContainment(points[i]);
	return 1;
}

@Override
public double getMinX()
{
	double min = a.getV0();
	if (b.getV0() < min) min = b.getV0();
	if (c.getV0() < min) min = c.getV0();
	return min;
}

@Override
public double getMaxX()
{
	double max = a.getV0();
	if (b.getV0() > max) max = b.getV0();
	if (c.getV0() > max) max = c.getV0();
	return max;
}

@Override
public double getMinY()
{
	double min = a.getV1();
	if (b.getV1() < min) min = b.getV1();
	if (c.getV1() < min) min = c.getV1();
	return min;
}

@Override
public double getMaxY()
{
	double max = a.getV1();
	if (b.getV1() > max) max = b.getV1();
	if (c.getV1() > max) max = c.getV1();
	return max;
}

@Override
public double getMinZ()
{
	double min = a.getV2();
	if (b.getV2() < min) min = b.getV2();
	if (c.getV2() < min) min = c.getV2();
	return min;
}

@Override
public double getMaxZ()
{
	double max = a.getV2();
	if (b.getV2() > max) max = b.getV2();
	if (c.getV2() > max) max = c.getV2();
	return max;
}

public void calculateN()
{
	n = c.sub(a);
	n = b.sub(a).cross(n);
}

@Override
public BoundingBox getBoundingBox()
{
	return new BoundingBox(this);
}

@Override
public BoundingSphere getBoundingSphere()
{
	return new BoundingSphere(this);
}

@Override
public double distance(Ray ray)
{
	Vector3 q = ray.q;
	if (q == null) q = ray.p.add(ray.v);
	double v0 = VolumeHelper.signedVolume(ray.p, q, a, b);
	double v1 = VolumeHelper.signedVolume(ray.p, q, b, c);
	double v2 = VolumeHelper.signedVolume(ray.p, q, c, a);
	if (((v0 >= 0) && (v1 >= 0) && (v2 >= 0)) || ((v0 <= 0) && (v1 <= 0) && (v2 <= 0)))
		return (n.multiply(a)-n.multiply(ray.p))/n.multiply(ray.v);
	else
		return -1;
}


/**
 * returns the plane in which the triangle is
 */
public Vector4 getPlane(Vector4 in)
{
	double w = n.multiply(a);
	in.set(n, -w);
	return in;
}

/**
 * Calculates the barycentric coordinates of the intersection point of the
 * given ray with this triangle and returns the result.
 * 
 * @throws NullPointerException if bary is null
 */
public Vector3 barycentricCoords(Ray ray, Vector3 bary)
{
	double baryA = VolumeHelper.signedVolume(ray.p, ray.q, a, b);
	double baryB = VolumeHelper.signedVolume(ray.p, ray.q, b, c);
	double baryC = VolumeHelper.signedVolume(ray.p, ray.q, c, a);
	double sum = baryA+baryB+baryC;
	baryA /= sum; baryB /= sum; baryC /= sum;
	
	bary.set(baryB, baryC, baryA);
	return bary;
}

/**
 * Calculates the barycentric coordinates of the intersection point of the
 * given ray with this triangle and returns the result. The last coordinate
 * of <code>bary</code> is set to <code>0</code>.
 * 
 * @param ray a ray hitting this triangle
 * @param bary the result is written to this parameter
 * @return bary = the barycentric coordinates at which <code>ray</code> hits this triangle
 * @throws NullPointerException if bary is null
 */
public Vector4 barycentricCoords(Ray ray, Vector4 bary)
{
	double baryA = VolumeHelper.signedVolume(ray.p, ray.q, a, b);
	double baryB = VolumeHelper.signedVolume(ray.p, ray.q, b, c);
	double baryC = VolumeHelper.signedVolume(ray.p, ray.q, c, a);
	double sum = baryA+baryB+baryC;
	baryA /= sum; baryB /= sum; baryC /= sum;
	
	bary.set(baryB, baryC, baryA, 0);
	return bary;
}

/**
 * Returns a string suitable for debugging.
 */
@Override
public String toString()
{ return "Triangle: "+a+" "+b+" "+c; }

}
