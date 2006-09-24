package de.yvert.camera;

import de.yvert.geometry.*;

/**
 * A helper class with camera modification and query functions.
 * These functions have well-defined semantics and can be expressed in terms of the
 * {@link Camera} interface.
 */
public final class CameraTools
{

public static double calcFieldOfViewX(double fieldOfViewY, double aspectRatio)
{
	return 2.0*Math.atan(aspectRatio*Math.tan(fieldOfViewY/2.0));
}

public static void setResolution(Camera camera, int width, int height)
{
	double aspectRatio = (double) width/(double) height;
	camera.setWidth(width);
	camera.setHeight(height);
	camera.setAspectRatio(aspectRatio);
	camera.setFieldOfViewX(calcFieldOfViewX(camera.getFieldOfViewY(), aspectRatio));
}

/**
 * Constructs a camera from position, look and up vectors.
 * <p>
 * If the up vector is colinear with the look vector, the result is undefined.
 * The vectors neither have to be normalized nor orthogonal.
 */
public static void setLookAt(Camera camera, Vector3 position, Vector3 target, Vector3 up)
{
	camera.setPosition(position);
	
	Vector3 nup = up.normalize();
	Vector3 f = target.sub(position).normalizeAndSet();
	Vector3 s = f.cross(nup).normalizeAndSet();
	Vector3 u = s.cross(f).normalizeAndSet();
	f.scaleAndSet(-1);
	camera.setRotation(new Matrix3(s, u, f).getQuaternion());
}

/**
 * Creates a 4x4 OpenGL transformation matrix to be uploaded with glLoadMatrix in modelview mode.
 */
public static Matrix4 createTransformation(Camera camera)
{
	Matrix4 result = new Matrix4();
	Vector3 position = camera.getPosition(new Vector3());
	result.setEntry(0, 0, 1);
	result.setEntry(1, 1, 1);
	result.setEntry(2, 2, 1);
	result.setEntry(3, 3, 1);
	result.setEntry(0, 3, -position.getX());
	result.setEntry(1, 3, -position.getY());
	result.setEntry(2, 3, -position.getZ());
	return camera.getInverseMatrix4(new Matrix4()).multiply(result);
}

/**
 * Creates a 4x4 OpenGL perspective matrix to be uploaded with glLoadMatrix in perspective mode.
 */
public static Matrix4 createGluPerspective(Camera camera)
{
	double farPlane = camera.getFarPlane();
	double nearPlane = camera.getNearPlane();
	double fieldOfViewY = camera.getFieldOfViewY();
	Matrix4 result = new Matrix4();
	double halfFovy = fieldOfViewY /2.0;
	double coT = 1.0 / Math.tan(halfFovy*Math.PI/180.0);
	result.setEntry(0, 0, coT/camera.getAspectRatio());
	result.setEntry(1, 1, coT);
	result.setEntry(2, 2, (farPlane+nearPlane)/(nearPlane-farPlane));
	result.setEntry(2, 3, (2*farPlane*nearPlane)/(nearPlane-farPlane));
	result.setEntry(3, 2, -1);
	result.setEntry(3, 3, 0);
	return result;
}

/**
 * Decompose matrix into quaternion and set the camera's rotation to this quaternion.
 */
public static void setMatrix3(Camera camera, Matrix3 matrix)
{
	Quaternion temp = matrix.getQuaternion();
	if (temp == null) throw new IllegalArgumentException("Unable to decompose matrix into quaternion!");
	camera.setRotation(temp);
}

/**
 * Returns the -z value used by {@link #getRay(Camera,Ray,double,double)}.
 * <p>
 * This calculated as follows: <code>cot(fovy/2)*height / 2</code> 
 * 
 * @return the -z value used by {@link #getRay(Camera,Ray,double,double)}.
 */
public static double getDist(Camera camera)
{
	double halfFovy = camera.getFieldOfViewY() /2.0;
	double coT = 1.0 / Math.tan(halfFovy*Math.PI/180.0);
	double distCache = coT*camera.getHeight()/2.0;
	return distCache;
}

/**
 * Calculates the ray that corresponds to the virtual ray OpenGL uses for the
 * given screen coordinates.
 * <p>
 * We use the inverse mapping of gluPerspective combined with glViewport.
 * <pre>
 * gluPerspective:
 * [ xp ]   | f/aspect 0   0  0 |   [ x ]
 * [ yp ] = |   0      f   0  0 | * [ y ]
 * [ zp ]   |   0      0   A  B |   [ z ]
 * [ wp ]   |   0      0  -1  0 |   [ 1 ]
 * aspect = cot(fovy/2)
 * 
 * glViewport:
 * xw = (xp/wp + 1) * width/2 + x0
 * yw = (yp/wp + 1) * height/2 + y0 
 * </pre>
 * 
 * In this camera, we know width and height, and x0 and y0 are always zero. Also, 
 * we only need one ray with arbitrary length for given window coordinates (xw,yw).
 * Therefore:
 * <pre>
 * x = (-2*xw / width  + 1)*z*aspect / f
 * y = (-2*yw / height + 1)*z        / f
 * z = -cot(fovy/2)*height / 2 &lt;--- can be freely chosen
 * </pre>
 * 
 * We simplify:
 * <pre>
 * x = (2*xw / width  - 1)*height/2*aspect
 * y = (2*yw / height - 1)*height/2
 * z = -cot(fovy/2)*height / 2
 * </pre>
 * 
 * If <code>aspect = width/height</code>, then this further simplifies to:
 * <pre>
 * x = xw - width/2
 * y = yw - height/2
 * z = -cot(fovy/2)*height / 2
 * </pre>
 * You can get this (admittedly odd) z value from the {@link #getDist()} function.
 * Also, due to the way OpenGL works, the pixel center is actually at .5, .5, so we
 * further add that much to both the x and y coordinates. 
 * 
 * If <code>aspect != width/height</code>, then this function DOES NOT WORK correctly!
 */
public static Ray getRay(Camera camera, Ray ray, double x, double y)
{
	camera.getPosition(ray.p);
	double vx = x-camera.getWidth()/2.+0.5;
	double vy = y-camera.getHeight()/2.+0.5;
	Matrix3 rot = camera.getMatrix3(new Matrix3());
	ray.v.set(vx, vy, -getDist(camera)).multiplyAndSet(rot);
	return ray;
}

/**
 * Converts z-buffer values to depth values according to this camera.
 */
public static float glZToDepth(Camera camera, float x)
{
	double farPlane = camera.getFarPlane();
	double nearPlane = camera.getNearPlane();
	double a = 2*farPlane*nearPlane/(nearPlane-farPlane);
	double b = (farPlane+nearPlane)/(nearPlane-farPlane);
	
	if (x == 1) return 0;
	return (float) (a/(2*x-1+b));
}

public static String serialize(Camera camera)
{ throw new UnsupportedOperationException(); }

public static Camera deserialize(String s)
{ throw new UnsupportedOperationException(); }

private CameraTools()
{/*Not instantiable!*/}

}
