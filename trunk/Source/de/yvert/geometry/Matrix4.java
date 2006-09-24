// arch-tag: 73ac22a1-bc16-4dd6-aabb-0044a3babb14
package de.yvert.geometry;

/**
 * A Matrix4 is a 4-tuple of 4-tuples.
 * <p>
 * The matrix is stored in row order. The first value of the pair (x,y) gives the row.
 * <pre>
 * 0 1 2 3
 * 4 5 6 7
 * 8 9 A B
 * C D E F
 * </pre>
 * <p>
 * When a method has the addon "AndSet", the result is stored in the current
 * object. This avoids creating new objects and consequently makes a program
 * faster.
 * 
 * @author Sara Adams
 */
public final class Matrix4 implements Cloneable
{

double[][] matrix;

/**
 * Creates a Matrix4 with all entries equal 0.
 */
public Matrix4()
{ matrix = new double[4][4]; }

/**
 * Creates a Matrix4 with entries succeeding row by row.
 */
public Matrix4(double a00, double a01, double a02, double a03,
               double a10, double a11, double a12, double a13,
               double a20, double a21, double a22, double a23,
               double a30, double a31, double a32, double a33)
{
	this();
	matrix[0][0]=a00; matrix[0][1]=a01; matrix[0][2]=a02; matrix[0][3]=a03;
	matrix[1][0]=a10; matrix[1][1]=a11; matrix[1][2]=a12; matrix[1][3]=a13;
	matrix[2][0]=a20; matrix[2][1]=a21; matrix[2][2]=a22; matrix[2][3]=a23;
	matrix[3][0]=a30; matrix[3][1]=a31; matrix[3][2]=a32; matrix[3][3]=a33;
}

/**
 * Creates a Matrix4 with columns s0, s1, s2, s3.
 */
public Matrix4(Vector4 s0, Vector4 s1, Vector4 s2, Vector4 s3)
{
	this(s0.getV0(), s1.getV0(), s2.getV0(), s3.getV0(),
	     s0.getV1(), s1.getV1(), s2.getV1(), s3.getV1(),
	     s0.getV2(), s1.getV2(), s2.getV2(), s3.getV2(),
	     s0.getV3(), s1.getV3(), s2.getV3(), s3.getV3());
}

/**
 * Creates a Matrix4 with same entries as Matrix3, rest = 0 except
 * matrix[3][3] = 1.
 */
public Matrix4(Matrix3 m)
{
	this();
	set(m);
}

/**
 * Creates a Matrix4 with all entries as in m.
 */
public Matrix4(Matrix4 m)
{
	this();
	set(m);
}

/**
 * Creates a Matrix3 corresponding to Quaternion Q.
 * Prints a warning, if length of Quaternion is not 1 (+- 0.0.1).
 */
public Matrix4(Quaternion Q)
{
	this();
	set(Q);
}

@Override
public Object clone() throws CloneNotSupportedException
{
	Matrix4 result = (Matrix4) super.clone();
	result.matrix = new double[4][4];
	result.set(this);
	return result;
}

/**
 * Creates a Matrix to transform objects into their reflection on plane.
 */
public static Matrix4 createReflectionMatrix(Matrix4 in, Vector4 plane)
{
	// Note: This looks like the Householder reflection matrix.
	double x = plane.getV0();
	double y = plane.getV1();
	double z = plane.getV2();
	double w = plane.getV3();
	
	in.setEntry(0, 0, 1-2*x*x);
	in.setEntry(0, 1, -2*x*y);
	in.setEntry(0, 2, -2*x*z);
	in.setEntry(0, 3, -2*x*w);
	
	in.setEntry(1, 0, -2*x*y);
	in.setEntry(1, 1, 1-2*y*y);
	in.setEntry(1, 2, -2*y*z);
	in.setEntry(1, 3, -2*y*w);
	
	in.setEntry(2, 0, -2*x*z);
	in.setEntry(2, 1, -2*y*z);
	in.setEntry(2, 2, 1-2*z*z);
	in.setEntry(2, 3, -2*z*w);
	
	in.setEntry(3, 0, 0);
	in.setEntry(3, 1, 0);
	in.setEntry(3, 2, 0);
	in.setEntry(3, 3, 1);
	
	return in;
}

/**
 * Returns matrix[x][y].
 * Throws an exception if x or y is not in {0,1,2,3}.
 */
public double getEntry(int x, int y)
{ return matrix[x][y]; }

/**
 * Returns Vector4 (matrix[y][0], matrix[y][1], matrix[y][2], matrix[y][3]).
 * Throws an exception if y is not in {0,1,2,3}.
 */
public Vector4 getRow(int y)
{ return new Vector4(matrix[y][0], matrix[y][1], matrix[y][2], matrix[y][3]); }

/**
 * Returns a double[4][4] containing all entries, the first [] representing the
 * rows, the second [] representing the columns.
 */
public double[][] getMatrix()
{ return matrix; }

public Matrix4 reset()
{
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			matrix[i][j] = (i == j) ? 1 : 0;
	return this;
}

/**
 * Sets matrix[x][y] to newEntry. Throws exception if x or y is not in
 * {0,1,2,3}.
 */
public Matrix4 setEntry(int x, int y, double newEntry)
{
	matrix[x][y] = newEntry;
	return this;
}

/**
 * Sets matrix[][y] to v. Throws exception if y is not in  {0,1,2,3}.
 */
public Matrix4 setColumn(int y, Vector4 v)
{
	matrix[0][y] = v.v0; matrix[1][y] = v.v1; 
	matrix[2][y] = v.v2; matrix[3][y] = v.v3;
	return this;
}

/**
 * Sets Matrix4 to Matrix4 with same entries as Matrix3 m, rest =0 except
 * matrix[3][3]=1.
 */
public Matrix4 set(Matrix3 m)
{
	for (int i = 0; i < 3; i++)
		for (int j = 0; j < 3; j++)
			matrix[i][j]=m.matrix[i][j];
	matrix[3][0] = 0; matrix[3][1] = 0; matrix[3][2] = 0;
	matrix[0][3] = 0; matrix[1][3] = 0; matrix[2][3] = 0;
	matrix[3][3] = 1;
	return this;
}

/**
 * Sets Matrix4 to Matrix4 with same entries as m.
 * Prints a warning, if length of Quaternion is not 1 (+- 0.01).
 */
public Matrix4 set(Matrix4 m)
{
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			matrix[i][j] = m.matrix[i][j];
	return this;
}

/**
 * Sets Matrix4 to Matrix3 correspoding to Quaternion Q, rest = 0 except
 * matrix[3][3] = 1.
 */
public Matrix4 set(Quaternion Q)
{
	if ((Q.getLength()<0.99)||(Q.getLength()>1.01)) 
		System.out.println("Quaternion does not have length 1, getMatrix() is probably incorrect!");
	matrix[0][0] = 1-2*Q.y*Q.y-2*Q.z*Q.z;
	matrix[0][1] = 2*Q.x*Q.y-2*Q.w*Q.z;
	matrix[0][2] = 2*Q.x*Q.z+2*Q.w*Q.y;
	matrix[1][0] = 2*Q.x*Q.y+2*Q.w*Q.z;
	matrix[1][1] = 1-2*Q.x*Q.x-2*Q.z*Q.z;
	matrix[1][2] = 2*Q.y*Q.z-2*Q.w*Q.x;
	matrix[2][0] = 2*Q.x*Q.z-2*Q.w*Q.y;
	matrix[2][1] = 2*Q.y*Q.z+2*Q.w*Q.x;
	matrix[2][2] = 1-2*Q.x*Q.x-2*Q.y*Q.y;
	matrix[3][0] = 0; matrix[3][1] = 0; matrix[3][2] = 0;
	matrix[0][3] = 0; matrix[1][3] = 0; matrix[2][3] = 0;
	matrix[3][3] = 1;
	return this;
}

/**
 * Returns a Matrix4 scaled by d
 */
public Matrix4 scale(double d)
{ return new Matrix4(this).scaleAndSet(d); }

/**
 * Same as scale(double d), but stores the result in the current Matrix4.
 */
public Matrix4 scaleAndSet(double d)
{
	matrix[0][0]*=d; matrix[0][1]*=d; matrix[0][2]*=d; matrix[0][3]*=d;
	matrix[1][0]*=d; matrix[1][1]*=d; matrix[1][2]*=d; matrix[1][3]*=d;
	matrix[2][0]*=d; matrix[2][1]*=d; matrix[2][2]*=d; matrix[2][3]*=d;
	matrix[3][0]*=d; matrix[3][1]*=d; matrix[3][2]*=d; matrix[3][3]*=d;
	return this;
}

/**
 * Transposes the matrix
 */
public Matrix4 transpose(Matrix4 in)
{
	for (int j=0;j<4;j++)
	for (int i=0;i<4;i++)
	{
		in.matrix[i][j] = matrix[j][i];
	}
	
	return in;
}

/**
 * Transpose the matrix
 */
public Matrix4 transpose()
{ 
	return transpose(new Matrix4()); 
}

/**
 * Returns the sum of two Matrix4.
 */
public Matrix4 add(Matrix4 m)
{ return new Matrix4(this).addAndSet(m); }

/**
 * Same as add(Matrix4 m), but stores the result in the current Matrix4.
 */
public Matrix4 addAndSet(Matrix4 m)
{
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			matrix[i][j] += m.matrix[i][j];
	return this;
}

/**
 * Returns the product of two Matrix4.
 */
public Matrix4 multiply(Matrix4 m)
{ return new Matrix4(this).multiplyAndSet(m); }

/**
 * Same as multiply(Matrix4 m), but stores the result in the current Matrix4.
 */
public Matrix4 multiplyAndSet(Matrix4 m)
{
	double[][] result = new double[4][4];
	for (int i = 0; i < 4; i++)
	{
		for (int j = 0; j < 4; j++)
		{
			result[i][j] = matrix[i][0]*m.matrix[0][j]+
			               matrix[i][1]*m.matrix[1][j]+
			               matrix[i][2]*m.matrix[2][j]+
			               matrix[i][3]*m.matrix[3][j];
		}
	}
	
	this.matrix = result;
	return this;
}

public Matrix4 multiplyLeftAndSet(Matrix4 m)
{
	double[][] result = new double[4][4];
	for (int i = 0; i < 4; i++)
	{
		for (int j = 0; j < 4; j++)
		{
			result[i][j] = m.matrix[i][0]*matrix[0][j]+
						   m.matrix[i][1]*matrix[1][j]+
						   m.matrix[i][2]*matrix[2][j]+
						   m.matrix[i][3]*matrix[3][j];
		}
	}
	
	this.matrix = result;
	return this;
}

/**
 * Prints this Matrix4.
 */
public void out()
{
	System.out.println(toString());
	
	/*	for (int i = 0; i < 4; i++)
	{
		for (int j = 0; j < 4; j++)
			System.out.print(matrix[i][j] + "  ");
		System.out.println();
	}*/
}

@Override
public String toString()
{
	StringBuffer out = new StringBuffer();
	
	for (int i = 0; i < 4; i++)
	{
		for (int j = 0; j < 4; j++)
			out.append(matrix[i][j] + "  ");
		out.append("\n");
	}
	
	return out.toString();
}

}
