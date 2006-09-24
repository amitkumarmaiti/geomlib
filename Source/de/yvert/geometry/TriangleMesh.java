// arch-tag: 269129a6-27e4-4364-9c07-b4387fe7746f
package de.yvert.geometry;

import de.yvert.cr.profiles.Material;
import de.yvert.textures.TexCoordFunction;
import de.yvert.textures.Texture;
import de.yvert.textures.TriangleTexCoordFunction;

public final class TriangleMesh extends SceneObject
{

private static final TriangleTexCoordFunction FUNCTION_0 = new TriangleTexCoordFunction(0);
private static final TriangleTexCoordFunction FUNCTION_1 = new TriangleTexCoordFunction(1);

public String name;

public float[][] vertexData;   // vertex data
public float[][] texcoordData; // texture coordinates per vertex
public float[][] lmcoordData;  // texture coordinates per vertex

public int[][] faceData;       // int triple per triangle
public Texture[] textureData;  // one texture per triangle
public Texture[] lightmapData; // one lightmap per triangle
public float[][] normalData;   // normal data per triangle (not vertex)
public float[][] vertexNormals; // normal data per vertex

public Material material;

public TriangleMesh()
{/*OK*/}

public TriangleMesh(Triangle data)
{
	vertexData = new float[3][3];
	texcoordData = new float[3][4];
	lmcoordData = new float[3][4];
	
	faceData = new int[1][3];
	textureData = new Texture[1];
	lightmapData = new Texture[1];
	
	data.a.get(vertexData[0]);
	data.b.get(vertexData[1]);
	data.c.get(vertexData[2]);
	
	if (data.getTextures() != null)
	{
		if ((data.getTextures().length > 0) && (data.getTextures()[0] != null))
		{
			data.texcoordparams[0].get(texcoordData[0]);
			data.texcoordparams[1].get(texcoordData[1]);
			data.texcoordparams[2].get(texcoordData[2]);
			textureData[0] = data.getTextures()[0];
		}
		
		if ((data.getTextures().length > 1) && (data.getTextures()[1] != null))
		{
			data.texcoordparams[3].get(lmcoordData[0]);
			data.texcoordparams[4].get(lmcoordData[1]);
			data.texcoordparams[5].get(lmcoordData[2]);
			lightmapData[0] = data.getTextures()[1];
		}
	}
	
	faceData[0][0] = 0;
	faceData[0][1] = 1;
	faceData[0][2] = 2;
	
	material = data.material;
	calculateNormals();
}

public TriangleMesh(Triangle[] data)
{
	vertexData = new float[3*data.length][3];
	texcoordData = new float[3*data.length][4];
	lmcoordData = new float[3*data.length][4];
	
	faceData = new int[data.length][3];
	textureData = new Texture[data.length];
	lightmapData = new Texture[data.length];
	
	for (int i = 0; i < data.length; i++)
	{
		data[i].a.get(vertexData[3*i  ]);
		data[i].b.get(vertexData[3*i+1]);
		data[i].c.get(vertexData[3*i+2]);
		
		if (data[i].getTextures() != null)
		{
			if (data[i].getTextures()[0] != null)
			{
				data[i].texcoordparams[0].get(texcoordData[3*i  ]);
				data[i].texcoordparams[1].get(texcoordData[3*i+1]);
				data[i].texcoordparams[2].get(texcoordData[3*i+2]);
				textureData[i] = data[i].getTextures()[0];
			}
			
			if (data[i].getTextures()[1] != null)
			{
				data[i].texcoordparams[3].get(lmcoordData[3*i  ]);
				data[i].texcoordparams[4].get(lmcoordData[3*i+1]);
				data[i].texcoordparams[5].get(lmcoordData[3*i+2]);
				lightmapData[i] = data[i].getTextures()[1];
			}
		}
		
		faceData[i][0] = 3*i;
		faceData[i][1] = 3*i+1;
		faceData[i][2] = 3*i+2;
	}
	
	calculateNormals();
}


public void setMaterial(Material material)
{ this.material = material; }

public Material getMaterial()
{ return material; }

public int triangleCount()
{ return faceData.length; }

public Vector3 getMin(Vector3 in)
{
	in.set(vertexData[0][0], vertexData[0][1], vertexData[0][2]);
	for (int i = 0; i < vertexData.length; i++)
		in.componentMinAndSet(vertexData[i][0], vertexData[i][1], vertexData[i][2]);
	return in;
}

public Vector3 getMax(Vector3 in)
{
	in.set(vertexData[0][0], vertexData[0][1], vertexData[0][2]);
	for (int i = 0; i < vertexData.length; i++)
		in.componentMaxAndSet(vertexData[i][0], vertexData[i][1], vertexData[i][2]);
	return in;
}

@Override
public void triangulate(Triangulation tri)
{
	Triangle[] data = getTriangles();
	for (int i = 0; i < data.length; i++)
		tri.add(data[i]);
}

public final void calculateNormals()
{
	normalData = new float[faceData.length][3];
	for (int i = 0; i < faceData.length; i++)
	{
		int a = faceData[i][0];
		int b = faceData[i][1];
		int c = faceData[i][2];
		
		float ex = vertexData[b][0]-vertexData[a][0];
		float ey = vertexData[b][1]-vertexData[a][1];
		float ez = vertexData[b][2]-vertexData[a][2];
		
		float fx = vertexData[c][0]-vertexData[a][0];
		float fy = vertexData[c][1]-vertexData[a][1];
		float fz = vertexData[c][2]-vertexData[a][2];
		
		normalData[i][0] = ey*fz - ez*fy;
		normalData[i][1] = ez*fx - ex*fz;
		normalData[i][2] = ex*fy - ey*fx;
	}
}

public void getNormal(Vector3 normal, int index)
{
	normal.set(normalData[index]);
}

public Triangle getTriangle(Triangle t, int index)
{
	t.a = new Vector3(vertexData[faceData[index][0]]);
	t.b = new Vector3(vertexData[faceData[index][1]]);
	t.c = new Vector3(vertexData[faceData[index][2]]);
	t.n = new Vector3(normalData[index]).normalizeAndSet();
	
	if (vertexNormals != null)
	{
		t.vertexNormals[0] = new Vector3(vertexNormals[faceData[index][0]]);
		t.vertexNormals[1] = new Vector3(vertexNormals[faceData[index][1]]);
		t.vertexNormals[2] = new Vector3(vertexNormals[faceData[index][2]]);
	}
	else
	{
		t.vertexNormals[0] = t.n;
		t.vertexNormals[1] = t.n;
		t.vertexNormals[2] = t.n;
	}
	
	if ((texcoordData != null) && (textureData != null))
	{
		if ((lmcoordData != null) && (lightmapData != null))
		{
			t.setTextures(new Texture[] { textureData[index], lightmapData[index] });
			t.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0, FUNCTION_1 });
			t.texcoordparams = new Vector4[6];
			
			t.texcoordparams[0] = new Vector4(texcoordData[faceData[index][0]], true);
			t.texcoordparams[1] = new Vector4(texcoordData[faceData[index][1]], true);
			t.texcoordparams[2] = new Vector4(texcoordData[faceData[index][2]], true);
			
			t.texcoordparams[3] = new Vector4(lmcoordData[faceData[index][0]], true);
			t.texcoordparams[4] = new Vector4(lmcoordData[faceData[index][1]], true);
			t.texcoordparams[5] = new Vector4(lmcoordData[faceData[index][2]], true);
		}
		else
		{
			t.setTextures(new Texture[] { textureData[index] });
			t.setTexCoordFunctions(new TexCoordFunction[] { FUNCTION_0 });
			t.texcoordparams = new Vector4[3];
			
			t.texcoordparams[0] = new Vector4(texcoordData[faceData[index][0]], true);
			t.texcoordparams[1] = new Vector4(texcoordData[faceData[index][1]], true);
			t.texcoordparams[2] = new Vector4(texcoordData[faceData[index][2]], true);
			
//			System.out.println(t.texcoordparams[0]+" "+t.texcoordparams[1]+" "+t.texcoordparams[2]);
		}
	}
	
	t.setMaterial(material);
	return t;
}

public Triangle getTriangle(int index)
{
	return getTriangle(new Triangle(), index);
}

public Triangle[] getTriangles()
{
	Triangle[] result = new Triangle[faceData.length];
	for (int i = 0; i < faceData.length; i++)
		result[i] = getTriangle(i);
	return result;
}

@Override
public String toString()
{ return name; }

}
