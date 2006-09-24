package de.yvert.camera;

import de.yvert.geometry.Matrix3;
import de.yvert.geometry.Ray;
import de.yvert.geometry.Vector3;

public class CameraAccelerator
{

Vector3 position;
int width, height;
Matrix3 rotcache3;
double distCache;
double[][] normFactors;

public CameraAccelerator(Camera camera)
{
	position = camera.getPosition(new Vector3());
	width = camera.getWidth();
	height = camera.getHeight();
	rotcache3 = camera.getMatrix3(new Matrix3());
	distCache = CameraTools.getDist(camera);
}

public Ray getRay(Ray ray, double x, double y)
{
	ray.p.set(position);
	ray.v.set(x-width/2.+0.5, y-height/2.+0.5, -distCache).multiplyAndSet(rotcache3);
	return ray;
}

public Ray getRay(Ray ray, int x, int y)
{ return getRay(ray, (double) x, (double) y); }

public Ray getNextRay(Ray ray)
{
	ray.v.addFirstColumn(rotcache3);
	return ray;
}

private void createNormalizationTable()
{
	normFactors = new double[width][height];
	Vector3 temp = new Vector3();
	for (int x = 0; x < width; x++)
		for (int y = 0; y < height; y++)
		{
			temp.set(x-width/2.+0.5, y-height/2.+0.5, -distCache);
			normFactors[x][y] = 1/temp.getLength();
		}
}

public Ray normalizeRay(Ray ray, int x, int y)
{
//	ray.v.normalizeAndSet();
	if (normFactors == null) createNormalizationTable();
	ray.v.scaleAndSet(normFactors[x][y]);
	return ray;
}

}
