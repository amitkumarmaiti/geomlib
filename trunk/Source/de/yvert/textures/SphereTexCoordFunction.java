// arch-tag: 2f05c369-dc57-44de-a0eb-96c8d830cccf
package de.yvert.textures;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.Sphere;
import de.yvert.geometry.Vector4;

public class SphereTexCoordFunction extends TexCoordFunction
{

@Override
public void genTexCoords(IntersectionResult result, Vector4 uvstcoords)
{
	Sphere s = (Sphere) result.item;
	double x = result.hitpoint.getX()-s.getCenter().getX();
	double y = result.hitpoint.getY()-s.getCenter().getY();
	double z = result.hitpoint.getZ()-s.getCenter().getZ();
	
	double r = s.getRadius();
	double phi = Math.PI+Math.atan2(-x, -z);
	double theta = Math.acos(y/r);
	uvstcoords.setX(phi/(2*Math.PI));
	uvstcoords.setY(theta/Math.PI);
}

}
