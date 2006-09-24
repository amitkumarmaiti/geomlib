// arch-tag: 03857894-ca5c-4623-9f6f-bf8a6a5044c0
package de.yvert.cr.profiles;

import de.yvert.cr.CrProgram;
import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;
import de.yvert.geometry.Vector4;
import de.yvert.textures.Color;
import de.yvert.textures.Texture;

/**
 * The interface for point lights. Actually it would be nice to have a common
 * base class for point and area lights. However, we haven't found a good way 
 * for this, yet.
 * 
 * @author Ulf Ochsenfahrt, Stefan Goldmann
 */
public abstract class PointLight
{

// This one should actually be bound to the light source geometry. 
// As point lights do not have extra geometry it's bound to the 
// light source directly.
private Texture[] textures = new Texture[0];

public void setTextures(Texture[] textures)
{
	if (textures == null) 
		throw new NullPointerException();
	this.textures = textures;
}

public Texture[] getTextures()
{ return textures; }

/**
 * Returns the position for the current time.
 * @param data Contains current time and also the textures of this light source.
 * @param result Write the position into this one.
 */
public abstract void getPosition(LightData data, Vector3 result);

/**
 * Returns the emission value (to speak in terms of radiometry: the intensity) 
 * the given direction and current time.
 * @param data Contains the current time and textures of this light source.
 * @param position The current position, as returned by <code>getPosition</code>
 * @param direction Emission direction (points away from light source) 
 * @param result Write the emission value into this one.
 */
public abstract void getEmission(LightData data, Vector3 position, Vector3 direction, Color result);

/**
 * Returns the attenuation behaviour of this light source. Physical lights return 
 * the <code>squaredDistance</code> here.
 * @param data Contains the current time and textures of this light source.
 * @param distance A distance parameter.
 * @param squaredDistance As distance, but squared.
 * @return Return how this light source attenuates light that travels this distance.
 */
public abstract double getAttenuation(LightData data, double distance, double squaredDistance);

/**
 * Returns a photon direction and weight (in terms of radiometry: power). The light source must do 
 * all weight calculations except division by the number of emitted photons.
 * @param data Contains the current time and textures of this light source.
 * @param uv An evenly distributed number from [0,1]x[0,1] which must be mapped to a weighted direction.
 * @param position The current position, as returned by <code>getPosition</code>
 * @param direction Write the photon direction into this one.
 * @param weight Write the photon power into this one.
 */
public abstract void getPhoton(LightData data, Vector2 uv, Vector3 position, Vector3 direction, Color weight);

// FIXME: Deprecate! (Only used in OpenGLWalkthrough)
public void setPosition(Vector3 position)
{
	// FIXME: This might fail!
	((CrProgram) this).setUniform("position", new Vector4(position));
}

}
