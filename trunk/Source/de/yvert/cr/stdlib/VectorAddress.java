// arch-tag: 2a4026b6-e9f3-41eb-9503-a265cf4c5c51
package de.yvert.cr.stdlib;

import de.yvert.geometry.Vector4;

public class VectorAddress 
{

private final Vector4 parent;
private final String mask;
	
public VectorAddress(Vector4 parent, String mask)
{
	this.parent = parent;
	this.mask = mask;
}
	
public float set(float other)
{
	if (mask.length() != 1)
		throw new RuntimeException("Internal Error in generated code!");
	switch (mask.charAt(0))
	{
		case 'x': parent.setX(other); break;
		case 'y': parent.setY(other); break;
		case 'z': parent.setZ(other); break;
		case 'w': parent.setW(other); break;
		default: throw new RuntimeException("Internal Error in generated code!");
	}
	return other;
}

public boolean set(boolean other)
{
	set(other ? 1f : 0f);
	return other;
}

public int set(int other)
{
	set((float) other);
	return other;
}
	
public Vector4 set(Vector4 other)
{
	if (mask.length() < 2)
		throw new RuntimeException("Internal Error in generated code!");	
	double[] vals = other.toArray();
	for (int i = 0; i < mask.length(); i++)
	{
		switch(mask.charAt(i))
		{
			case 'x' : parent.setX(vals[i]); break;
			case 'y' : parent.setY(vals[i]); break;
			case 'z' : parent.setZ(vals[i]); break;
			case 'w' : parent.setW(vals[i]); break;
			default: throw new RuntimeException("Internal Error in generated code!");	
		}	
	}	
	return other;
}
	
public int postfixInc()
{
	if (mask.length() != 1)
		throw new RuntimeException("Internal Error in generated code!");	
	int v;
	switch(mask.charAt(0))
	{
		case 'x': v = (int) parent.getX(); parent.setX(v+1); break; 
		case 'y': v = (int) parent.getY(); parent.setY(v+1); break;
		case 'z': v = (int) parent.getZ(); parent.setZ(v+1); break;
		case 'w': v = (int) parent.getW(); parent.setW(v+1); break;
		default: throw new RuntimeException("Internal Error in generated code!"); 
	}
	return v;
}
	
public int postfixDec()
{
	if (mask.length() != 1)
		throw new RuntimeException("Internal Error in generated code!");	
	int v;
	switch(mask.charAt(0))
	{
		case 'x': v = (int) parent.getX(); parent.setX(v-1); break; 
		case 'y': v = (int) parent.getY(); parent.setY(v-1); break;
		case 'z': v = (int) parent.getZ(); parent.setZ(v-1); break;
		case 'w': v = (int) parent.getW(); parent.setW(v-1); break;
		default: throw new RuntimeException("Internal Error in generated code!"); 
	}	
	return v;
}

public int prefixInc()
{
	if (mask.length() != 1)
		throw new RuntimeException("Internal Error in generated code!");	
	int v;
	switch(mask.charAt(0))
	{
		case 'x': v = (int) parent.getX()+1; parent.setX(v); break; 
		case 'y': v = (int) parent.getY()+1; parent.setY(v); break;
		case 'z': v = (int) parent.getZ()+1; parent.setZ(v); break;
		case 'w': v = (int) parent.getW()+1; parent.setW(v); break;
		default: throw new RuntimeException("Internal Error in generated code!"); 
	}	
	return v;
}
	
public int prefixDec()
{
	if (mask.length() != 1)
		throw new RuntimeException("Internal Error in generated code!");	
	int v;
	switch(mask.charAt(0))
	{
		case 'x': v = (int) parent.getX()-1; parent.setX(v); break; 
		case 'y': v = (int) parent.getY()-1; parent.setY(v); break;
		case 'z': v = (int) parent.getZ()-1; parent.setZ(v); break;
		case 'w': v = (int) parent.getW()-1; parent.setW(v); break;
		default: throw new RuntimeException("Internal Error in generated code!"); 
	}	
	return v;
}

}
