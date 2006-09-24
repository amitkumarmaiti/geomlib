// arch-tag: 26f30bf1-263b-4e6c-82a5-ac75a3145982
package de.yvert.geometry;

public class Ray implements Cloneable
{

public Vector3 p = new Vector3();
public Vector3 v = new Vector3();
public Vector3 q;

public Ray()
{/*OK*/}

public Ray(Ray other)
{
	p.set(other.p);
	v.set(other.v);
	update();
}

@Override
public Ray clone()
{
	try
	{
		update();
		Ray result = (Ray) super.clone();
		result.p = result.p.clone();
		result.v = result.v.clone();
		result.q = result.q.clone();
		return result;
	}
	catch (CloneNotSupportedException e)
	{ throw new UnsupportedOperationException(e); }
}

public void set(Ray other)
{
	p.set(other.p);
	v.set(other.v);
	update();
}

public final void update()
{
	if (q != null)
		q.set(p).addAndSet(v);
	else
		q = p.add(v);
}

@Override
public String toString()
{ return p+" + t * "+v; }

}
