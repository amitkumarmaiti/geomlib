package de.yvert.geometry;

public class Plane
{

private final Vector3 pos = new Vector3();
private final Vector3 n = new Vector3();

public Plane(Vector3 pos, Vector3 n)
{
	this.pos.set(pos);
	this.n.set(n);
}

public Vector3 intersect(Ray ray, Vector3 result)
{
	result.set(ray.p);
	result.subAndSet(pos);
	double enumerator = result.multiply(n);
	double denominator = ray.v.multiply(n);
	double t = -enumerator/denominator;
	if (t < 0) return null;
	result.set(ray.p);
	result.addAndSet(ray.v, t);
	return result;
}

}
