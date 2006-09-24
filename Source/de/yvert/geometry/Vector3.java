// arch-tag: 203d1338-6d75-4f5d-a6d6-47055b162a3d
package de.yvert.geometry;

import java.util.regex.*;

/**
 * A Vector3 is a 3-tuple of doubles ( v0, v1, v2 ).
 * <p>
 * When a method has the addon "AndSet", the result is stored in the current
 * object. This avoids creating new objects and consequently makes a program
 * faster.
 * 
 * @author Sara Adams, Ulf Ochsenfahrt
 */
public final class Vector3 implements Cloneable
{

private static final String DOUBLE_PATTERN =
	"(?:\\+|\\-)?(?:NaN|Infinity|\\d+(?:\\.\\d*)?(?:(?:e|E)(?:\\+|\\-)?\\d+)?)";
private static final Pattern SERIAL_PATTERN = Pattern.compile(
	"\\(("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+")\\)");

public static final Vector3 ZERO = new Vector3(0, 0, 0);
public static final Vector3 X = new Vector3(1, 0, 0);
public static final Vector3 Y = new Vector3(0, 1, 0);
public static final Vector3 Z = new Vector3(0, 0, 1);

double v0, v1, v2;

/**
 * Creates a Vector3 with v0=v1=v2=0.
 */
public Vector3()
{/*OK*/}

/**
 * Creates a Vector3 ( v0, v1, v2 ).
 */
public Vector3(double v0, double v1, double v2)
{ this.v0=v0; this.v1=v1; this.v2=v2; }

/**
 * Creates a Vector3 with the same value as v (copy constructor).
 */
public Vector3(Vector3 v)
{ this(v.v0, v.v1, v.v2); }

public Vector3(float[] v)
{ this(v[0], v[1], v[2]); }

public Vector3(double[] v)
{ this(v[0], v[1], v[2]); }

@Override
public Vector3 clone()
{
	try
	{ return (Vector3) super.clone(); }
	catch (CloneNotSupportedException e)
	{ throw new UnsupportedOperationException(e); }
}

/**
 * Returns the first component of Vector3.
 */
public double getV0()
{ return v0; }

public double getX()
{ return v0; }

/**
 * Returns the second component of Vector3.
 */
public double getV1()
{ return v1; }

public double getY()
{ return v1; }

/**
 * Returns the third and last component of Vector3.
 */
public double getV2()
{ return v2; }

public double getZ()
{ return v2; }

/**
 * Returns a double[3] containing v0,v1,v2.
 */
public double[] toDoubleArray()
{ return new double[] {v0,v1,v2}; }

public void get(double[] data)
{
	data[0] = v0;
	data[1] = v1;
	data[2] = v2;
}

public void get(float[] data)
{
	data[0] = (float) v0;
	data[1] = (float) v1;
	data[2] = (float) v2;
}

/**
 * Returns the length of the vector.
 */
public double getLength()
{ return Math.sqrt(v0*v0+v1*v1+v2*v2); }

public double getSquaredLength()
{ return v0*v0+v1*v1+v2*v2; }

/**
 * Sets the first component of Vector3 to newEntry.
 */
public Vector3 setV0(double newV0)
{ v0 = newV0; return this; }

public Vector3 setX(double newV0)
{ v0 = newV0; return this; }

/**
 * Sets the second component of Vector3 to newEntry.
 */
public Vector3 setV1(double newV1)
{ v1 = newV1; return this; }

public Vector3 setY(double newV1)
{ v1 = newV1; return this; }

/**
 * Sets the third and last component of Vector3 to newEntry.
 */
public Vector3 setV2(double newV2)
{ v2 = newV2; return this; }

public Vector3 setZ(double newV2)
{ v2 = newV2; return this; }

/**
 * Sets Vector3 to the same value als v.
 */
public Vector3 set(Vector3 v)
{ v0 = v.getV0(); v1 = v.getV1(); v2 = v.getV2(); return this; }

public Vector3 set(double v0, double v1, double v2)
{ this.v0 = v0; this.v1 = v1; this.v2 = v2; return this; }

/**
 * Set this vector to the first three components of v.
 */
public Vector3 set(Vector4 v)
{ v0 = v.getV0(); v1 = v.getV1(); v2 = v.getV2(); return this; }

/**
 * Sets the Vector3 to ( d[0], d[1], d[2] ).
 * Throws an NumberFormatException if d.length != 3.
 */
public Vector3 set(double[] d)
{
	if (d.length != 3) throw new NumberFormatException();
	v0 = d[0]; v1 = d[1]; v2 = d[2];
	return this;
}

public Vector3 set(float[] d)
{
	if (d.length != 3) throw new NumberFormatException();
	v0 = d[0]; v1 = d[1]; v2 = d[2];
	return this;
}

/**
 * Returns a normalized Vector3, i.e. with length 1.
 */
public Vector3 normalize()
{ return scale(1/getLength()); }

/**
 * Same as normalize(), but stores the result in the current Vector3.
 */
public Vector3 normalizeAndSet()
{  return scaleAndSet(1/getLength()); }

/**
 * Returns the product of two Vector3.
 */
public double multiply(Vector3 v)
{ return v0*v.v0+v1*v.v1+v2*v.v2; }

public double multiply(double x, double y, double z)
{ return v0*x+v1*y+v2*z; }

/**
 * Returns the crossproduct of two Vector3, i.e. the returned Vector3 is
 * orthogonal to the other two Vector3.
 */
public Vector3 cross(Vector3 v)
{ return new Vector3(this).crossAndSet(v); }

/**
 * Same as cross(Vector3 v), but stores the result in the current Vector3.
 */
public Vector3 crossAndSet(Vector3 v)
{
	double a = v1*v.v2 - v2*v.v1;
	double b = v2*v.v0 - v0*v.v2;
	double c = v0*v.v1 - v1*v.v0;
	v0 = a;
	v1 = b;
	v2 = c;
	return this;
}

/**
 * Returns a Vector3 scaled by d.
 */
public Vector3 scale(double d)
{ return new Vector3(d*v0,d*v1,d*v2); }

/**
 * Same as scale(double d), but stores the result in the current Vector3.
 */
public Vector3 scaleAndSet(double d)
{ v0*=d; v1*=d; v2*=d; return this; }

/**
 * Returns the sum of two Vector3.
 */
public Vector3 add(Vector3 v)
{ return new Vector3(v0+v.v0,v1+v.v1,v2+v.v2); }

/**
 * Same as add(Vector3 v), but stores the result in the current Vector3.
 */
public Vector3 addAndSet(Vector3 v)
{ v0+=v.v0; v1+=v.v1; v2+=v.v2; return this; }

/**
 * Sets this vector to the sum of this vector and s times the given vector.
 */
public Vector3 addAndSet(Vector3 v, double s)
{
	v0 += s*v.v0;
	v1 += s*v.v1;
	v2 += s*v.v2;
	return this;
}

/**
 * Returns the difference of two Vector3.
 */
public Vector3 sub(Vector3 v)
{ return new Vector3(v0-v.v0, v1-v.v1, v2-v.v2); }

/**
 * Same as sub(Vector3 v), but stores the result in the current Vector3.
 */
public Vector3 subAndSet(Vector3 v)
{ v0-=v.v0; v1-=v.v1; v2-=v.v2; return this; }

public Vector3 subAndSet(Vector3 v, double s)
{
	v0 -= s*v.v0;
	v1 -= s*v.v1;
	v2 -= s*v.v2;
	return this;
}

/**
 * Returns the product of a Matrix3 and a Vector3.
 */
public Vector3 multiply(Matrix3 m)
{ return new Vector3(this).multiplyAndSet(m); }

/**
 * Same as multiply(), but stores the result in the current Vector3.
 */
public Vector3 multiplyAndSet(Matrix3 m)
{
	double a = v0*m.matrix[0][0] + v1*m.matrix[0][1] + v2*m.matrix[0][2];
	double b = v0*m.matrix[1][0] + v1*m.matrix[1][1] + v2*m.matrix[1][2];
	double c = v0*m.matrix[2][0] + v1*m.matrix[2][1] + v2*m.matrix[2][2];
	v0 = a; v1 = b; v2 = c;
	return this;
}

/**
 * Returns the product of a Matrix4 and a Vector3.
 * A one is appended to this Vector3 and the multiplication done as usual.
 * The result is homogenized by dividing by the forth component, after which
 * the forth component is discarded again.
 */
public Vector3 multiply(Matrix4 m)
{ return new Vector3(this).multiplyAndSet(m); }

/**
 * Same as multiply(), but stores the result in the current Vector3.
 */
public Vector3 multiplyAndSet(Matrix4 m)
{
	double a=v0*m.matrix[0][0]+v1*m.matrix[0][1]+v2*m.matrix[0][2]+m.matrix[0][3];
	double b=v0*m.matrix[1][0]+v1*m.matrix[1][1]+v2*m.matrix[1][2]+m.matrix[1][3];
	double c=v0*m.matrix[2][0]+v1*m.matrix[2][1]+v2*m.matrix[2][2]+m.matrix[2][3];
	double d=v0*m.matrix[3][0]+v1*m.matrix[3][1]+v2*m.matrix[3][2]+m.matrix[3][3];
	v0 = a/d; v1 = b/d; v2 = c/d;
	return this;
}

public Vector3 multiplyAndSet(Vector3 a, Vector3 b, Vector3 c)
{
	double n0 = v0;
	double n1 = v1;
	double n2 = v2;
	v0 = n0*a.v0 + n1*b.v0 + n2*c.v0;
	v1 = n0*a.v1 + n1*b.v1 + n2*c.v1;
	v2 = n0*a.v2 + n1*b.v2 + n2*c.v2;
	return this;
}

/**
 * Adds the first column of the given Matrix3 to this Vector3.
 * Returns this vector. Useful for incremental ray calculation for a camera.
 */
public Vector3 addFirstColumn(Matrix3 m)
{
	v0 += m.matrix[0][0];
	v1 += m.matrix[1][0];
	v2 += m.matrix[2][0];
	return this;
}


public Vector3 componentMin(Vector3 other)
{ return new Vector3(this).componentMinAndSet(other); }

public Vector3 componentMinAndSet(Vector3 other)
{
	if (other.v0 < v0) v0 = other.v0;
	if (other.v1 < v1) v1 = other.v1;
	if (other.v2 < v2) v2 = other.v2;
	return this;
}

public Vector3 componentMinAndSet(double a, double b, double c)
{
	if (a < v0) v0 = a;
	if (b < v1) v1 = b;
	if (c < v2) v2 = c;
	return this;
}

public Vector3 componentMax(Vector3 other)
{ return new Vector3(this).componentMaxAndSet(other); }

public Vector3 componentMaxAndSet(Vector3 other)
{
	if (other.v0 > v0) v0 = other.v0;
	if (other.v1 > v1) v1 = other.v1;
	if (other.v2 > v2) v2 = other.v2;
	return this;
}

public Vector3 componentMaxAndSet(double a, double b, double c)
{
	if (a > v0) v0 = a;
	if (b > v1) v1 = b;
	if (c > v2) v2 = c;
	return this;
}

/**
 * Returns a String ( v0, v1, v2 ).
 */
@Override
public String toString() 
{ return "( "+v0+", "+v1+", "+v2+" )"; }

public double squaredDistance(Vector3 other)
{
	double dx = v0-other.v0;
	double dy = v1-other.v1;
	double dz = v2-other.v2;
	return dx*dx + dy*dy + dz*dz;
}

/**
 * Returns the distance between this vector and the given vector.
 */
public double distance(Vector3 other)
{
	return Math.sqrt(squaredDistance(other));
}

/**
 * Returns any vector that is orthogonal to this vector.
 * That is, v.multiply(v.findAnyOrthogonal()) == 0.
 * The vector is normalized before it is returned.
 * @see #normalizeAndSet()
 */
public Vector3 findAnyOrthogonal()
{
	// find the axis that is least colinear to this vector
	Vector3 subdominant = new Vector3();
	if ((Math.abs(v0) < Math.abs(v1)) && (Math.abs(v0) < Math.abs(v2)))
		subdominant.set(1, 0, 0);
	else
	{
		if (Math.abs(v1) < Math.abs(v2))
			subdominant.set(0, 1, 0);
		else
			subdominant.set(0, 0, 1);
	}
	return cross(subdominant).normalizeAndSet();
}

/**
 * Finds tangent and cotangent, interpreting this vector as normal.
 * The vectors are find using a smooth rotation scheme without
 * discontinouties. This is neccessary for anisotropic reflection
 * models.
 * 
 * This one forms x,y,z = t,c,n.
 * 
 * @param tangent The tangent is stored in here.
 * @param cotangent The cotangent is stored in here.
 */
public void findBase(Vector3 tangent, Vector3 cotangent)
{
	double alpha = Math.atan2(getY(), getX());
	double ca = Math.cos(alpha);
	double sa = Math.sin(alpha);
	double cb = getZ();
	double sb = Math.sqrt(1-cb*cb);
	
	tangent.set(cb*ca, cb*sa, -sb);
	cotangent.set(-sa, ca, 0);
}

// This one forms x,y,z = c,t,n. Don't remove yet.
public void findBase2(Vector3 tangent, Vector3 cotangent)
{ 	
	double ct = getY();
	double st = Math.sqrt(1-ct*ct);
	double phi = Math.PI-Math.atan2(getZ(),getX());
	double cp = Math.cos(phi);
	double sp = Math.sin(phi);
	tangent.set(ct*cp, st, -ct*sp);
	cotangent.set(sp, 0, cp);
}

public String serialize()
{ return "("+v0+","+v1+","+v2+")"; }

public Vector3 decode(String value)
{
	Matcher m = SERIAL_PATTERN.matcher(value);
	if (m.matches())
	{
		v0 = Double.parseDouble(m.group(1));
		v1 = Double.parseDouble(m.group(2));
		v2 = Double.parseDouble(m.group(3));
		return this;
	}
	else
		throw new NumberFormatException("Invalid Vector3 \""+value+"\"!");
}

public static void main(String[] args)
{
	Vector3 test = new Vector3();
	test.decode("(0,0,1)");
}

}
