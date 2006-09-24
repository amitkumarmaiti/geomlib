// arch-tag: ca01c97f-35c1-4224-8fdd-7ad2470f998b
package de.yvert.geometry;

/**
 * A Vector4 is a 4-tuple of doubles <code>(v0, v1, v2, v3)</code>.
 * <p>
 * The values of this 4-tuple are also called <code>(x, y, z, w)</code>.
 * <p>
 * When a method has the addon <code>AndSet</code>, the result is stored in the current
 * object. This avoids creating new objects and can consequently make a program faster.
 * 
 * @author Sara Adams
 * @author Ulf Ochsenfahrt
 */
public final class Vector4 implements Cloneable
{

public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);
public static final Vector4 X = new Vector4(1, 0, 0, 0);
public static final Vector4 Y = new Vector4(0, 1, 0, 0);
public static final Vector4 Z = new Vector4(0, 0, 1, 0);
public static final Vector4 W = new Vector4(0, 0, 0, 1);

double v0, v1, v2, v3;

/**
 * Creates a Vector4 with <code>v0 = v1 = v2 = v3 = 0</code>.
 */
public Vector4()
{/*OK*/}

/**
 * Creates a Vector4 <code>(v0, v1, v2, v3)</code>.
 */
public Vector4(double v0, double v1, double v2, double v3)
{ set(v0, v1, v2, v3); }

/**
 * Creates a Vector4 with the same value as v.
 */
public Vector4(Vector4 v) 
{ set(v); }

/**
 * Creates a Vector4 with the same value v0, v1, v2 as v and v3 = 0.
 */
public Vector4(Vector3 v)
{ set(v); }

/**
 * Creates a Vector4 with the same value v0, v1 as v and v2 = v3 = 0.
 */
public Vector4(Vector2 v)
{ 
	v0 = v.getV0();
	v1 = v.getV1();
	v2 = v3 = 0;
}

public Vector4(float[] data)
{
	if (data.length != 4) throw new IllegalArgumentException();
	v0 = data[0];
	v1 = data[1];
	v2 = data[2];
	v3 = data[3];
}

public Vector4(float[] data, boolean lenient)
{
	if (data != null)
	{
		if (data.length > 0) v0 = data[0];
		if (data.length > 1) v1 = data[1];
		if (data.length > 2) v2 = data[2];
		if (data.length > 3) v3 = data[3];
	}
}

@Override
public Object clone() throws CloneNotSupportedException
{ return super.clone(); }

/**
 * Returns the first component of Vector4.
 */
public double getV0()
{ return v0; }

public double getX()
{ return v0; }

/**
 * Returns the second component of Vector4.
 */
public double getV1()
{ return v1; }

public double getY()
{ return v1; }

/**
 * Returns the third component of Vector4.
 */
public double getV2()
{ return v2; }

public double getZ()
{ return v2; }

/**
 * Returns the fourth and last component of Vector4.
 */
public double getV3()
{ return v3; }

public double getW()
{ return v3; }

/**
 * Returns a double[4] containing <code>v0, v1, v2, v3</code>.
 */
public double[] toArray()
{ return new double[] { v0, v1, v2, v3 }; }

/**
 * Returns the length of this Vector4.
 */
public double getLength()
{ return Math.sqrt(v0*v0 + v1*v1 + v2*v2 + v3*v3); }

/**
 * Sets the first component of this Vector4 to newEntry.
 */
public Vector4 setV0(double newV0)
{ v0 = newV0; return this; }

public Vector4 setX(double newV0)
{ v0 = newV0; return this; }

/**
 * Sets the second component of this Vector4 to newEntry.
 */
public Vector4 setV1(double newV1)
{ v1 = newV1; return this; }

public Vector4 setY(double newV1)
{ v1 = newV1; return this; }

/**
 * Sets the third component of this Vector4 to newEntry.
 */
public Vector4 setV2(double newV2)
{ v2 = newV2; return this; }

public Vector4 setZ(double newV2)
{ v2 = newV2; return this; }

/**
 * Sets the fourth and last component of this Vector4 to newEntry.
 */
public Vector4 setV3(double newV3)
{ v3 = newV3; return this; }

public Vector4 setW(double newV3)
{ v3 = newV3; return this; }

/**
 * Set this Vector4 to <code>(n0, n1, n2, n3)</code>.
 */
public Vector4 set(double n0, double n1, double n2, double n3)
{
	v0 = n0; v1 = n1; v2 = n2; v3 = n3;
	return this;
}

/**
 * Sets Vector4 to the same value als v.
 */
public Vector4 set(Vector4 v)
{ return set(v.v0, v.v1, v.v2, v.v3); }

public Vector4 set(Vector3 v, double v3)
{ return set(v.v0, v.v1, v.v2, v3); }

/**
 * Same as <code>set(v, 0)</code>.
 */
public Vector4 set(Vector3 v)
{ set(v, 0); return this; }

/**
 * Sets this Vector4 to <code>(d[0], d[1], d[2], d[3])</code>.
 * Throws an NumberFormatException if <code>d.length != 4</code>.
 */  
public Vector4 set(double[] d)
{
	if (d.length != 4) throw new NumberFormatException();
	v0 = d[0]; v1 = d[1]; v2 = d[2]; v3 = d[3];
	return this;
}

/**
 * Returns a normalized Vector4, i.e. with length 1.
 * Same as <code>scale(1/getLength())</code>.
 */
public Vector4 normalize()
{ return scale(1/getLength()); }

/**
 * Same as {@link #normalize()}, but stores the result in this Vector4.
 * Same as <code>scaleAndSet(1/getLength())</code>.
 */
public Vector4 normalizeAndSet()
{ return scaleAndSet(1/getLength()); }

/**
 * Returns a Vector4 scaled by s.
 */
public Vector4 scale(double s)
{ return new Vector4(this).scaleAndSet(s); }

/**
 * Same as {@link #scale(double d)}, but stores the result in this Vector4.
 */
public Vector4 scaleAndSet(double d)
{ v0 *= d; v1 *= d; v2 *= d; v3 *= d; return this; }

/**
 * Returns the sum of two Vector4.
 */
public Vector4 add(Vector4 v)
{ return new Vector4(this).addAndSet(v); }

/**
 * Same as {@link #add(Vector4)}, but stores the result in this Vector4.
 */
public Vector4 addAndSet(Vector4 v)
{ v0 += v.v0; v1 += v.v1; v2 += v.v2; v3 += v.v3; return this; }

/**
 * Same as {@link #addAndSet(Vector4)}, but with additional scaling.
 */
public Vector4 addAndSet(Vector4 v, double scale)
{ v0 += v.v0*scale; v1 += v.v1*scale; v2 += v.v2*scale; v3 += v.v3*scale; return this; }

/**
 * Returns the difference of the two Vector4.
 */
public Vector4 sub(Vector4 v)
{ return new Vector4(this).subAndSet(v); }

/**
 * Same as {@link #sub(Vector4)}, but stores the result in this Vector4.
 */
public Vector4 subAndSet(Vector4 v)
{ v0 -= v.v0; v1 -= v.v1; v2 -= v.v2; v3 -= v.v3; return this; }

/** 
 * Componentwise multiplication. For dot product see {@link #multiply(Vector4)}.
 */
public Vector4 mul(Vector4 v)
{ return new Vector4(v0*v.v0, v1*v.v1, v2*v.v2, v3*v.v3); }

/** 
 * Componentwise division.
 */
public Vector4 div(Vector4 v)
{ return new Vector4(v0/v.v0, v1/v.v1, v2/v.v2, v3/v.v3); }

/**
 * Returns the dot product of two Vector4.
 */
public double multiply(Vector4 v)
{ return v0*v.v0 + v1*v.v1 + v2*v.v2 + v3*v.v3; }

/**
 * Returns the cross product of two Vector4, i.e. the returned Vector4 is
 * orthogonal to the other two Vector4 and when ignoring the fourth
 * component the resulting Vector3 is also orthogonal.
 */
public Vector4 cross(Vector4 v)
{ return new Vector4(this).crossAndSet(v); }

/**
 * Same as {@link #cross(Vector4)}, but stores the result in this Vector4.
 */
public Vector4 crossAndSet(Vector4 v)
{
	double a = v1*v.v2-v2*v.v1;
	double b = v2*v.v0-v0*v.v2;
	double c = v0*v.v1-v1*v.v0;
	v0 = a; v1 = b; v2 = c; v3 = 0;
	return this;
}

/**
 * Returns the product of a Matrix4 and a Vector4.
 */
public Vector4 multiply(Matrix4 m)
{ return new Vector4(this).multiplyAndSet(m); }

/**
 * Same as {@link #multiply(Matrix4)}, but stores the result in the current Vector4.
 */
public Vector4 multiplyAndSet(Matrix4 m)
{
	double a = v0*m.matrix[0][0]+v1*m.matrix[0][1]+
	           v2*m.matrix[0][2]+v3*m.matrix[0][3];
	double b = v0*m.matrix[1][0]+v1*m.matrix[1][1]+
	           v2*m.matrix[1][2]+v3*m.matrix[1][3];
	double c = v0*m.matrix[2][0]+v1*m.matrix[2][1]+
	           v2*m.matrix[2][2]+v3*m.matrix[2][3];
	double d = v0*m.matrix[3][0]+v1*m.matrix[3][1]+
	           v2*m.matrix[3][2]+v3*m.matrix[3][3];
	v0 = a; v1 = b; v2 = c; v3 = d;
	return this;
}

public Vector4 multiplyAndSet(Vector4 a, Vector4 b, Vector4 c)
{
	double n0 = v0;
	double n1 = v1;
	double n2 = v2;
	v0 = n0*a.v0 + n1*b.v0 + n2*c.v0;
	v1 = n0*a.v1 + n1*b.v1 + n2*c.v1;
	v2 = n0*a.v2 + n1*b.v2 + n2*c.v2;
	v3 = 0;
	return this;
}

public Vector4 homogenize()
{ return new Vector4(this).homogenizeAndSet(); }

public Vector4 homogenizeAndSet()
{
	v0 /= v3;
	v1 /= v3;
	v2 /= v3;
	v3 = 1;
	return this;
}

public Vector4 adjustToScreenAndSet(int width, int height)
{
	v0 = (v0+1)*width/2.0;
	v1 = (v1+1)*height/2.0;
	return this;
}

public void get(float[] data)
{
	if (data.length != 4) throw new IllegalArgumentException();
	data[0] = (float) v0;
	data[1] = (float) v1;
	data[2] = (float) v1;
	data[3] = (float) v1;
}

/**
 * Returns a String representation of this Vector4, i.e. <code>(v0, v1, v2, v3)</code>.
 */
@Override
public String toString() 
{ return "("+v0+", "+v1+", "+v2+", "+v3+")"; }

}
