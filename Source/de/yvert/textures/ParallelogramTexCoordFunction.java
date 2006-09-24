// arch-tag: 79eed16c-3867-4e7b-9e73-b4fc4e910a84
package de.yvert.textures;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.Parallelogram;
import de.yvert.geometry.Vector3;
import de.yvert.geometry.Vector4;

/**
 * Prototype of a texcoord functions for parallelogram.
 * Works currently only for quads. FIXME!
 * 
 * @author Stefan Goldmann
 *
 */
public class ParallelogramTexCoordFunction extends TexCoordFunction
{

private Vector3 v = new Vector3();
private int offset;

public ParallelogramTexCoordFunction(int offset)
{ this.offset = 4*offset; }

public int getOffset()
{ return offset; }

@Override
public void genTexCoords(IntersectionResult result, Vector4 uvstcoords)
{
	Parallelogram p = (Parallelogram) result.item;
	v.set(result.hitpoint).subAndSet(p.a);

	// Simple bilinear interpolation. Works for quads only. FIXME!
	double s = v.multiply(p.u)/p.u.getSquaredLength();
	double t = v.multiply(p.v)/p.v.getSquaredLength();
	
	// Parallelogram vertices are counted counter clockwise, 
	// so this order is correct
	uvstcoords.set(p.texcoordparams[offset+0]).scaleAndSet((1-s)*(1-t));
	uvstcoords.addAndSet(p.texcoordparams[offset+1], s*(1-t));
	uvstcoords.addAndSet(p.texcoordparams[offset+3], (1-s)*t);
	uvstcoords.addAndSet(p.texcoordparams[offset+2], s*t);
}

}
