// arch-tag: c4163ef6-b4d2-4bc0-8263-ef5eb2c35a4f
package de.yvert.geometry;

import java.text.DecimalFormat;
import java.text.FieldPosition;

/**
 * A Matrix3 is a 3-tuple of 3-tuples.
 * <p>
 * When a method has the addon "AndSet", the result is stored in the current
 * object. This usually avoids creating new objects and consequently makes a
 * program faster.
 * 
 * @author Sara Adams
 */
public final class Matrix3 implements Cloneable
{

public static final double EPSILON = 1e-4;

double[][] matrix;

/**
 * Creates a Matrix3 with all entries equal 0.
 */
public Matrix3()
{ matrix = new double[3][3]; }

/**
 * Creates a Matrix3 with entries succeeding row by row.
 */
public Matrix3(double a00, double a01, double a02,
               double a10, double a11, double a12,
               double a20, double a21, double a22)
{
	this();
	matrix[0][0] = a00; matrix[0][1] = a01; matrix[0][2] = a02;
	matrix[1][0] = a10; matrix[1][1] = a11; matrix[1][2] = a12;
	matrix[2][0] = a20; matrix[2][1] = a21; matrix[2][2] = a22;
}

/**
 * Creates a Matrix3 with columns s0, s1, s2.
 */
public Matrix3(Vector3 s0, Vector3 s1, Vector3 s2)
{
	this(s0.getV0(), s1.getV0(), s2.getV0(),
	     s0.getV1(), s1.getV1(), s2.getV1(),
	     s0.getV2(), s1.getV2(), s2.getV2());
}

/**
 * Creates a Matrix3 with all entries as in m.
 */
public Matrix3(Matrix3 m)
{
	this();
	for (int i = 0; i < 3; i++)
		for (int j = 0; j < 3; j++)
			matrix[i][j] = m.matrix[i][j];
}

/**
 * Creates a Matrix3 corresponding to Quaternion Q.
 * Prints a warning, if length of Quaternion is not 1 (+- 0.0.1).
 */
public Matrix3(Quaternion Q)
{ this(); set(Q); }

@Override
public Object clone() throws CloneNotSupportedException
{
	Matrix3 result = (Matrix3) super.clone();
	result.matrix = matrix.clone();
	return result;
}

/**
 * Returns matrix[x][y]. Throws an exception if x or y is not in {0,1,2}.
 */
public double getEntry(int x, int y)
{ return matrix[x][y]; }

/**
 * Returns Vector3 (matrix[y][0], matrix[y][1], matrix[y][2]).
 * Throws an exception if y is not in {0,1,2}.
 */
public Vector3 getColumn(int y)
{ return new Vector3(matrix[y][0], matrix[y][1], matrix[y][2]); }

/**
 * Returns a double[3][3] containing all entries, the first [] representing the
 * rows, the second [] representing the columns.
 */
public double[][] getMatrix()
{ return matrix; }

/**
 * Sets matrix[x][y] to newEntry. Throws exception if x or y is not in  {0,1,2}.
 */
public Matrix3 setEntry(int x, int y, double newEntry) 
{
	matrix[x][y] = newEntry;
	return this;
}

/**
 * Sets matrix[][y] to v. Throws exception if y is not in  {0,1,2}.
 */
public Matrix3 setColumn(int y, Vector3 v)
{
	matrix[0][y] = v.v0;
	matrix[1][y] = v.v1;
	matrix[2][y] = v.v2;
	return this;
}

/**
 * Sets Matrix4 to Matrix4 with same entries as m.
 */ 
public Matrix3 set(Matrix3 m)
{
	for (int i = 0; i < 3; i++)
		for (int j = 0; j < 3; j++)
			matrix[i][j] = m.matrix[i][j];
	return this;
}

/**
 * Sets Matrix3 to Matrix3 correspoding to Quaternion Q.
 * Prints a warning, if length of Quaternion is not 1 (+- 0.01).
 */
public Matrix3 set(Quaternion Q)
{
	if ((Q.getLength() < 0.99) || (Q.getLength() > 1.01))
		System.out.println("Quaternion does not have unit length, getMatrix() is probably incorrect!"); 
	matrix[0][0] = 1 - 2*Q.y*Q.y - 2*Q.z*Q.z;
	matrix[0][1] = 2*Q.x*Q.y - 2*Q.w*Q.z;
	matrix[0][2] = 2*Q.x*Q.z + 2*Q.w*Q.y;
	matrix[1][0] = 2*Q.x*Q.y + 2*Q.w*Q.z;
	matrix[1][1] = 1 - 2*Q.x*Q.x - 2*Q.z*Q.z;
	matrix[1][2] = 2*Q.y*Q.z - 2*Q.w*Q.x;
	matrix[2][0] = 2*Q.x*Q.z - 2*Q.w*Q.y;
	matrix[2][1] = 2*Q.y*Q.z + 2*Q.w*Q.x;
	matrix[2][2] = 1 - 2*Q.x*Q.x - 2*Q.y*Q.y;
	return this;
}

/**
 * Returns a Matrix3 scaled by d
 */
public Matrix3 scale(double d)
{ return new Matrix3(this).scaleAndSet(d); }

/**
 * Same as scale(double d), but stores the result in the current Matrix3.
 */
public Matrix3 scaleAndSet(double d)
{
	matrix[0][0] *= d; matrix[0][1] *= d; matrix[0][2] *= d;
	matrix[1][0] *= d; matrix[1][1] *= d; matrix[1][2] *= d;
	matrix[2][0] *= d; matrix[2][1] *= d; matrix[2][2] *= d;
	return this;
}

/**
 * Returns the sum of two Matrix3.
 */
public Matrix3 add(Matrix3 m)
{ return new Matrix3(this).addAndSet(m); }

/**
 * Same as add(Matrix3 m), but stores the result in the current Matrix3.
 */
public Matrix3 addAndSet(Matrix3 m)
{
	for (int i = 0; i < 3; i++)
		for (int j = 0; j < 3; j++)
			matrix[i][j] += m.matrix[i][j];
	return this;
}

/**
 * Returns the product of two Matrix3.
 */
public Matrix3 multiply(Matrix3 m)
{
	Matrix3 result = new Matrix3();
	for (int i = 0; i < 3; i++)
	{
		for (int j = 0; j < 3; j++)
			result.matrix[i][j] = matrix[i][0]*m.matrix[0][j] +
			                      matrix[i][1]*m.matrix[1][j] +
			                      matrix[i][2]*m.matrix[2][j];
	}
	return result;
}

/**
 * Same as multiply(Matrix3 m), but stores the result in the current Matrix3.
 */
public Matrix3 multiplyAndSet(Matrix3 m)
{ return set(multiply(m)); }

/**
 * Returns a quaternion if possible.
 */
public Quaternion getQuaternion()
{
	/*
	matrix[0][0] = 1 - 2*y*y - 2*z*z;
	matrix[0][1] = 2*x*y - 2*w*z;
	matrix[0][2] = 2*x*z + 2*w*y;
	matrix[1][0] = 2*x*y + 2*w*z;
	matrix[1][1] = 1 - 2*x*x - 2*z*z;
	matrix[1][2] = 2*y*z - 2*w*x;
	matrix[2][0] = 2*x*z - 2*w*y;
	matrix[2][1] = 2*y*z + 2*w*x;
	matrix[2][2] = 1 - 2*x*x - 2*y*y;
	*/
	
	double T = 1+matrix[0][0]+matrix[1][1]+matrix[2][2];
	if (T > 1e-8d)
	{
		double S = 2*Math.sqrt(T);
		double qx = (matrix[2][1] - matrix[1][2]) / S;
    double qy = (matrix[0][2] - matrix[2][0]) / S;
    double qz = (matrix[1][0] - matrix[0][1]) / S;
    double qw = S / 4;
    return new Quaternion(qw, qx, qy, qz);
	}
	else
	{
		if (matrix[0][0] > matrix[1][1] && matrix[0][0] > matrix[2][2])
		{ // Column 0: 
			double S  = 2*Math.sqrt(1 + matrix[0][0] - matrix[1][1] - matrix[2][2]);
			double qx = S / 4;
			double qy = (matrix[1][0] + matrix[0][1]) / S;
			double qz = (matrix[0][2] + matrix[2][0]) / S;
			double qw = (matrix[2][1] - matrix[1][2]) / S;
			return new Quaternion(qw, qx, qy, qz);
		}
		else if (matrix[1][1] > matrix[2][2])
		{ // Column 1: 
			double S  = 2*Math.sqrt(1 + matrix[1][1] - matrix[0][0] - matrix[2][2]);
			double qx = (matrix[1][0] + matrix[0][1]) / S;
			double qy = 0.25 * S;
			double qz = (matrix[2][1] + matrix[1][2]) / S;
			double qw = (matrix[0][2] - matrix[2][0]) / S;
			return new Quaternion(qw, qx, qy, qz);
		}
		else
		{ // Column 2:
			double S  = 2*Math.sqrt(1 + matrix[2][2] - matrix[0][0] - matrix[1][1]);
			double qx = (matrix[0][2] + matrix[2][0]) / S;
			double qy = (matrix[2][1] + matrix[1][2]) / S;
			double qz = 0.25 * S;
			double qw = (matrix[1][0] - matrix[0][1]) / S;
			return new Quaternion(qw, qx, qy, qz);
		}
	}
	
/*	double qx = ( matrix[0][0] - matrix[1][1] - matrix[2][2] + 1) / 4.0;
	double qy = (-matrix[0][0] + matrix[1][1] - matrix[2][2] + 1) / 4.0;
	double qz = (-matrix[0][0] - matrix[1][1] + matrix[2][2] + 1) / 4.0;
	double qw = ( matrix[0][0] + matrix[1][1] + matrix[2][2] + 1) / 4.0;
	
	if ((qx < -EPSILON) || (qy < -EPSILON) || (qz < -EPSILON) || (qw < -EPSILON))
		throw new RuntimeException("Not a rotation matrix!");
	
	if (qx < 0) qx = 0;
	if (qy < 0) qy = 0;
	if (qz < 0) qz = 0;
	if (qw < 0) qw = 0;
	
	qx = Math.sqrt(qx);
	qy = Math.sqrt(qy);
	qz = Math.sqrt(qz);
	qw = Math.sqrt(qw);
	
	double qxqw = (matrix[2][1] - matrix[1][2]) / 4.0;
	double qyqw = (matrix[0][2] - matrix[2][0]) / 4.0;
	double qzqw = (matrix[1][0] - matrix[0][1]) / 4.0;
	
	double temp = Math.abs(qxqw) - qx*qw;
	if ((temp > EPSILON) || (temp < -EPSILON))
		throw new RuntimeException("Not a rotation matrix!");
	temp = Math.abs(qyqw) - qy*qw;
	if ((temp > EPSILON) || (temp < -EPSILON))
		throw new RuntimeException("Not a rotation matrix!");
	temp = Math.abs(qzqw) - qz*qw;
	if ((temp > EPSILON) || (temp < -EPSILON))
		throw new RuntimeException("Not a rotation matrix!");
	
	if (qxqw < 0) qx = -qx;
	if (qyqw < 0) qy = -qy;
	if (qzqw < 0) qz = -qz;
	
	return new Quaternion(qw, qx, qy, qz);*/
}

/**
 * Sets all entries.
 */
public void set(double a00, double a01, double a02,
                double a10, double a11, double a12,
                double a20, double a21, double a22)
{
	matrix[0][0] = a00; matrix[0][1] = a01; matrix[0][2] = a02;
	matrix[1][0] = a10; matrix[1][1] = a11; matrix[1][2] = a12;
	matrix[2][0] = a20; matrix[2][1] = a21; matrix[2][2] = a22;
}

/**
 * Prints Matrix3.
 */
public void out()
{
	for (int i = 0; i < 3; i++)
	{
		for (int j = 0; j < 3; j++)
			System.out.print(matrix[i][j] + "  ");
		System.out.println();
	}
}

static DecimalFormat fmt = new DecimalFormat(" ###0.00000000;-#");

@Override
public String toString()
{
	StringBuffer result = new StringBuffer();
	for (int i = 0; i < 3; i++)
	{
		result.append("[ ");
		for (int j = 0; j < 3; j++)
		{
			fmt.format(matrix[i][j], result, new FieldPosition(0));
			result.append(' ');
		}
		result.append("]\n");
	}
	return result.toString();
}

}
