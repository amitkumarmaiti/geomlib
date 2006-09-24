// arch-tag: 2301208a-5b72-4db3-a411-3a56ea4af7b5
package de.yvert.geometry;

import java.util.regex.*;

/**
 * A quaternion q is a number of the shape q=w+x*i+y*j+z*k, where
 * i*i=j*j=k*k=-1, i*j=-j*i=k, j*k=-k*j=i, k*i=-i*k=j and w,x,y,z are real
 * numbers. Quaternions can be used to write rotations of matrices more
 * compactly. For pure rotations the length of the Quaternion has to be
 * normalized, i.e. the length must be 1. <br>
 * When a method has the addon "AndSet", the result is stored in the current
 * object. This avoids creating new objects and consequently makes a program
 * faster.
 * 
 * @author Sara Adams
 * @author Ulf Ochsenfahrt
 */
public final class Quaternion implements Cloneable
{

private static final String DOUBLE_PATTERN =
	"(?:\\+|\\-)?(?:NaN|Infinity|\\d+(?:\\.\\d*)?(?:(?:e|E)(?:\\+|\\-)?\\d+)?)";
private static final Pattern SERIAL_PATTERN = Pattern.compile(
	"\\(("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+")\\)");

public static final Quaternion ZERO = new Quaternion(1, 0, 0, 0);

double w, x, y, z;

/**
 * Creates a Quaternion describing the identity rotation.
 */
public Quaternion()
{ this(1, 0, 0, 0); }

/**
 * Creates a Quaternion q=w+x*i+y*j+z*k
 */
public Quaternion(double w, double x, double y, double z)
{
	this.w = w; this.x = x; this.y = y; this.z = z;
}

/**
 * Creates a Quaternion with the same value as Q
 */
public Quaternion(Quaternion Q)
{ this(Q.w, Q.x, Q.y, Q.z); }

/**
 * Creates a Quaternion q=w+x*i+y*j+z*k, where v=(w,x,y,z) and w,x,y,z are
 * double.
 */
public Quaternion(Vector4 v)
{ this(v.v0, v.v1, v.v2, v.v3); }

/**
 * Creates a Quaternion
 * q=sin(angle/2)+cos(angle/2)*x*i+cos(angle/2)*y*j+cos(angle/2)*z*k,
 * where v=(x,y,z) and angle,x,y,z are double. The created Quaternion has 
 * length 1. The result is a rotation around the given axis by the given angle.
 */
public Quaternion(Vector3 v, double angle)
{
	double h = Math.sin(angle/2); 
	w = Math.cos(angle/2); x = h*v.v0; y = h*v.v1; z = h*v.v2;
}


@Override
public Quaternion clone()
{
	try
	{ return (Quaternion) super.clone(); }
	catch (CloneNotSupportedException e)
	{ throw new UnsupportedOperationException(e); }
}

public double getW()
{ return w; }

public double getX()
{ return x; }

public double getY()
{ return y; }

public double getZ()
{ return z; }

public Vector4 getVector4()
{ return new Vector4(w,x,y,z); }

/**
 * Returns the length of the Quaternion.
 */
public double getLength()
{ return Math.sqrt(w*w+x*x+y*y+z*z); }

public Quaternion set(Quaternion other)
{
	this.w = other.w;
	this.x = other.x;
	this.y = other.y;
	this.z = other.z;
	return this;
}

/**
 * Returns the corresponding Matrix3 to the Quaternion if the length of the
 * Quaternion is 1. Prints a warning if length is not 1 (+- 0.01).
 */
public Matrix3 getMatrix3()
{
	if ((getLength() < 0.99) || (getLength() > 1.01)) 
		System.err.println("WARNING: Quaternion does not have length 1, getMatrix3() is probably incorrect!");
	return new Matrix3().set(this);
}

/** 
 * Returns the Matrix4 with the same entries as the Matrix3 corresponding to 
 * the Quaternion if the length of the Quaternion is 1. Other entries of 
 * Matrix4 = 0, except matrix[3][3] = 1.
 * Prints a warning, if length of Quanternion is not 1 (+- 0.01).
 */ 
public Matrix4 getMatrix4()
{
	if ((getLength() < 0.99) || (getLength() > 1.01))
		System.err.println("WARNING: Quaternion does not have length 1, getMatrix4() is probably incorrect!");
	return new Matrix4().set(this);
}

/**
 * Returns a String w+x*i+y*j+z*k
 */
@Override
public String toString()
{ return w+" + "+x+"*i + "+y+"*j + "+z+"*k";}

/**
 * Returns a Quaternion with length 1
 */
public Quaternion normalize()
{ return new Quaternion(this).normalizeAndSet(); }

public Quaternion normalizeAndSet()
{ return this.scaleAndSet(1/getLength()); }

/**
 * Returns true only if w=x=y=z=0
 */
public boolean equalsZero()
{
	if ((w != 0) || (x != 0) || (y != 0) || (z != 0))
		return false;
	return true;
}

/**
 * Returns -(w+x*i+y*j+z*k)
 */
public Quaternion negate()
{ return new Quaternion(-w,-x,-y,-x); }

public Quaternion negateAndSet()
{
	w=-w; x=-x; y=-y; z=-z;
	return this;
}

/**
 * Returns (w+x*i+y*j+z*k)*(w+x*i+y*j+z*k)=(w+x*i+y*j+z*k)*(w-x*i-y*j-z*k)=a+0*i+0*j+0*k, 
 * where a is double. The squareroute of square() is the length of the Quaternion.
 */
public Quaternion square()
{ return this.multiply(this); }

public Quaternion squareAndSet()
{ return this.multiplyAndSet(this); }

/**
 * Returns w-x*i-y*j-z*k.
 */
public Quaternion conjugate()  
{ return new Quaternion(w, -x, -y, -z); }
public Quaternion conjugateAndSet()
{ 
	x=-x; y=-y; z=-z;
	return this;
}

/**
 * Returns the sum of two Quaternions.
 */
public Quaternion add(Quaternion Q)
{ return new Quaternion(this).addAndSet(Q); }

public Quaternion addAndSet(Quaternion Q)
{
	w += Q.w; x += Q.x; y += Q.y; z += Q.z;
	return this;
}

public Quaternion sub(Quaternion Q)
{ return new Quaternion(this).subAndSet(Q); }

public Quaternion subAndSet(Quaternion Q)
{
	w -= Q.w; x -= Q.x; y -= Q.y; z -= Q.z;
	return this;
}

/**
 * Returns a Quaternions scaled by d.
 */
public Quaternion scale(double d)
{
	return new Quaternion(w*d, x*d, y*d, z*d);
}

public Quaternion scaleAndSet(double d)
{
	w *= d; x *= d; y *= d; z *= d;
	return this;
}

/**
 * Returns the product of two Quaternions.
 * <p>
 * If Q1, Q2 are normalized Quaternions, Q1 and Q2 resemble rotations and
 * Q1.multiply(Q2) is the rotation you get when first rotating by Q2 and then
 * rotating by Q1.
 */
public Quaternion multiply(Quaternion Q)
{ return new Quaternion(this).multiplyAndSet(Q); }

/**
 * Same as multiply(Quaternion Q), but stores the result in the current
 * Quaternion.
 */
public Quaternion multiplyAndSet(Quaternion Q)
{
	double tw = this.w, tx = this.x, ty = this.y, tz = this.z;
	this.w = Q.w*tw - Q.x*tx - Q.y*ty - Q.z*tz;
	this.x = Q.w*tx + Q.x*tw + Q.y*tz - Q.z*ty;
	this.y = Q.w*ty - Q.x*tz + Q.y*tw + Q.z*tx;
	this.z = Q.w*tz + Q.x*ty - Q.y*tx + Q.z*tw;
	return this;
}

/**
 * Returns the multiplicative inverse of a Quaternions, that is not equal to
 * zero.
 * @throws ArithmeticException
 *    if the current Quaternion is zero
 */
public Quaternion invert()
{
	if (this.equalsZero()) throw new ArithmeticException();
	return (this.conjugate()).scaleAndSet(1/(w*w+x*x+y*y+z*z));
}

public Quaternion invertAndSet()
{
	if (equalsZero()) throw new ArithmeticException();
	double length = getLength();
	conjugateAndSet().scaleAndSet(1/length);
	return this;
}

public String serialize()
{ return "("+w+","+x+","+y+","+z+")"; }

public Quaternion decode(String value)
{
	Matcher m = SERIAL_PATTERN.matcher(value);
	if (m.matches())
	{
		w = Double.parseDouble(m.group(1));
		x = Double.parseDouble(m.group(2));
		y = Double.parseDouble(m.group(3));
		z = Double.parseDouble(m.group(4));
		return this;
	}
	else
		throw new NumberFormatException("Invalid Vector3 \""+value+"\"!");
}

}
