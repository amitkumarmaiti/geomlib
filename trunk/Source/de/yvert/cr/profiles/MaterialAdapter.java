// arch-tag: 8e6ad129-ca17-482c-989a-2add28b943b6
package de.yvert.cr.profiles;

import de.yvert.cr.stdlib.DirectionMapper;
import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;
import de.yvert.textures.Color;

public class MaterialAdapter extends Material
{

@Override
public void getEmittance(IntersectionResult result, Color emittance)
{ emittance.set(Color.BLACK); }

@Override 
public void getNormal(IntersectionResult result)
{
	// Nothing to be done here. Shading normal has been set to normal already by result.update().
}

@Override
public void getWeight(IntersectionResult result, Vector3 direction, Color weight)
{ weight.set(Color.BLACK); }

@Override
public void getMirrorWeight(IntersectionResult result, Color weight)
{ weight.set(Color.BLACK); }

@Override 
public void getRefractionWeight(IntersectionResult result, Color weight, Color rior)
{ weight.set(Color.BLACK); }

@Override 
public void getExteriorExtinction(IntersectionResult result, Color ext)
{ ext.set(Color.WHITE); }

@Override 
public void getInteriorExtinction(IntersectionResult result, Color ext)
{ ext.set(Color.WHITE); }

/**
 * getDirectBRDF implemented in terms of getWeight.<p>
 * NOTE: You should only override this method if you have read the documentation for
 * {@link Material#getDirectBRDF(IntersectionResult, Vector3, Color)} and understand
 * what you are doing.
 */
@Override
public void getDirectBRDF(IntersectionResult result, Vector3 direction, Color weight)
{
	// As direction points towards surface, we need to flip the sign
	// FIXME: shouldn't this be cos = Math.abs(...) ? Yes, Ulf. This is correct.
	getWeight(result, direction, weight);
	double cos = Math.abs(direction.multiply(result.normal));
	weight.mul(1/cos);
}

/**
 * getWeightedDirection implemented as uniform sampling.<p>
 * NOTE: You should only override this method if you have read the documentation for
 * {@link Material#getWeightedDirection(IntersectionResult, Vector2, Vector3, Color)}
 * and understand what you are doing.
 */
@Override
public void getWeightedDirection(IntersectionResult isection, Vector2 sample, Vector3 direction, Color weight)
{
	// FIXME: Most materials wont have transmission. For these 
	// materials, this function wastes half the samples.
	DirectionMapper.mapEven(sample,direction);
	getWeight(isection, direction, weight);
	weight.mul(4*Math.PI);
}

}
