package de.yvert.camera;

import de.yvert.geometry.Matrix3;
import de.yvert.geometry.Matrix4;
import de.yvert.geometry.Quaternion;
import de.yvert.geometry.Vector3;

/**
 * Describes an abstract camera.
 * Position and rotation are left abstract for implementors.
 * 
 * You must provide getPosition, setPosition, getRotation and setRotation.
 * You may optionally override getInverseRotation. If you do not override
 * getInverseRotation, it is implicitly calculated from getRotation.
 * 
 * @author Ulf Ochsenfahrt
 */
public abstract class AbstractCamera implements Camera
{

private static final long serialVersionUID = 1L;

private int width = 640;
private int height = 480;
private double aspectRatio = (double) width/(double) height;
private double nearPlane = 0.1;
private double farPlane = 10000.0;
private double fieldOfViewY = 60.0;
private double fieldOfViewX = CameraTools.calcFieldOfViewX(fieldOfViewY, aspectRatio);

protected AbstractCamera()
{/*OK*/}

@Override
public Camera clone()
{
	try
	{ return (Camera) super.clone(); }
	catch (CloneNotSupportedException e)
	{ throw new UnsupportedOperationException(e); }
}

protected final void copyAll(Camera camera)
{
	width = camera.getWidth();
	height = camera.getHeight();
	aspectRatio = camera.getAspectRatio();
	nearPlane = camera.getNearPlane();
	farPlane = camera.getFarPlane();
	fieldOfViewX = camera.getFieldOfViewX();
	fieldOfViewY = camera.getFieldOfViewY();
}

public int getWidth()
{ return width; }

public int getHeight()
{ return height; }

public double getAspectRatio()
{ return aspectRatio; }

public double getNearPlane()
{ return nearPlane; }

public double getFarPlane()
{ return farPlane; }

public double getFieldOfViewY()
{ return fieldOfViewY; }

public double getFieldOfViewX()
{ return fieldOfViewX; }

public abstract Vector3 getPosition(Vector3 position);
public abstract Quaternion getRotation(Quaternion result);

public Quaternion getInverseRotation(Quaternion result)
{ return getRotation(result).invertAndSet(); }

public Matrix3 getMatrix3(Matrix3 result)
{
	result.set(getRotation(new Quaternion()));
	return result;
}

public Matrix4 getMatrix4(Matrix4 result)
{
	result.set(getRotation(new Quaternion()));
	return result;
}

public Matrix3 getInverseMatrix3(Matrix3 result)
{
	result.set(getInverseRotation(new Quaternion()));
	return result;
}

public Matrix4 getInverseMatrix4(Matrix4 result)
{
	result.set(getInverseRotation(new Quaternion()));
	return result;
}

public void setWidth(int width)
{ this.width = width; }

public void setHeight(int height)
{ this.height = height; }

public void setAspectRatio(double aspectRatio)
{ this.aspectRatio = aspectRatio; }

public void setNearPlane(double nearPlane)
{ this.nearPlane = nearPlane; }

public void setFarPlane(double farPlane)
{ this.farPlane = farPlane; }

public void setFieldOfViewY(double fieldOfViewY)
{ this.fieldOfViewY = fieldOfViewY; }

public void setFieldOfViewX(double fieldOfViewX)
{ this.fieldOfViewX = fieldOfViewX; }

public abstract void setPosition(Vector3 position);
public abstract void setRotation(Quaternion rotation);

public void setSize(int width, int height)
{
	setWidth(width);
	setHeight(height);
	setAspectRatio((double) getWidth()/(double) getHeight());
}

}
