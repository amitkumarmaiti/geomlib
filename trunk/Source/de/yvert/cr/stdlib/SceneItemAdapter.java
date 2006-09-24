// arch-tag: ebc2ef0d-704c-46e8-944f-6622418a93cd
package de.yvert.cr.stdlib;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.*;

public class SceneItemAdapter extends SceneItem 
{

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal) 
{ 
	throw new RuntimeException("Internal Error (codgen should have forced existance of this method)!");
}

@Override
public double distance(Ray ray) 
{
	throw new RuntimeException("Internal Error (codgen should have forced existance of this method)!");
}

@Override
public BoundingBox getBoundingBox()
{
	throw new RuntimeException("Internal Error (codgen should have forced existance of this method)!");
}

@Override
public BoundingSphere getBoundingSphere() 
{
	BoundingBox bb = getBoundingBox();
	BoundingSphere bs = new BoundingSphere(bb.center, bb.max.sub(bb.min).getLength()/2);
	return bs;
}

@Override
public double getMinX() 
{ return getBoundingBox().min.getX(); }

@Override
public double getMaxX() 
{ return getBoundingBox().max.getX(); }

@Override
public double getMinY() 
{ return getBoundingBox().min.getY(); }

@Override
public double getMaxY() 
{ return getBoundingBox().max.getY(); }

@Override
public double getMinZ() 
{ return getBoundingBox().min.getZ(); }

@Override
public double getMaxZ() 
{ return getBoundingBox().max.getZ(); }

@Override
public void triangulate(Triangulation tri) 
{
	throw new UnsupportedOperationException("Not supported yet!");
}

}
