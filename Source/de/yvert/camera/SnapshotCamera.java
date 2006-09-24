package de.yvert.camera;

import de.yvert.geometry.Quaternion;
import de.yvert.geometry.Vector3;

public final class SnapshotCamera extends AbstractCamera
{

private static final long serialVersionUID = 1L;

private Vector3 position = new Vector3();
private Quaternion rotation = new Quaternion();

public SnapshotCamera(Camera camera)
{ set(camera); }

public SnapshotCamera()
{/*OK*/}

@Override
public SnapshotCamera clone()
{
	SnapshotCamera result = (SnapshotCamera) super.clone();
	result.rotation = rotation.clone();
	return result;
}

public final void set(Camera camera)
{
	copyAll(camera);
	camera.getPosition(position);
	camera.getRotation(rotation);
}

@Override
public Vector3 getPosition(Vector3 result)
{ return result.set(position); }

@Override
public Quaternion getRotation(Quaternion result)
{ return result.set(rotation); }

@Override
public void setPosition(Vector3 position)
{ this.position.set(position); }

@Override
public void setRotation(Quaternion rotation)
{ this.rotation.set(rotation); }


}
