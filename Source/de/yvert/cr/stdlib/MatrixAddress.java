// arch-tag: aa9cffc0-3a88-44a6-a1ca-146034d8bdd1
package de.yvert.cr.stdlib;

import de.yvert.geometry.Matrix4;
import de.yvert.geometry.Vector4;

public class MatrixAddress 
{
	
private final Matrix4 parent;
private final String mask;
	
public MatrixAddress(Matrix4 parent, String mask)
{
	this.parent = parent;
	this.mask = mask;
}
		
public float set(float other)
{
	// Remember, a single field access looks like this: mymatrix._m31
	if (mask.length() != 4)
		throw new RuntimeException("Internal Error in generated code!");
	
	// Yes, this is kind of dirty.
	int i = mask.charAt(2)-'0';
	int j = mask.charAt(3)-'0';
	
	parent.setEntry(i,j, other);
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
	double[] vals = other.toArray(); 
	String[] fields = mask.split("_m");
	if ((fields.length < 2) || !"".equals(fields[0]))
		throw new RuntimeException("Internal error in generated code (" + mask + ")!");
	for (int i = 1; i < fields.length; i++)
	{
		String s = fields[i];
		if (s.length() != 2)
			throw new RuntimeException("Internal Error in generated code!");
		
		// Yes, this is kind of dirty.
		int j = s.charAt(0)-'0';
		int k = s.charAt(1)-'0';
		
		parent.setEntry(j,k, vals[i-1]);
	}	
	return other;
}

public int postfixInc()
{
	// Remember, a single field access looks like this: mymatrix._m31
	if (mask.length() != 4)
		throw new RuntimeException("Internal Error in generated code!");
	
	// Yes, this is kind of dirty.
	int i = mask.charAt(2)-'0';
	int j = mask.charAt(3)-'0';
	
	int v = (int) parent.getEntry(i,j);
	parent.setEntry(i,j, v+1);
	return v;
}
	
public int postfixDec()
{
	// Remember, a single field access looks like this: mymatrix._m31
	if (mask.length() != 4)
		throw new RuntimeException("Internal Error in generated code!");
	
	// Yes, this is kind of dirty.
	int i = mask.charAt(2)-'0';
	int j = mask.charAt(3)-'0';
	
	int v = (int) parent.getEntry(i,j);
	parent.setEntry(i,j, v-1);
	return v;
}

public int prefixInc()
{
	// Remember, a single field access looks like this: mymatrix._m31
	if (mask.length() != 4)
		throw new RuntimeException("Internal Error in generated code!");
	
	// Yes, this is kind of dirty.
	int i = mask.charAt(2)-'0';
	int j = mask.charAt(3)-'0';
	
	int v = (int) parent.getEntry(i,j)+1;
	parent.setEntry(i,j, v);
	return v;	
}
	
public int prefixDec()
{
	// Remember, a single field access looks like this: mymatrix._m31
	if (mask.length() != 4)
		throw new RuntimeException("Internal Error in generated code!");
	
	// Yes, this is kind of dirty.
	int i = mask.charAt(2)-'0';
	int j = mask.charAt(3)-'0';
	
	int v = (int) parent.getEntry(i,j)-1;
	parent.setEntry(i,j, v);
	return v;	
}
	
}
