// arch-tag: 44684736-403b-4cf6-8924-d21e892afdb6
package de.yvert.cr.profiles;

import de.yvert.geometry.*;
import de.yvert.textures.TexCoordFunction;
import de.yvert.textures.Texture;

/**
 * Container class for raytracing.
 * 
 * @author Ulf Ochsenfahrt, Stefan Goldmann
 */
public class IntersectionResult
{

public int time = 0;
public int x,y;
public int stat0, stat1;

public Ray ray = new Ray();
public Vector3 hitpoint = new Vector3();

public Vector3 normal = new Vector3();
public Vector3 tangent = new Vector3();
public Vector3 cotangent = new Vector3();

public Vector3 shadingNormal = new Vector3();
public Vector3 shadingTangent = new Vector3();
public Vector3 shadingCotangent = new Vector3();
public Vector3 reflectv = new Vector3(); // Reflection vector according to shading normal

// Hit distance, the object that has been hit, it's textures and it's texture coordinates
public double distance;
public SceneItem item = null;
public Texture[] textures = null;
public Vector4[] uvstcoords = new Vector4[8];

public IntersectionResult()
{
	for (int i = 0; i < uvstcoords.length; i++)
		uvstcoords[i] = new Vector4();
}

public void update()
{
	// calculate hit point
	hitpoint.set(ray.v).scaleAndSet(distance).addAndSet(ray.p);
	
	// calculate texture coordinates
	textures = item.getTextures();
	if (textures != null)
	{
		TexCoordFunction[] functions = item.getTexCoordFunctions();
		for (int i = 0; i < textures.length; i++)
			functions[i].genTexCoords(this, uvstcoords[i]);
	}
	
	// calculate geometry normal - this is fixed for any object
	item.getNormal(this, normal);

	// calculate tangent and cotangent to geometry normal
	normal.findBase(tangent, cotangent);
	
	// This must be done in case the material does not modify the normal.
	shadingNormal.set(normal);
		
	// get the shading normal from the material, 
	// use it to calculate shading tangent and cotangent
	item.getMaterial().getNormal(this);
	shadingNormal.findBase(shadingTangent, shadingCotangent);
	
	// calculate reflected vector according to shading normal
	double scale = ray.v.multiply(shadingNormal);
	reflectv.set(ray.v).addAndSet(shadingNormal, -2*scale);
	reflectv.normalizeAndSet();
}

}
