// arch-tag: e912d82e-6db0-43a5-9d4f-702a1e50adf6
package de.yvert.geometry;

/**
 * A Vector2 is a 2-tuple of doubles ( v0, v1 ).
 * <p>
 * When a method has the addon "AndSet", the result is stored in the current
 * object. This avoids creating new objects and consequently makes a program
 * faster.
 * 
 * @author Sara Adams
 * @author Ulf Ochsenfahrt
 */
public final class Vector2 implements Cloneable
{

double v0, v1;

/**
 * Creates a Vector2 with v0=v1=v2=0.
 */
public Vector2()
{/*OK*/}

/**
 * Creates a Vector2 ( v0, v1 ).
 */
public Vector2(double v0, double v1)
{ this.v0=v0; this.v1=v1; }

/**
 * Creates a Vector2 with the same value as v (copy constructor).
 */
public Vector2(Vector2 v)
{ this(v.v0, v.v1); }

public Vector2(float[] v)
{ this(v[0], v[1]); }

public Vector2(double[] v)
{ this(v[0], v[1]); }

@Override
public Object clone() throws CloneNotSupportedException
{ return super.clone(); }

/**
 * Returns the first component of Vector2.
 */
public double getV0()
{ return v0; }

public double getX()
{ return v0; }

/**
 * Returns the second component of Vector2.
 */
public double getV1()
{ return v1; }

public double getY()
{ return v1; }

/**
 * Returns a double[2] containing v0,v1.
 */
public double[] toDoubleArray()
{ return new double[] {v0,v1}; }

public void get(double[] data)
{
	data[0] = v0;
	data[1] = v1;
}

public void get(float[] data)
{
	data[0] = (float) v0;
	data[1] = (float) v1;
}

/**
 * Returns the length of the vector.
 */
public double getLength()
{ return Math.sqrt(v0*v0+v1*v1); }

public double getSquaredLength()
{ return v0*v0+v1*v1; }

/**
 * Sets the first component of Vector2 to newEntry.
 */
public Vector2 setV0(double newV0)
{ v0=newV0; return this; }

public Vector2 setX(double newV0)
{ v0=newV0; return this; }

/**
 * Sets the second component of Vector2 to newEntry.
 */
public Vector2 setV1(double newV1)
{ v1=newV1; return this; }

public Vector2 setY(double newV1)
{ v1=newV1; return this; }

/**
 * Sets Vector2 to the same value als v.
 */
public Vector2 set(Vector2 v)
{ v0 = v.getV0(); v1 = v.getV1(); return this; }

public Vector2 set(Vector2 v, double scale)
{ v0 = scale*v.getV0(); v1 = scale*v.getV1(); return this; }

public Vector2 set(double v0, double v1)
{ this.v0 = v0; this.v1 = v1; return this; }

/**
 * Sets the Vector2 to ( d[0], d[1] ).
 * Throws an NumberFormatException if d.length != 2.
 */
public Vector2 set(double[] d)
{
	if (d.length != 2) throw new NumberFormatException();
	v0 = d[0]; v1 = d[1];
	return this;
}

public Vector2 set(float[] d)
{
	if (d.length != 2) throw new NumberFormatException();
	v0 = d[0]; v1 = d[1];
	return this;
}

/**
 * Sets this vector to the value of <code>s.v0*at + s.v1*bt + s.v2*ct</code>.
 */
public Vector2 set(Vector3 s, Vector2 at, Vector2 bt, Vector2 ct)
{
	v0 = s.getV0()*at.getV0() + s.getV1()*bt.getV0() + s.getV2()*ct.getV0();
	v1 = s.getV0()*at.getV1() + s.getV1()*bt.getV1() + s.getV2()*ct.getV1();
	return this;
}

/**
 * Returns a normalized Vector2, i.e. with length 1.
 */
public Vector2 normalize()
{ return scale(1/getLength()); }

/**
 * Same as normalize(), but stores the result in the current Vector2.
 */
public Vector2 normalizeAndSet()
{  return scaleAndSet(1/getLength()); }

/**
 * Returns the product of two Vector2.
 */
public double multiply(Vector2 v)
{ return v0*v.v0+v1*v.v1; }

/**
 * Returns a Vector2 scaled by d.
 */
public Vector2 scale(double d)
{ return new Vector2(d*v0,d*v1); }

/**
 * Same as scale(double d), but stores the result in the current Vector2.
 */
public Vector2 scaleAndSet(double d)
{ v0*=d; v1*=d; return this; }

/**
 * Returns the sum of two Vector2.
 */
public Vector2 add(Vector2 v)
{ return new Vector2(v0+v.v0,v1+v.v1); }

/**
 * Same as add(Vector2 v), but stores the result in the current Vector2.
 */
public Vector2 addAndSet(Vector2 v)
{ v0+=v.v0; v1+=v.v1; return this; }

public Vector2 addAndSet(Vector2 v, double scale)
{ v0 += scale*v.v0; v1 += scale*v.v1; return this; }

/**
 * Returns the difference of two Vector2.
 */
public Vector2 sub(Vector2 v)
{ return new Vector2(v0-v.v0, v1-v.v1); }

/**
 * Same as sub(Vector2 v), but stores the result in the current Vector2.
 */
public Vector2 subAndSet(Vector2 v)
{ v0-=v.v0; v1-=v.v1; return this; }

/**
 * Returns a String ( v0, v1, v2 ).
 */
@Override
public String toString() 
{ return "( "+v0+", "+v1+" )"; }

public boolean same(Vector2 other)
{ return (v0 == other.v0) && (v1 == other.v1); }

public double squaredDistance(Vector2 other)
{
	double dx = v0-other.v0;
	double dy = v1-other.v1;
	return dx*dx + dy*dy;
}

public void clipRepeat()
{
	v0 -= Math.floor(v0);
	v1 -= Math.floor(v1);
}

/**
 * Returns the distance between this vector and the given vector.
 */
public double distance(Vector2 other)
{
	return Math.sqrt(squaredDistance(other));
}

}
