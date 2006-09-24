// arch-tag: 7569faf7-1c5d-4186-8537-198b68af5c42
package de.yvert.geometry;

import de.yvert.algorithms.roots.*;
import de.yvert.cr.profiles.IntersectionResult;

public final class Torus extends SceneItem
{

private static final double EPSILON = 1e-6f;

public Vector3 center;
public Vector3 normal;
public double majorRadius;
public double minorRadius;

public Torus(Torus other)
{
	set(other);
}

public Torus(Vector3 center, Vector3 normal, double majorRadius, double minorRadius)
{
	this.center = new Vector3(center);
	this.normal = new Vector3(normal).normalizeAndSet();
	this.majorRadius = majorRadius;
	this.minorRadius = minorRadius;
}


public Vector3 getCenter()
{ return center; }

public Vector3 getNormal()
{ return normal; }

public double getMajorRadius()
{ return majorRadius; }

public double getMinorRadius()
{ return minorRadius; }

public final Torus set(Torus other)
{
	center = new Vector3(other.center);
	normal = new Vector3(other.normal);
	majorRadius = other.majorRadius;
	minorRadius = other.minorRadius;
	return this;
}

//public double surfaceArea()
//{ return 4*Math.PI*Math.PI*majorRadius*minorRadius; }

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{
	double u = result.hitpoint.sub(center).multiply(normal);
	
	Vector3 p = normal.scale(-u).addAndSet(result.hitpoint).subAndSet(center);
	p.scaleAndSet(majorRadius/p.getLength()).addAndSet(center);
	
	geonormal.set(result.hitpoint).subAndSet(p);
	geonormal.normalizeAndSet();
}

@Override
public double getMinX()
{ return center.getX()-(majorRadius+minorRadius); }

@Override
public double getMaxX()
{ return center.getX()+(majorRadius+minorRadius); }

@Override
public double getMinY()
{ return center.getY()-(majorRadius+minorRadius); }

@Override
public double getMaxY()
{ return center.getY()+(majorRadius+minorRadius); }

@Override
public double getMinZ()
{ return center.getZ()-(majorRadius+minorRadius); }

@Override
public double getMaxZ()
{ return center.getZ()+(majorRadius+minorRadius); }

@Override
public BoundingBox getBoundingBox()
{
	return new BoundingBox(new Vector3(getMinX(), getMinY(), getMinZ()), 
			new Vector3(getMaxX(), getMaxY(), getMaxZ()));
}

@Override
public BoundingSphere getBoundingSphere()
{
	return new BoundingSphere(center, majorRadius+minorRadius);
}

@Override
public double distance(Ray ray)
{
	Vector3 e = ray.p.sub(center);
	
	double dd = ray.v.multiply(ray.v);
	double dn = ray.v.multiply(normal);
	double ee = e.multiply(e);
	double ed = e.multiply(ray.v);
	double en = e.multiply(normal);

	double r2 = majorRadius*majorRadius;
	double k2 = minorRadius*minorRadius;
	double c1 = r2-k2;
	double c2 = 4*r2;
	double c3 = 2*c1-c2;
	
	double[] c = new double[5];
	c[4] = dd*dd;
	c[3] = 4*ed*dd;
	c[2] = 2*(2*ed*ed+ee*dd) + c2*dn*dn + c3*dd;
	c[1] = 4*ee*ed + c2*2*en*dn + c3*2*ed;
	c[0] = ee*ee + c2*en*en + c3*ee + c1*c1;
			
	Roots roots = RootSolver.solve(new Polynom(c));	
	double x = Double.MAX_VALUE;
	for (int i = 0; i < roots.size(); i++)
	{
		ComplexNumber r = roots.get(i);
		if ((!r.hasIm()) && (r.re < x) && (r.re > EPSILON)) x = r.re;
	}
	
	return x;
}

@Override
public String toString()
{ return center+", "+normal+", "+majorRadius+", "+minorRadius; }

@Override
public void triangulate(Triangulation tri)
{
	triangulate(tri, 10, 5);
}

private void triangulate(Triangulation tri, int comega, int ctheta)
{
	Vector3[][] data = new Vector3[comega+1][ctheta+1];
	for (int i = 0; i <= comega; i++)
		for (int j = 0; j <= ctheta; j++)
		{
			double omega = (i*2*Math.PI)/comega;
			double theta = (j*2*Math.PI)/ctheta;
			
			// Calculate disk point
			double x = minorRadius*Math.cos(theta)+majorRadius;
			double y = minorRadius*Math.sin(theta);
			double z = 0;
		
			// Rotate disk point
			z = -Math.sin(omega)*x; // + Math.cos(omega)*z;
			x =  Math.cos(omega)*x; // + Math.sin(omega)*z;
			
			// Rotate and translate to torus coordinate system
			data[i][j] = new Vector3(x,y,z);
			localToGlobal(normal, data[i][j], data[i][j]); 
			data[i][j].addAndSet(center);
		}
	
	for (int i = 0; i < comega; i++)
		for (int j = 0; j < ctheta; j++)
		{
			Vector3 a = data[i][j];
			Vector3 b = data[i][j+1];
			Vector3 c = data[i+1][j+1];
			Vector3 d = data[i+1][j];
			
			Triangle t0 = new Triangle(a, b, c, true);
			t0.setMaterial(material);
			
			Triangle t1 = new Triangle(c, d, a, true);
			t1.setMaterial(material);
			
			tri.add(t0).add(t1);
		}
}

/**
 * Transforms a given vector from local into global coordinates assuming right handed
 * coordinate systems.
 *
 * It finds the polar coordinate representation <theta, phi> of the normal. Theta is 
 * the angle between the positive world coordinate y axis and the normal. Phi is is the
 * angle between the negative world coordinate x axis and the normal projected onto the
 * world coordinate x-z-plane (the angle is counted positive counter clockwise). After 
 * that it takes the src vector and performs two rotations on it, one around the z axis
 * using theta and the other around the y axis using phi. Both are done counter
 * clockwise. The result is written to the dst vector. The code will also work, if src 
 * and dst refer to the same object.
 */
private void localToGlobal(Vector3 nresult, Vector3 src, Vector3 dst)
{
	// Find cos and sin values of the rotation angles, fix atan2-result to fit
	// into right handed coord. system with phi starting at the neg. x axis
	double ct = nresult.getY();
	double st = Math.sqrt(1-ct*ct);
	double phi = Math.PI-Math.atan2(nresult.getZ(),nresult.getX());
	double cp = Math.cos(phi);
	double sp = Math.sin(phi);
	
	// Copy input into temp. variables so that src and dst can be the same object
	double tx = src.getX(), ty = src.getY(), tz = src.getZ();
	
	// Perform the two rotations
	dst.setX(ct*cp*tx - st*cp*ty + sp*tz);
	dst.setY(st*tx + ct*ty);
	dst.setZ(-ct*sp*tx + st*sp*ty + cp*tz);
}

}
