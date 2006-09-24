// arch-tag: 20f9bf2d-01b5-427f-a632-b0fc976cfa25
package de.yvert.cr.stdlib;

import de.yvert.geometry.Vector4;

public class VectorHelper 
{
	
//	 bool-to-float conversion
private static float b2f(boolean b)
{ return b ? 1 : 0; }
	
public static Vector4 create(float f)
{ return new Vector4(f,f,f,f); }

public static Vector4 create(float f1, float f2)
{ return new Vector4(f1,f2,0,0); }

public static Vector4 create(float f1, float f2, float f3)
{ return new Vector4(f1,f2,f3,0); }

public static Vector4 create(float f1, float f2, float f3, float f4)
{ return new Vector4(f1,f2,f3,f4); }
	
public static Vector4 create(boolean b)
{ return create(b2f(b)); }

public static Vector4 create(boolean b1, boolean b2)
{ return new Vector4(b2f(b1),b2f(b2),0,0); }

public static Vector4 create(boolean b1, boolean b2, boolean b3)
{ return new Vector4(b2f(b1),b2f(b2),b2f(b3),0); }

public static Vector4 create(boolean b1, boolean b2, boolean b3, boolean b4)
{ return new Vector4(b2f(b1),b2f(b2),b2f(b3),b2f(b4)); }

public static Vector4 set(Vector4 src, float f)
{
	src.setX(f);
	src.setY(f);
	src.setZ(f);
	src.setW(f);
	return src;
}

public static Vector4 set(Vector4 src, Number n)
{ return set(src, n.floatValue()); }

public static Vector4 set(Vector4 src, boolean b)
{ return set(src, b2f(b)); }

public static Vector4 set(Vector4 src, Boolean b)
{ return set(src, b.booleanValue()); }

public static Vector4 unaryPlus(Vector4 src)
{ return new Vector4(src); }

public static Vector4 unaryMinus(Vector4 src)
{ return new Vector4(src).scaleAndSet(-1); }

public static Vector4 unaryNot(Vector4 src)
{
	int x = ~((int) src.getX());
	int y = ~((int) src.getY());
	int z = ~((int) src.getZ());
	int w = ~((int) src.getW());
	return new Vector4(x,y,z,w);
}

public static Vector4 unaryLogicNot(Vector4 src)
{
	double x = (src.getX() != 0) ? 0 : 1;
	double y = (src.getY() != 0) ? 0 : 1;
	double z = (src.getZ() != 0) ? 0 : 1;
	double w = (src.getW() != 0) ? 0 : 1;
	return new Vector4(x,y,z,w);
}

public static Vector4 add(Vector4 a, Vector4 b)
{ return a.add(b); }

public static Vector4 sub(Vector4 a, Vector4 b)
{ return a.sub(b); }

public static Vector4 mul(Vector4 a, Vector4 b)
{ return a.mul(b); }

public static Vector4 div(Vector4 a, Vector4 b)
{ return a.div(b); }

public static float getX(Vector4 src)
{ return (float) src.getX(); }

public static float getY(Vector4 src)
{ return (float) src.getY(); }

public static float getZ(Vector4 src)
{ return (float) src.getZ(); }

public static float getW(Vector4 src)
{ return (float) src.getW(); }

public static boolean getBoolX(Vector4 src)
{ return (src.getX() != 0); }

public static boolean getBoolY(Vector4 src)
{ return (src.getY() != 0); }

public static boolean getBoolZ(Vector4 src)
{ return (src.getZ() != 0); }

public static boolean getBoolW(Vector4 src)
{ return (src.getW() != 0); }

public static Vector4 get(Vector4 src, String mask)
{	
	if (mask.length() > 4) 
		throw new RuntimeException("Internal Error in generated code!");	
	
	double[] vals = new double[4];
	for (int i = 0; i < mask.length(); i++)
	{
		switch (mask.charAt(i))
		{
			case 'x': vals[i] = src.getX(); break;
			case 'y': vals[i] = src.getY(); break;
			case 'z': vals[i] = src.getZ(); break;
			case 'w': vals[i] = src.getW(); break;
			default: throw new RuntimeException("Internal Error in generated code!");
		}
	}
	
	return new Vector4(vals[0], vals[1], vals[2], vals[3]);
}

public static boolean boolShrink(Vector4 src, int dim)
{
	boolean result = true;
	result &= (dim > 0) ? (src.getX()!=0) : true;
	result &= (dim > 1) ? (src.getY()!=0) : true;
	result &= (dim > 2) ? (src.getZ()!=0) : true;
	result &= (dim > 3) ? (src.getW()!=0) : true;	
	return result;
}

public static boolean boolShrink2(Vector4 src)
{ return boolShrink(src, 2); }

public static boolean boolShrink3(Vector4 src)
{ return boolShrink(src, 3); }

public static boolean boolShrink4(Vector4 src)
{ return boolShrink(src, 4); }

public static Vector4 conditional(Vector4 condition, Vector4 a, Vector4 b)
{
	Vector4 res = new Vector4();
	res.setX((condition.getX() != 0) ? a.getX() : b.getX());
	res.setY((condition.getY() != 0) ? a.getY() : b.getY());
	res.setZ((condition.getZ() != 0) ? a.getZ() : b.getZ());
	res.setW((condition.getW() != 0) ? a.getW() : b.getW());
	return res;
}

public static Vector4 logicAnd(Vector4 a, Vector4 b)
{
	double x = getBoolX(a)&&getBoolX(b) ? 1 : 0;
	double y = getBoolY(a)&&getBoolY(b) ? 1 : 0;
	double z = getBoolZ(a)&&getBoolZ(b) ? 1 : 0;
	double w = getBoolW(a)&&getBoolW(b) ? 1 : 0;
	return new Vector4(x,y,z,w);
}

public static Vector4 logicOr(Vector4 a, Vector4 b)
{
	double x = getBoolX(a)||getBoolX(b) ? 1 : 0;
	double y = getBoolY(a)||getBoolY(b) ? 1 : 0;
	double z = getBoolZ(a)||getBoolZ(b) ? 1 : 0;
	double w = getBoolW(a)||getBoolW(b) ? 1 : 0;
	return new Vector4(x,y,z,w);
}

public static Vector4 and(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() & (int) b.getX();
	int y = (int) a.getY() & (int) b.getY();
	int z = (int) a.getZ() & (int) b.getZ();
	int w = (int) a.getW() & (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 or(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() | (int) b.getX();
	int y = (int) a.getY() | (int) b.getY();
	int z = (int) a.getZ() | (int) b.getZ();
	int w = (int) a.getW() | (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 xor(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() ^ (int) b.getX();
	int y = (int) a.getY() ^ (int) b.getY();
	int z = (int) a.getZ() ^ (int) b.getZ();
	int w = (int) a.getW() ^ (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 shl(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() << (int) b.getX();
	int y = (int) a.getY() << (int) b.getY();
	int z = (int) a.getZ() << (int) b.getZ();
	int w = (int) a.getW() << (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 shr(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() >> (int) b.getX();
	int y = (int) a.getY() >> (int) b.getY();
	int z = (int) a.getZ() >> (int) b.getZ();
	int w = (int) a.getW() >> (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 mod(Vector4 a, Vector4 b)
{
	int x = (int) a.getX() % (int) b.getX();
	int y = (int) a.getY() % (int) b.getY();
	int z = (int) a.getZ() % (int) b.getZ();
	int w = (int) a.getW() % (int) b.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 castToIntVector(Vector4 src)
{
	int x = (int) src.getX();
	int y = (int) src.getY();
	int z = (int) src.getZ();
	int w = (int) src.getW();
	return new Vector4(x,y,z,w);
}

public static Vector4 equal(Vector4 a, Vector4 b)
{
	boolean x = a.getX() == b.getX();
	boolean y = a.getY() == b.getY();
	boolean z = a.getZ() == b.getZ();
	boolean w = a.getW() == b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

public static Vector4 notEqual(Vector4 a, Vector4 b)
{
	boolean x = a.getX() != b.getX();
	boolean y = a.getY() != b.getY();
	boolean z = a.getZ() != b.getZ();
	boolean w = a.getW() != b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

public static Vector4 less(Vector4 a, Vector4 b)
{
	boolean x = a.getX() < b.getX();
	boolean y = a.getY() < b.getY();
	boolean z = a.getZ() < b.getZ();
	boolean w = a.getW() < b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

public static Vector4 lessEqual(Vector4 a, Vector4 b)
{
	boolean x = a.getX() <= b.getX();
	boolean y = a.getY() <= b.getY();
	boolean z = a.getZ() <= b.getZ();
	boolean w = a.getW() <= b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

public static Vector4 greater(Vector4 a, Vector4 b)
{
	boolean x = a.getX() > b.getX();
	boolean y = a.getY() > b.getY();
	boolean z = a.getZ() > b.getZ();
	boolean w = a.getW() > b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

public static Vector4 greaterEqual(Vector4 a, Vector4 b)
{
	boolean x = a.getX() >= b.getX();
	boolean y = a.getY() >= b.getY();
	boolean z = a.getZ() >= b.getZ();
	boolean w = a.getW() >= b.getW();
	return new Vector4(b2f(x),b2f(y),b2f(z),b2f(w));
}

}
