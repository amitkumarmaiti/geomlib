// arch-tag: 001b6f89-d364-4d75-9f26-613528cdf45c
package de.yvert.cr.profiles;

import de.yvert.cr.stdlib.DirectionMapper;
import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;
import de.yvert.textures.Color;

public class PointLightAdapter extends PointLight
{

@Override
public void getPosition(LightData data, Vector3 result)
{ result.set(0, 0, 0); }

@Override
public void getEmission(LightData data, Vector3 position, Vector3 direction, Color result)
{ result.setToZero(); }

@Override
public double getAttenuation(LightData data, double distance, double squaredDistance)
{ return squaredDistance; }

@Override
public void getPhoton(LightData data, Vector2 uv, Vector3 position, Vector3 direction, Color weight)
{
	DirectionMapper.mapEven(uv,direction);
	getEmission(data, position, direction, weight);
	weight.mul(4*Math.PI); // Converts from Intensity to power
}

}
