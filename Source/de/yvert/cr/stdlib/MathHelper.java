// arch-tag: 20ed9556-75b5-48f1-baf0-e0b50ff1473f
package de.yvert.cr.stdlib;

import de.yvert.geometry.Vector4;

public class MathHelper
{

public static Vector4 sqrt2(Vector4 v)
{ return new Vector4(Math.sqrt(v.getX()), Math.sqrt(v.getY()), 0,0); }

public static Vector4 sqrt3(Vector4 v)
{ return new Vector4(Math.sqrt(v.getX()), Math.sqrt(v.getY()), Math.sqrt(v.getZ()), 0); }

public static Vector4 sqrt4(Vector4 v)
{ return new Vector4(Math.sqrt(v.getX()), Math.sqrt(v.getY()), Math.sqrt(v.getZ()), Math.sqrt(v.getW())); }



public static float rsqrt(float v)
{ return (float) (1/Math.sqrt(v)); }

public static Vector4 rsqrt2(Vector4 v)
{ return new Vector4(rsqrt((float) v.getX()), rsqrt((float) v.getY()), 0,0); }

public static Vector4 rsqrt3(Vector4 v)
{ return new Vector4(rsqrt((float) v.getX()), rsqrt((float) v.getY()), rsqrt((float) v.getZ()), 0); }

public static Vector4 rsqrt4(Vector4 v)
{ return new Vector4(rsqrt((float) v.getX()), rsqrt((float) v.getY()), rsqrt((float) v.getZ()), rsqrt((float) v.getW())); }



public static float rcp(float v)
{ return 1/v; }

public static Vector4 rcp2(Vector4 v)
{ return new Vector4(rcp((float) v.getX()), rcp((float) v.getY()), 0,0); }

public static Vector4 rcp3(Vector4 v)
{ return new Vector4(rcp((float) v.getX()), rcp((float) v.getY()), rcp((float) v.getZ()), 0); }

public static Vector4 rcp4(Vector4 v)
{ return new Vector4(rcp((float) v.getX()), rcp((float) v.getY()), rcp((float) v.getZ()), rcp((float) v.getW())); }



public static Vector4 noise2()
{ return new Vector4(Math.random(), Math.random(), 0,0); }

public static Vector4 noise3()
{ return new Vector4(Math.random(), Math.random(), Math.random(), 0); }

public static Vector4 noise4()
{ return new Vector4(Math.random(), Math.random(), Math.random(), Math.random()); }



public static Vector4 min2(Vector4 a, Vector4 b)
{ return new Vector4(Math.min(a.getX(),b.getX()), Math.min(a.getY(),b.getY()), 0,0); }

public static Vector4 min3(Vector4 a, Vector4 b)
{ return new Vector4(Math.min(a.getX(),b.getX()), Math.min(a.getY(),b.getY()), Math.min(a.getZ(),b.getZ()), 0); }

public static Vector4 min4(Vector4 a, Vector4 b)
{ return new Vector4(Math.min(a.getX(),b.getX()), Math.min(a.getY(),b.getY()), Math.min(a.getZ(),b.getZ()), Math.min(a.getW(),b.getW())); }



public static Vector4 max2(Vector4 a, Vector4 b)
{ return new Vector4(Math.max(a.getX(),b.getX()), Math.max(a.getY(),b.getY()), 0,0); }

public static Vector4 max3(Vector4 a, Vector4 b)
{ return new Vector4(Math.max(a.getX(),b.getX()), Math.max(a.getY(),b.getY()), Math.max(a.getZ(),b.getZ()), 0); }

public static Vector4 max4(Vector4 a, Vector4 b)
{ return new Vector4(Math.max(a.getX(),b.getX()), Math.max(a.getY(),b.getY()), Math.max(a.getZ(),b.getZ()), Math.max(a.getW(),b.getW())); }



public static float frac(float v)
{ return Math.abs(v - (int) v); }



public static float clamp(float min, float max, float val)
{
	float _min = Math.min(min,max);
	float _max = Math.max(min,max);
	return Math.max(Math.min(val, _max), _min);
}
	
}
