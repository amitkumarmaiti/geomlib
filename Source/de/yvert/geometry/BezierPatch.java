// arch-tag: 40e00802-6238-4d21-8d14-bbc21447cc33
package de.yvert.geometry;

import de.yvert.cr.profiles.Material;
import de.yvert.textures.TexCoordFunction;
import de.yvert.textures.Texture;
import de.yvert.textures.TriangleTexCoordFunction;

/**
 * Describes a Quake3-scene compatible bezier patch.
 * <p>
 * TODO: Allow the triangulation resolution to be specified in Triangulation.
 * 
 * @author Ulf Ochsenfahrt
 */
public class BezierPatch extends SceneObject
{

private static final TriangleTexCoordFunction FUNCTION_0 = new TriangleTexCoordFunction(0);
private static final TriangleTexCoordFunction FUNCTION_1 = new TriangleTexCoordFunction(1);

public int sizex;
public int sizey;

public Material material;

public float[][] vertexData;   // vertex data
public float[][] normalData;   // normal data for every vertex
public float[][] texcoordData; // texture coordinate for every vertex
public float[][] lmcoordData;  // lightmap coordinate for every vertex

public Texture texture;
public Texture lightmap;

private double[][] helper = new double[4][3];

public BezierPatch(int sizex, int sizey)
{
	this.sizex = sizex;
	this.sizey = sizey;
	vertexData = new float[sizex*sizey][3];
	texcoordData = new float[sizex*sizey][2];
	lmcoordData = new float[sizex*sizey][2];
}

private Vector3 getVertex(Vector3 vertex, double s, double t)
{
	int xi = 2 * (int) Math.floor(s);
	int yi = 2 * (int) Math.floor(t);
	double ms = s-Math.floor(s);
	double mt = t-Math.floor(t);
	
	if (xi >= sizex)
		throw new RuntimeException("XI");
	if (yi >= sizey)
		throw new RuntimeException("YI");
	
	if (xi == sizex-1)
	{
		xi -= 2;
		ms += 1;
	}
	if (yi == sizey-1)
	{
		yi -= 2;
		mt += 1;
	}
	
	for (int i = 0; i < 3; i++)
	{
		helper[0][i] = (1-ms)*(1-ms)*vertexData[xi  +yi*sizex][i] +
		               2*ms*(1-ms)  *vertexData[xi+1+yi*sizex][i] +
		               ms*ms        *vertexData[xi+2+yi*sizex][i];
		helper[1][i] = (1-ms)*(1-ms)*vertexData[xi  +(yi+1)*sizex][i] +
		               2*ms*(1-ms)  *vertexData[xi+1+(yi+1)*sizex][i] +
		               ms*ms        *vertexData[xi+2+(yi+1)*sizex][i];
		helper[2][i] = (1-ms)*(1-ms)*vertexData[xi  +(yi+2)*sizex][i] +
		               2*ms*(1-ms)  *vertexData[xi+1+(yi+2)*sizex][i] +
		               ms*ms        *vertexData[xi+2+(yi+2)*sizex][i];
		
		helper[3][i] = (1-mt)*(1-mt)*helper[0][i] +
		               2*mt*(1-mt)  *helper[1][i] +
		               mt*mt        *helper[2][i];
	}
	
	vertex.set(helper[3]);
	return vertex;
}

private Vector4 getTexCoord(Vector4 vertex, double s, double t)
{
	int xi = 2 * (int) Math.floor(s);
	int yi = 2 * (int) Math.floor(t);
	double ms = s-Math.floor(s);
	double mt = t-Math.floor(t);
	
	if (xi >= sizex)
		throw new RuntimeException("XI");
	if (yi >= sizey)
		throw new RuntimeException("YI");
	
	if (xi == sizex-1)
	{
		xi -= 2;
		ms += 1;
	}
	if (yi == sizey-1)
	{
		yi -= 2;
		mt += 1;
	}
	
	for (int i = 0; i < 2; i++)
	{
		helper[0][i] = (1-ms)*(1-ms)*texcoordData[xi  +yi*sizex][i] +
		               2*ms*(1-ms)  *texcoordData[xi+1+yi*sizex][i] +
		               ms*ms        *texcoordData[xi+2+yi*sizex][i];
		helper[1][i] = (1-ms)*(1-ms)*texcoordData[xi  +(yi+1)*sizex][i] +
		               2*ms*(1-ms)  *texcoordData[xi+1+(yi+1)*sizex][i] +
		               ms*ms        *texcoordData[xi+2+(yi+1)*sizex][i];
		helper[2][i] = (1-ms)*(1-ms)*texcoordData[xi  +(yi+2)*sizex][i] +
		               2*ms*(1-ms)  *texcoordData[xi+1+(yi+2)*sizex][i] +
		               ms*ms        *texcoordData[xi+2+(yi+2)*sizex][i];
		
		helper[3][i] = (1-mt)*(1-mt)*helper[0][i] +
		               2*mt*(1-mt)  *helper[1][i] +
		               mt*mt        *helper[2][i];
	}
	
	vertex.set(helper[3][0], helper[3][1], 0, 0);
	return vertex;
}

private Vector4 getLmCoord(Vector4 vertex, double s, double t)
{
	int xi = 2 * (int) Math.floor(s);
	int yi = 2 * (int) Math.floor(t);
	double ms = s-Math.floor(s);
	double mt = t-Math.floor(t);
	
	if (xi >= sizex)
		throw new RuntimeException("XI");
	if (yi >= sizey)
		throw new RuntimeException("YI");
	
	if (xi == sizex-1)
	{
		xi -= 2;
		ms += 1;
	}
	if (yi == sizey-1)
	{
		yi -= 2;
		mt += 1;
	}
	
	for (int i = 0; i < 2; i++)
	{
		helper[0][i] = (1-ms)*(1-ms)*lmcoordData[xi  +yi*sizex][i] +
		               2*ms*(1-ms)  *lmcoordData[xi+1+yi*sizex][i] +
		               ms*ms        *lmcoordData[xi+2+yi*sizex][i];
		helper[1][i] = (1-ms)*(1-ms)*lmcoordData[xi  +(yi+1)*sizex][i] +
		               2*ms*(1-ms)  *lmcoordData[xi+1+(yi+1)*sizex][i] +
		               ms*ms        *lmcoordData[xi+2+(yi+1)*sizex][i];
		helper[2][i] = (1-ms)*(1-ms)*lmcoordData[xi  +(yi+2)*sizex][i] +
		               2*ms*(1-ms)  *lmcoordData[xi+1+(yi+2)*sizex][i] +
		               ms*ms        *lmcoordData[xi+2+(yi+2)*sizex][i];
		
		helper[3][i] = (1-mt)*(1-mt)*helper[0][i] +
		               2*mt*(1-mt)  *helper[1][i] +
		               mt*mt        *helper[2][i];
	}
	
	vertex.set(helper[3][0], helper[3][1], 0, 0);
	return vertex;
}

private Vector3 getVertex(double s, double t)
{
	return getVertex(new Vector3(), s, t);
}

private Vector4 getTexCoord(double s, double t)
{
	return getTexCoord(new Vector4(), s, t);
}

private Vector4 getLmCoord(double s, double t)
{
	return getLmCoord(new Vector4(), s, t);
}

@Override
public void triangulate(Triangulation tri)
{
	int numx = 4*sizex;
	int numy = 4*sizey;
	
	double fx = (sizex-1)/(2.0*numx);
	double fy = (sizey-1)/(2.0*numy);
	
	for (int i = 0; i < numx; i++)
		for (int j = 0; j < numy; j++)
		{
			double s = i*fx;
			double t = j*fy;
			
			Triangle triangle;
			triangle = new Triangle();
			triangle.a = getVertex(s,    t);
			triangle.b = getVertex(s+fx, t);
			triangle.c = getVertex(s,    t+fy);
			triangle.calculateNormal();
			
			if (texture != null)
			{
				if (lightmap != null)
				{
					triangle.setTextures(new Texture[] { texture, lightmap });
					triangle.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0, FUNCTION_1 });
					triangle.texcoordparams = new Vector4[6];
					triangle.texcoordparams[0] = getTexCoord(s,    t);
					triangle.texcoordparams[1] = getTexCoord(s+fx, t);
					triangle.texcoordparams[2] = getTexCoord(s,    t+fy);
					
					triangle.texcoordparams[3] = getLmCoord(s,    t);
					triangle.texcoordparams[4] = getLmCoord(s+fx, t);
					triangle.texcoordparams[5] = getLmCoord(s,    t+fy);
				}
				else
				{
					triangle.setTextures(new Texture[] { texture });
					triangle.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0 });
					triangle.texcoordparams = new Vector4[3];
					triangle.texcoordparams[0] = getTexCoord(s,    t);
					triangle.texcoordparams[1] = getTexCoord(s+fx, t);
					triangle.texcoordparams[2] = getTexCoord(s,    t+fy);
				}
			}
			triangle.setMaterial(material);
			tri.add(triangle);
			
			
			
			triangle = new Triangle();
			triangle.a = getVertex(s,    t+fy);
			triangle.b = getVertex(s+fx, t);
			triangle.c = getVertex(s+fx, t+fy);
			triangle.calculateNormal();
			
			if (texture != null)
			{
				if (lightmap != null)
				{
					triangle.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0, FUNCTION_1 });
					triangle.setTextures(new Texture[] { texture, lightmap });
					triangle.texcoordparams = new Vector4[6];
					triangle.texcoordparams[0] = getTexCoord(s,    t+fy);
					triangle.texcoordparams[1] = getTexCoord(s+fx, t);
					triangle.texcoordparams[2] = getTexCoord(s+fx, t+fy);
					
					triangle.texcoordparams[3] = getLmCoord(s,    t+fy);
					triangle.texcoordparams[4] = getLmCoord(s+fx, t);
					triangle.texcoordparams[5] = getLmCoord(s+fx, t+fy);
				}
				else
				{
					triangle.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0 });
					triangle.setTextures(new Texture[] { texture });
					triangle.texcoordparams = new Vector4[3];
					triangle.texcoordparams[0] = getTexCoord(s,    t+fy);
					triangle.texcoordparams[1] = getTexCoord(s+fx, t);
					triangle.texcoordparams[2] = getTexCoord(s+fx, t+fy);
				}
			}
			triangle.setMaterial(material);
			tri.add(triangle);
		}
}

}
