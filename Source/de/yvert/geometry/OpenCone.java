// arch-tag: 39549e33-c6ec-4954-97a2-3a9c7f56595a
package de.yvert.geometry;

import de.yvert.cr.profiles.IntersectionResult;

public class OpenCone extends SceneItem
{

private static final double EPSILON = 1e-4f;

public Vector3 apex, vertex;
public double apexRadius, vertexRadius;

public OpenCone(OpenCone other)
{ set(other); }

public OpenCone(Vector3 apex, Vector3 vertex, double apexRadius, double vertexRadius)
{
	this.apex = new Vector3(apex);
	this.vertex = new Vector3(vertex);
	this.apexRadius = apexRadius;
	this.vertexRadius = vertexRadius;
}

public OpenCone(Vector4 apex, Vector4 vertex)
{
	this.apex = new Vector3().set(apex);
	this.vertex = new Vector3().set(vertex);
	this.apexRadius = apex.getV3();
	this.vertexRadius = vertex.getV3();
}


public final OpenCone set(OpenCone other)
{
	this.apex = new Vector3(other.apex);
	this.vertex = new Vector3(other.vertex);
	this.apexRadius = other.apexRadius;
	this.vertexRadius = other.vertexRadius;
	return this;
}

private double min(double a, double b)
{ return a < b ? a : b; }

private double max(double a, double b)
{ return a > b ? a : b; }

@Override
public void getNormal(IntersectionResult result, Vector3 geonormal)
{
	Vector3 a = apex.sub(vertex);
	
	double u = result.hitpoint.sub(vertex).multiply(a)/a.getSquaredLength();
	double r0 = vertexRadius, r1 = apexRadius;
	Vector3 pl = a.scale(u).addAndSet(vertex);
	double tanAlpha = (r1-r0)/a.getLength();
	double dp = result.hitpoint.sub(pl).getLength()*tanAlpha/a.getLength();
	geonormal.set(a).scaleAndSet(-dp).addAndSet(result.hitpoint).subAndSet(pl);
	geonormal.normalizeAndSet();
}

@Override
public double getMinX()
{ return min(apex.getX()-apexRadius, vertex.getX()-vertexRadius); }

@Override
public double getMaxX()
{ return max(apex.getX()+apexRadius, vertex.getX()+vertexRadius); }

@Override
public double getMinY()
{ return min(apex.getY()-apexRadius, vertex.getY()-vertexRadius); }

@Override
public double getMaxY()
{ return max(apex.getY()+apexRadius, vertex.getY()+vertexRadius); }

@Override
public double getMinZ()
{ return min(apex.getZ()-apexRadius, vertex.getZ()-vertexRadius); }

@Override
public double getMaxZ()
{ return max(apex.getZ()+apexRadius, vertex.getZ()+vertexRadius); }

@Override
public BoundingBox getBoundingBox()
{
	return new BoundingBox(new Vector3(getMinX(), getMinY(), getMinZ()),
	               new Vector3(getMaxX(), getMaxY(), getMaxZ()));
}

@Override
public BoundingSphere getBoundingSphere()
{ throw new RuntimeException("Argh!"); }

@Override
public double distance(Ray r)
{
	Vector3 p0 = vertex, p1 = apex, o = r.p, d = r.v;
	double r0 = vertexRadius, r1 = apexRadius, dr = r1-r0;
	
	Vector3 a = p1.sub(p0);
	Vector3 c = o.sub(p0);
	
	double ac = a.multiply(c);
	double ad = a.multiply(d);
	double cd = c.multiply(d);
	
	double aa = a.multiply(a);
	double cc = c.multiply(c);
	double dd = d.multiply(d);
	
	// These will hold the pre-results
	double t1 = -Double.MAX_VALUE, t2 = t1 /*, t3 = t1, t4 = t1*/;
	
	// Check Side
	double z1 = 1/aa + dr*dr/(aa*aa);
	double z2 = r0*dr/aa;
	
	double c1 = ad*ad*z1 - dd;
	double c2 = 2*(ac*ad*z1 + ad*z2 - cd);
	double c3 = r0*r0 + ac*ac*z1 + ac*2*z2 - cc; 
		
	double t = c2*c2 - 4*c1*c3;
	if (t > EPSILON)
	{
		t = Math.sqrt(t);
		t1 = (-c2 + t)/(2*c1);
		t2 = (-c2 - t)/(2*c1);
		
		double u1 = (ac + t1*ad)/aa;
		double u2 = (ac + t2*ad)/aa;
		if (u1 < 0 || u1 > 1) t1 = -Double.MAX_VALUE;
		if (u2 < 0 || u2 > 1) t2 = -Double.MAX_VALUE;
	}
	
/*	// Check Top/Bottom
	if ((ad > EPSILON) || (ad < -EPSILON))
	{
		Vector3 e = o.sub(p1);
		double ae = a.multiply(e);
		double de = d.multiply(e);
		double ee = e.multiply(e);
			
		t3 = -ac/ad;
		t4 = -ae/ad;
		double rad3 = cc + 2*cd*t3 + dd*t3*t3;
		double rad4 = ee + 2*de*t4 + dd*t4*t4;
			
		t3 = (rad3 > r0*r0) ? -Double.MAX_VALUE : t3;
		t4 = (rad4 > r1*r1) ? -Double.MAX_VALUE : t4;
	}*/
	
	// Merge results (hint: at most 2 t-values are greater than 0)
	double tmax = Math.max(t1, t2);
	double tmin = -Double.MAX_VALUE;
	if ((t1 > EPSILON) && (t1 < tmax)) tmin = t1;
	if ((t2 > EPSILON) && (t2 < tmax)) tmin = t2;
//	if ((t3 > EPSILON) && (t3 < tmax)) tmin = t3;
//	if ((t4 > EPSILON) && (t4 < tmax)) tmin = t4;
				
	if (tmax < EPSILON) return -Double.MAX_VALUE;
		
	if (tmin > EPSILON)
		return tmin;
	else
		return tmax;
}

private void triangulate(Triangulation tri, int comega)
{
	Vector3 up = vertex.sub(apex).normalizeAndSet();
	Vector3 left = up.findAnyOrthogonal();
	Vector3 front = up.cross(left).normalizeAndSet();
	
	Vector3[][] data = new Vector3[comega+1][2];
	for (int i = 0; i <= comega; i++)
	{
		double omega = ((double) i*2*Math.PI)/comega;
		double x = Math.sin(omega);
		double y = Math.cos(omega);
		
		data[i][0] = new Vector3(apex).addAndSet(left, apexRadius*x);
		data[i][0].addAndSet(front, apexRadius*y);
		
		data[i][1] = new Vector3(vertex).addAndSet(left, vertexRadius*x);
		data[i][1].addAndSet(front, vertexRadius*y);
	}
	
	for (int i = 0; i < comega; i++)
		for (int j = 0; j < 1; j++)
		{
			Vector3 a = data[i][j];
			Vector3 b = data[i][j+1];
			Vector3 c = data[i+1][j+1];
			Vector3 d = data[i+1][j];
			
			Triangle t0 = new Triangle(a, b, c);
			t0.setMaterial(material);
			
			Triangle t1 = new Triangle(c, d, a);
			t1.setMaterial(material);
			
			tri.add(t0).add(t1);
		}
}

@Override
public void triangulate(Triangulation tri)
{
	triangulate(tri, 10);
}

}
