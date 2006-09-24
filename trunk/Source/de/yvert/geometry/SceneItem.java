// arch-tag: 2ae5cb84-eefa-485b-a38c-02ba105fb8ca
package de.yvert.geometry;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.cr.profiles.Material;
import de.yvert.textures.TexCoordFunction;
import de.yvert.textures.Texture;

/**
 * A <code>SceneItem</code> extends a {@link SceneObject} by certain 
 * functions which are necessary for raytracing.
 * 
 * @author Ulf Ochsenfahrt
 */
public abstract class SceneItem extends SceneObject
{

protected Material material;
protected Texture[] textures;
protected TexCoordFunction[] texfunctions;

public final Material getMaterial()
{ return material; }

public final void setMaterial(Material material)
{ this.material = material; }

public Texture[] getTextures()
{ return textures; }

public void setTextures(Texture[] textures)
{ this.textures = textures; }

public TexCoordFunction[] getTexCoordFunctions()
{ return texfunctions; }

public void setTexCoordFunctions(TexCoordFunction[] texfunctions)
{ this.texfunctions = texfunctions; }

public abstract void getNormal(IntersectionResult result, Vector3 geonormal);

public abstract double getMinX();
public abstract double getMaxX();
public abstract double getMinY();
public abstract double getMaxY();
public abstract double getMinZ();
public abstract double getMaxZ();

public BoundingBox getBoundingBox()
{
	Vector3 min = new Vector3(getMinX(), getMinY(), getMinZ());
	Vector3 max = new Vector3(getMaxX(), getMaxY(), getMaxZ());
	return new BoundingBox(min, max);
}

public abstract BoundingSphere getBoundingSphere();
// distance at which the ray hits this object or -1, if the ray does not hit this object
public abstract double distance(Ray ray);

}
