// arch-tag: e6e2f15c-77dc-4f15-a4df-17a6cd283f6f
package de.yvert.cr.stdlib;

import de.yvert.geometry.Matrix4;
import de.yvert.geometry.Vector4;

public class MatrixHelper 
{
	
//	 bool-to-float conversion
private static float b2f(boolean b)
{ return b ? 1 : 0; }

public static Matrix4 create(float f)
{ return new Matrix4(f,f,f,f, f,f,f,f, f,f,f,f, f,f,f,f); }

public static Matrix4 create(float f1, float f2, float f3, float f4)
{ return new Matrix4(f1,f2,0,0, f3,f4,0,0, 0,0,0,0, 0,0,0,0); }

public static Matrix4 create(float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9)
{ return new Matrix4(f1,f2,f3,0, f4,f5,f6,0, f7,f8,f9,0, 0,0,0,0); }
	
public static Matrix4 create(float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16)
{ return new Matrix4(f1,f2,f3,f4, f5,f6,f7,f8, f9,f10,f11,f12, f13,f14,f15,f16); }

public static Matrix4 create(boolean b)
{ return create(b2f(b)); }

public static Matrix4 create(boolean b1, boolean b2, boolean b3, boolean b4)
{ return new Matrix4(b2f(b1),b2f(b2),0,0, b2f(b3),b2f(b4),0,0, 0,0,0,0, 0,0,0,0); }

public static Matrix4 create(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8, boolean b9)
{ return new Matrix4(b2f(b1),b2f(b2),b2f(b3),0, b2f(b4),b2f(b5),b2f(b6),0, b2f(b7),b2f(b8),b2f(b9),0, 0,0,0,0); }

public static Matrix4 create(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8, boolean b9, boolean b10, boolean b11, boolean b12, boolean b13, boolean b14, boolean b15, boolean b16)
{ return new Matrix4(b2f(b1),b2f(b2),b2f(b3),b2f(b4), b2f(b5),b2f(b6),b2f(b7),b2f(b8), b2f(b9),b2f(b10),b2f(b11),b2f(b12), b2f(b13),b2f(b14),b2f(b15),b2f(b16)); }

public static Matrix4 set(Matrix4 src, float f)
{
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			src.setEntry(i,j, f);
	return src;
}

public static Matrix4 set(Matrix4 src, Number n)
{ return set(src, n.floatValue()); }

public static Matrix4 set(Matrix4 src, boolean b)
{ return set(src, b2f(b)); }

public static Matrix4 set(Matrix4 src, Boolean b)
{ return set(src, b.booleanValue()); }

public static Matrix4 add(Matrix4 a, Matrix4 b)
{ return a.add(b); }

public static Matrix4 sub(Matrix4 a, Matrix4 b)
{ 
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, a.getEntry(i,j)-b.getEntry(i,j));
	return res;
}

public static Matrix4 mul(Matrix4 a, Matrix4 b)
{ 
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, a.getEntry(i,j)*b.getEntry(i,j));
	return res;
}

public static Matrix4 div(Matrix4 a, Matrix4 b)
{ 
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, a.getEntry(i,j)/b.getEntry(i,j));
	return res;
}

public static Matrix4 mod(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) % (int)b.getEntry(i,j));
	return res;
}

public static Matrix4 shl(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) << (int)b.getEntry(i,j));
	return res;
}

public static Matrix4 shr(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) >> (int)b.getEntry(i,j));
	return res;
}

public static Matrix4 and(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) & (int)b.getEntry(i,j));
	return res;
}

public static Matrix4 or(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) | (int)b.getEntry(i,j));
	return res;
}

public static Matrix4 xor(Matrix4 a, Matrix4 b)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int)a.getEntry(i,j) ^ (int)b.getEntry(i,j));
	return res;
}

public static float get_m00(Matrix4 src)
{ return (float) src.getEntry(0,0); }

public static float get_m01(Matrix4 src)
{ return (float) src.getEntry(0,1); }

public static float get_m02(Matrix4 src)
{ return (float) src.getEntry(0,2); }

public static float get_m03(Matrix4 src)
{ return (float) src.getEntry(0,3); }

public static float get_m10(Matrix4 src)
{ return (float) src.getEntry(1,0); }

public static float get_m11(Matrix4 src)
{ return (float) src.getEntry(1,1); }

public static float get_m12(Matrix4 src)
{ return (float) src.getEntry(1,2); }

public static float get_m13(Matrix4 src)
{ return (float) src.getEntry(1,3); }

public static float get_m20(Matrix4 src)
{ return (float) src.getEntry(2,0); }

public static float get_m21(Matrix4 src)
{ return (float) src.getEntry(2,1); }

public static float get_m22(Matrix4 src)
{ return (float) src.getEntry(2,2); }

public static float get_m23(Matrix4 src)
{ return (float) src.getEntry(2,3); }

public static float get_m30(Matrix4 src)
{ return (float) src.getEntry(3,0); }

public static float get_m31(Matrix4 src)
{ return (float) src.getEntry(3,1); }

public static float get_m32(Matrix4 src)
{ return (float) src.getEntry(3,2); }

public static float get_m33(Matrix4 src)
{ return (float) src.getEntry(3,3); }

public static boolean getBool_m00(Matrix4 src)
{ return (src.getEntry(0,0) != 0); }

public static boolean getBool_m01(Matrix4 src)
{ return (src.getEntry(0,1) != 0); }

public static boolean getBool_m02(Matrix4 src)
{ return (src.getEntry(0,2) != 0); }

public static boolean getBool_m03(Matrix4 src)
{ return (src.getEntry(0,3) != 0); }

public static boolean getBool_m10(Matrix4 src)
{ return (src.getEntry(1,0) != 0); }

public static boolean getBool_m11(Matrix4 src)
{ return (src.getEntry(1,1) != 0); }

public static boolean getBool_m12(Matrix4 src)
{ return (src.getEntry(1,2) != 0); }

public static boolean getBool_m13(Matrix4 src)
{ return (src.getEntry(1,3) != 0); }

public static boolean getBool_m20(Matrix4 src)
{ return (src.getEntry(2,0) != 0); }

public static boolean getBool_m21(Matrix4 src)
{ return (src.getEntry(2,1) != 0); }

public static boolean getBool_m22(Matrix4 src)
{ return (src.getEntry(2,2) != 0); }

public static boolean getBool_m23(Matrix4 src)
{ return (src.getEntry(2,3) != 0); }

public static boolean getBool_m30(Matrix4 src)
{ return (src.getEntry(3,0) != 0); }

public static boolean getBool_m31(Matrix4 src)
{ return (src.getEntry(3,1) != 0); }

public static boolean getBool_m32(Matrix4 src)
{ return (src.getEntry(3,2) != 0); }

public static boolean getBool_m33(Matrix4 src)
{ return (src.getEntry(3,3) != 0); }

public static Vector4 get(Matrix4 src, String mask)
{	
	float[] values = new float[4];
	String[] fields = mask.split("_m");
	if ((fields.length < 2) || !"".equals(fields[0]))
		throw new RuntimeException("Internal error in generated code (" + mask + ")!");
	for (int i = 1; i < fields.length; i++)
	{
		String s = fields[i];
		if (s.length() != 2)
			throw new RuntimeException("Internal Error in generated code(" + s + ")");
		// Yes, this is kind of dirty.
		int j = s.charAt(0)-'0';
		int k = s.charAt(1)-'0';
		values[i-1] = (float) src.getEntry(j,k);
	}	
	return new Vector4(values);
}	

public static boolean boolShrink(Matrix4 src, int dim)
{
	boolean res = true;
	for (int i = 0; i < dim; i++)
		for (int j = 0; j < dim; j++)
			res &= (src.getEntry(i,j) != 0);
	return res;
}

public static boolean boolShrink2(Matrix4 src)
{ return boolShrink(src, 2); }

public static boolean boolShrink3(Matrix4 src)
{ return boolShrink(src, 3); }

public static boolean boolShrink4(Matrix4 src)
{ return boolShrink(src, 4); }

public static Matrix4 castToIntMatrix(Matrix4 src)
{
	Matrix4 res = new Matrix4();
	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++)
			res.setEntry(i,j, (int) src.getEntry(i,j));
	return res;
}


// FIXME: Move these functions into MathHelper (update SLC-Code then)

public static Vector4 mul(Matrix4 m, Vector4 v, int dim)
{
	float[] data = new float[4];
	double[] vals = v.toArray();
	for (int i = 0; i < dim; i++)
		for (int j = 0; j < dim; j++)
			data[i] += vals[j]*m.getEntry(i,j);
	return new Vector4(data);
}

public static Vector4 mul2(Matrix4 m, Vector4 v)
{ return mul(m,v,2); }

public static Vector4 mul3(Matrix4 m, Vector4 v)
{ return mul(m,v,3); }

public static Vector4 mul4(Matrix4 m, Vector4 v)
{ return mul(m,v,4); }


public static Vector4 mul(Vector4 v, Matrix4 m, int dim)
{
	float[] data = new float[4];
	double[] vals = v.toArray();
	for (int i = 0; i < dim; i++)
		for (int j = 0; j < dim; j++)
			data[i] += vals[j]*m.getEntry(j,i);
	return new Vector4(data);
}

public static Vector4 mul2(Vector4 v, Matrix4 m)
{ return mul(v,m,2); }

public static Vector4 mul3(Vector4 v, Matrix4 m)
{ return mul(v,m,3); }

public static Vector4 mul4(Vector4 v, Matrix4 m)
{ return mul(v,m,4); }

}
