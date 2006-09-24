// arch-tag: 483271cb-a2dc-48d4-87f8-e232d829f73f
package de.yvert.cr.profiles;

import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;
import de.yvert.textures.Color;

/**
 * Common base class for all materials.
 * <br><br>
 * The <code>direction</code>-paramater found in 
 * <code>getWeight</code> and <code>getDirectBRDF</code> is an input parameter which points
 * towards the surface. In a light gathering pass (i.e. typical raytracing), it gives the 
 * direction of incoming light, i.e. a lightsource sample or a gathered photon. However,
 * in a light distribution pass (i.e. photon mapping) it actually gives the direction of the 
 * scattered light. A material does not know about this and still assumes a direction that
 * points towards the surface. The renderer must take of flipping directions correctly.
 * <br><br>
 * The <code>direction</code>-parameter of <code>getWeightedDirection</code> is an output
 * parameter which points away from the surface. It can be used for light gathering and 
 * distribution in exactly the same way.
 *
 * @author Ulf Ochsenfahrt, Stefan Goldmann
 *
 */
public abstract class Material
{

// TODO: Performance optimization.
// material flags, returns an ORed combination of flags.
// If a flag is not returned, the corresponding method is assumed to return the default value.
// Some materials only override some methods - this improves the performance of those materials.
// public abstract int getFlags();

/**
 * The emittance of this object. For physically correct rendering, the emittance must be zero.
 * FIXME: What about area lights?
 * 
 * @param result the incoming ray, the emittance returned should be in the opposite direction
 * @param emittance the resulting emittance
 */
public abstract void getEmittance(IntersectionResult result, Color emittance);

/**
 * Can modify the normal that is calculated from the geometry, e.g. with a normal or bump map.
 * It is valid to do nothing in which case the default normal will be kept.
 * 
 * @param result the incoming ray &amp; normal
 */
public abstract void getNormal(IntersectionResult result);

/**
 * Calculate the value of the BRDF times the cosine between light direction and surface normal.
 * (set result to black, if light comes from inside and surface does not have inner reflection)
 * 
 * @param result gives the incoming ray
 * @param Incoming light direction (points towards surface)
 * @param weight the result
 */
public abstract void getWeight(IntersectionResult result, Vector3 direction, Color weight);

/**
 * Calculate the weight for the mirror ray. The mirror direction is implicitly calculated
 * with the geometry/shading (FIXME: which one?) normal.
 * 
 * @param result gives the incoming ray
 * @param color the result
 */
public abstract void getMirrorWeight(IntersectionResult result, Color color);

/**
 * Calculate the weight for the refracted ray. The refracted ray direction is implicitly
 * calculated with the geometry/shading (FIXME: which one?) normal.
 * 
 * FIXME: index of refraction shouldn't be a color? ior is not documented
 * at this time, only the red channel is used. this _must_ be documented!
 * 
 * @param result gives the incoming ray
 * @param weight the resulting weight
 * @param rior the relative index of refraction
 */
public abstract void getRefractionWeight(IntersectionResult result, Color weight, Color rior);

/**
 * The extinction coefficient for the volume outside this object.
 * In a surface representation, it is impossible to track the extinction coefficient automatically.
 * Surfaces can intersect arbitrarily which makes the coefficient ill-defined.
 * 
 * @param result the incoming ray, can come from either inside or outside
 * @param ext the resulting extinction coefficient
 */
public abstract void getExteriorExtinction(IntersectionResult result, Color ext);

/**
 * The extinction coefficient for the volume inside this object.
 * 
 * @param result the incoming ray, can come from either inside or outside
 * @param ext the resulting extinction coefficient
 */
public abstract void getInteriorExtinction(IntersectionResult result, Color ext);


/**
 * Calculate the value of the BRDF.<p>
 * This method is used mainly for photon mapping (and other global illumination techniques)
 * which must use the value of the BRDF at some points. The default implementation is to call 
 * getWeight and divide by the cosine of the light direction and surface normal. However, this 
 * can lead to numerical problems if the cosine is very small. You should only implement this 
 * method if you anticipate numerical or performance problems.
 * <p>
 * If you do not understand the above paragraph, you should keep your hands away from this function!
 * <p>
 * If the return values of this method disagree with those of
 * {@link #getWeight(IntersectionResult, Vector3, Color)}, the results are undefined.
 * 
 * @param isection gives the incoming ray
 * @param direction Incoming light direction (points towards surface)
 * @param weight the result
 */
public abstract void getDirectBRDF(IntersectionResult isection, Vector3 direction, Color weight);


/**
 * Importance sample the BRDF.<p>
 * Importance sampling is a method to improve the accuracy of sampling a function f (in this case, 
 * the BRDF). It works by sampling not uniformly, but according to a probability distribution 
 * function g. In order to obtain statistically correct results, the resulting function value must 
 * be weighted with 1/g.<p>
 * If the function g is chosen wisely, then the resulting weight function f/g is much simpler than 
 * the original weight function f. Thereby the variance of the integration can be reduced. <p>
 * The best you can achieve is sampling according to BRDF times cosine of outgoing direction. If 
 * this is not possible, try to sample away the part that causes the highest variance.
 * <p> 
 * If you do not understand the above paragraph, you should keep your hands away from this function!
 * <p>
 * If the return values of this method disagree with those of
 * {@link #getWeight(IntersectionResult, Vector3, Color)} or
 * {@link #getDirectBRDF(IntersectionResult, Vector3, Color)}, the results are undefined.
 * 
 * @param isection the incoming light ray
 * @param sample a random vector with independant components, each uniformly distributed between 0 and 1
 * @param direction the importance sampled result direction (points away from surface)
 * @param weight the weight for that direction
 */
public abstract void getWeightedDirection(IntersectionResult isection, Vector2 sample, Vector3 direction, Color weight);

}
