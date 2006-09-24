// arch-tag: 4eb1a5de-ee19-4972-9c12-0fd95bb05dd8
package de.yvert.cr.profiles;

import de.yvert.geometry.Vector3;
import de.yvert.geometry.Vector4;
import de.yvert.textures.Color;
import de.yvert.textures.Texture;

// FIXME: very hackish
public class AreaLight
{

private final Vector3 p0 = new Vector3();
private final Vector3 p1 = new Vector3();
private final Vector3 p2 = new Vector3();
private final Vector3 p3 = new Vector3();
private final Color color = new Color();

// this should be a rectangular area
// p0    p1
// +-----+
// |     |
// +-----+
// p2    p3
public AreaLight(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3, Color color)
{
	this.p0.set(p0);
	this.p1.set(p1);
	this.p2.set(p2);
	this.p3.set(p3);
	this.color.set(color);
}

public Texture[] getTextures()
{ return new Texture[0]; }

public void getPhoton(LightData data, Vector4 uv, Vector3 position, Vector3 direction, Color weight)
{
	double u = uv.getX();
	double v = uv.getY();
	position.set(0, 0, 0);
	position.addAndSet(p0, (1-u)*(1-v));
	position.addAndSet(p1, u*(1-v));
	position.addAndSet(p2, (1-u)*v);
	position.addAndSet(p3, u*v);
	
	// FIXME: set direction from remaining two parameters
	
	weight.set(color);
}

}
