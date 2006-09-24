package de.yvert.camera;

import de.yvert.geometry.Matrix3;
import de.yvert.geometry.Quaternion;
import de.yvert.geometry.Vector3;

public final class FreeCamera extends AbstractCamera
{

private static final long serialVersionUID = 1L;

private Vector3 position = new Vector3();
private Quaternion startRotation = new Quaternion();
private Quaternion addedRotation = new Quaternion();

public FreeCamera()
{/*OK*/}

@Override
public FreeCamera clone()
{
	FreeCamera result = (FreeCamera) super.clone();
	result.position = position.clone();
	result.startRotation = startRotation.clone();
	result.addedRotation = addedRotation.clone();
	return result;
}

@Override
public Vector3 getPosition(Vector3 result)
{
	result.set(position);
	return result;
}

@Override
public Quaternion getRotation(Quaternion result)
{
	result.set(addedRotation).multiplyAndSet(startRotation).normalizeAndSet();
	return result;
}

@Override
public void setPosition(Vector3 position)
{ this.position.set(position); }

@Override
public void setRotation(Quaternion rotation)
{
	if (rotation == null) throw new NullPointerException("rotation may not be null!");
	this.startRotation.set(rotation);
	this.addedRotation.set(Quaternion.ZERO);
}


public void moveForward(double amount)
{
	Vector3 dir = new Vector3(0, 0, -amount);
	Matrix3 rot = getMatrix3(new Matrix3());
	dir.multiplyAndSet(rot);
	position.addAndSet(dir);
}

public void moveBackward(double amount)
{ moveForward(-amount); }

public void moveLeft(double amount)
{
	Vector3 dir = new Vector3(-amount, 0, 0);
	Matrix3 rot = getMatrix3(new Matrix3());
	dir.multiplyAndSet(rot);
	position.addAndSet(dir);
}

public void moveRight(double amount)
{ moveLeft(-amount); }

public void rotateLeft(double amount)
{
	Quaternion rot = new Quaternion(new Vector3(0, 1, 0), -amount/180.0);
	addedRotation.multiplyAndSet(rot);
}

public void rotateUp(double amount)
{
	Quaternion rot = new Quaternion(new Vector3(1, 0, 0), amount/180.0);
	addedRotation.multiplyAndSet(rot);
}

public void rollLeft(double amount)
{
	Quaternion rot = new Quaternion(new Vector3(0, 0, 1), -amount/180.0);
	addedRotation.multiplyAndSet(rot);
}

}
