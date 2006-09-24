package de.yvert.camera;

import de.yvert.geometry.Quaternion;
import de.yvert.geometry.Vector3;

public final class TargetCamera extends AbstractCamera
{

private static final long serialVersionUID = 1L;

private Vector3 goal = new Vector3();
private double phi, tau;
private double radius;

public TargetCamera(Vector3 goal, double radius)
{
	this.goal.set(goal);
	this.radius = radius;
}

@Override
public TargetCamera clone()
{
	TargetCamera result = (TargetCamera) super.clone();
	result.goal = goal.clone();
	return result;
}

public double getDistance()
{ return radius; }

public void setDistance(double dist)
{ radius = dist; }

@Override
public Vector3 getPosition(Vector3 result)
{
	double dx = -Math.sin(phi)*Math.sin(tau)*radius;
	double dy = -Math.sin(phi)*Math.cos(tau)*radius;
	double dz = Math.cos(phi)*radius;
	result.set(goal);
	result.addAndSet(new Vector3(dx, dy, dz));
	return result;
}

@Override
public Quaternion getRotation(Quaternion result)
{
	Quaternion temp1 = new Quaternion(Vector3.X, phi);
	Quaternion temp2 = new Quaternion(Vector3.Z, -tau);
	result.set(temp1).multiplyAndSet(temp2).normalizeAndSet();
	return result;
}

@Override
public void setPosition(Vector3 position)
{ throw new UnsupportedOperationException(); }

@Override
public void setRotation(Quaternion rotation)
{ throw new UnsupportedOperationException(); }


public void zoomIn(double amount)
{ radius -= amount; }

public void rotateLeft(double amount)
{ tau += Math.toRadians(amount); }

public void rotateUp(double amount)
{
	phi -= Math.toRadians(amount);
//	if (phi < 0) phi = 0;
}


}
