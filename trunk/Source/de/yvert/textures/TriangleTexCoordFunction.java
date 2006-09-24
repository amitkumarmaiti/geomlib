// arch-tag: a697c29a-18c8-42c9-9486-1772e8331332
package de.yvert.textures;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.Triangle;
import de.yvert.geometry.Vector4;

public class TriangleTexCoordFunction extends TexCoordFunction
{

private final int offset;

public TriangleTexCoordFunction(int offset)
{ this.offset = 3*offset; }

public int getOffset()
{ return offset; }

@Override
public void genTexCoords(IntersectionResult result, Vector4 uvstcoords)
{
	Triangle t = (Triangle) result.item;
	t.barycentricCoords(result.ray, uvstcoords);
//	System.out.println(t.texcoordparams[offset+0]+" "+t.texcoordparams[offset+1]+" "+t.texcoordparams[offset+2]);
	uvstcoords.multiplyAndSet(t.texcoordparams[offset+0], t.texcoordparams[offset+1], t.texcoordparams[offset+2]);
	
	double x = uvstcoords.getX();
	double y = uvstcoords.getY();
	x -= Math.floor(x);
	y -= Math.floor(y);
	uvstcoords.set(x, y, 0, 0);
}

}
