package de.yvert.cr.profiles;

/**
 * A <code>NormalMap</code> is used to vary the geometrical normal.
 *
 * @author Stefan Goldmann
 */
public abstract class NormalMap 
{

/**
 * Can modify the normal that is calculated from the geometry, e.g. with a normal or bump map.
 * It is valid to do nothing in which case the default normal will be kept.
 * 
 * @param result the incoming ray &amp; normal
 */
public abstract void getNormal(IntersectionResult result);

}
