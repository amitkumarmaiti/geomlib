//arch-tag: 7fde99a8-beac-4153-82ff-85057293c1b0
package de.yvert.cr.stdlib;

import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;

/**
 * A helper class for mapping uv-coordinates to directions accordings to some
 * standard propability distribution functions. It does also contain some
 * functions for mapping local to global coordinates and vice versa.
 * 
 * @author Stefan Goldmann
 *
 */
public class DirectionMapper
{

/**
 * Maps the uv-coordinate to a point on the unit sphere evenly. The uv coordinate
 * is a uniformly distributed sample in [0,1]x[0,1]. 
 * @param uv The uv-coordinate
 * @param result The result, a point on the unit sphere.
 */
public static void mapEven(Vector2 uv, Vector3 result)
{
	double cosAlpha = 1-uv.getV0()*2;
	double sinAlpha = Math.sqrt(1-cosAlpha*cosAlpha);
	double beta = 2*uv.getV1();
	result.setY(cosAlpha);
	result.setX(Math.cos(beta)*sinAlpha);
	result.setZ(Math.sin(beta)*sinAlpha);
}

// TODO: Add missing functions

}
