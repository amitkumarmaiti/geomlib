package de.yvert.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.yvert.algorithms.VertexNormalCalculator;
import de.yvert.cr.profiles.Material;
import de.yvert.geometry.TriangleMesh;
import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;

public class ModelReader_obj extends TextModelReader implements ModelReader
{

private static final int USE_MTL    = (1 << 0);
private static final int GROUP_NAME = (1 << 1);

private static final Material DEFAULT_MATERIAL = null; //MaterialFactory.createDiffuse(Color.WHITE);

protected static final Pattern SL_INT_PATTERN =
	Pattern.compile("("+INT_STR+")(?:/((?:"+INT_STR+")?)(?:/((?:"+INT_STR+")?))?)?");

	private static class Vertex
	{
		Vector3 pos, texcoords;
		public Vertex(Vector3 pos, Vector3 texcoords)
		{ this.pos = pos; this.texcoords = texcoords; }
		public boolean equals(Vertex o)
		{ return (pos == o.pos)/* && (texcoords == o.texcoords)*/; }
		@Override
		public boolean equals(Object other)
		{ return equals((Vertex) other); }
		@Override
		public int hashCode()
		{ return pos.hashCode()/*+texcoords.hashCode()*/; }
	}
	private static class Face
	{
		int[][] indices;
		public Face(int[][] indices)
		{ this.indices = indices; }
	}

HashMap<String,Material> materials = new HashMap<String,Material>();
ArrayList<Vector3> vertices = new ArrayList<Vector3>();
ArrayList<Vector3> texcoords = new ArrayList<Vector3>();
ArrayList<Vector3> normals = new ArrayList<Vector3>();
ArrayList<Face> faces = new ArrayList<Face>();

boolean hasObject = false;

private int warned = 0;

public ModelReader_obj()
{/*OK*/}

static Vector3 NULL = new Vector3();

private void finishMesh() throws IOException
{
	if (faces.size() == 0) return;
	
	TriangleMesh mesh = new TriangleMesh();
	HashMap<Vertex,Integer> used = new HashMap<Vertex,Integer>();
	for (int i = 0; i < faces.size(); i++)
	{
		Face f = faces.get(i);
		for (int j = 0; j < 3; j++)
		{
			Vector3 pos = vertices.get(f.indices[j][0]);
			Vector3 coords;
			if (f.indices[j][1] >= 0)
				coords = texcoords.get(f.indices[j][1]);
			else
				coords = NULL;
			Vertex v = new Vertex(pos, coords);
			if (!used.containsKey(v))
				used.put(v, new Integer(used.size()));
		}
	}
	
	mesh.faceData = new int[faces.size()][3];
	mesh.vertexData = new float[used.size()][3];
//	mesh.vertexNormals = new float[used.size()][3];
	mesh.texcoordData = new float[used.size()][2];
	for (int i = 0; i < faces.size(); i++)
	{
		Face f = faces.get(i);
		for (int j = 0; j < 3; j++)
		{
			Vector3 pos = vertices.get(f.indices[j][0]);
//			Vector3 normal = normals.get(f.indices[j][2]);
			Vector3 coords;
			if (f.indices[j][1] >= 0)
				coords = texcoords.get(f.indices[j][1]);
			else
				coords = NULL;
			Vertex v = new Vertex(pos, coords);
			int k = used.get(v).intValue();
			mesh.faceData[i][j] = k;
			mesh.vertexData[k][0] = (float) pos.getX();
			mesh.vertexData[k][1] = (float) pos.getY();
			mesh.vertexData[k][2] = (float) pos.getZ();
//			mesh.vertexNormals[k][0] = (float) normal.getX();
//			mesh.vertexNormals[k][1] = (float) normal.getY();
//			mesh.vertexNormals[k][2] = (float) normal.getZ();
			mesh.texcoordData[k][0] = (float) coords.getX();
			mesh.texcoordData[k][1] = (float) coords.getY();
//			mesh.texcoordData[k][2] = (float) coords.getZ();
		}
		
		int h = mesh.faceData[i][1];
		mesh.faceData[i][1] = mesh.faceData[i][2];
		mesh.faceData[i][2] = h;
	}
	
//	System.out.println("WARNING: new normal calculations!");
	VertexNormalCalculator calc = new VertexNormalCalculator(mesh.vertexData, mesh.faceData, mesh.texcoordData);
	calc.doCalculation();
	mesh.vertexData = calc.getVertices();
	mesh.faceData = calc.getTriangles();
	mesh.texcoordData = calc.getTexCoords();
	mesh.vertexNormals = calc.getNormals();
	
	mesh.calculateNormals();
	if (currentMaterial != null)
		mesh.setMaterial(currentMaterial);
	else
		mesh.setMaterial(DEFAULT_MATERIAL);
	model.add(mesh);
	faces.clear();
	if (false) throw new IOException("FINISH ME!");
}

private void warn(int what)
{
	if ((warned & what) != 0) return;
	switch (what)
	{
		case USE_MTL : System.out.println("WARNING: Usemtl declaration ignored!"); break;
		case GROUP_NAME : System.out.println("WARNING: Group name declaration ignored!"); break;
	}
	warned |= what;
}

private void parseFace(String s) throws IOException
{
	String[] corners = s.split(" +");
	int[][] indices = new int[corners.length-1][3];
//	boolean hasTexCoords = true;
	Matcher m = SL_INT_PATTERN.matcher("");
	for (int i = 0; i < corners.length-1; i++)
	{
		m.reset(corners[i+1]);
		if (m.matches())
		{
			indices[i][0] = Integer.parseInt(m.group(1))-1;
			String tmp = m.group(2);
			if ((tmp != null) && !"".equals(tmp))
				indices[i][1] = Integer.parseInt(tmp)-1;
			else
				indices[i][1] = -1;
			tmp = m.group(3);
			if ((tmp != null) && !"".equals(tmp))
				indices[i][2] = Integer.parseInt(tmp)-1;
			else
				indices[i][2] = -1;
		}
		else
			throw new IOException("Invalid face line: \""+s+"\" for \""+corners[i+1]+"\"!");
	}
	switch (corners.length-1)
	{
		case 3 :
			faces.add(new Face(indices));
			break;
		case 4 :
			{
				int[][] first = new int[3][3];
				int[][] second = new int[3][3];
				first[0] = indices[0];
				first[1] = indices[1];
				first[2] = indices[2];
				second[0] = indices[0];
				second[1] = indices[2];
				second[2] = indices[3];
				faces.add(new Face(first));
				faces.add(new Face(second));
			}
			break;
		default :
			System.err.println("Too many vertices in face: \""+s+"\"!");
//			throw new IOException("Too many vertices in face: \""+s+"\"!");
	}
}

private void parseGroupName(String s) throws IOException
{
	finishMesh();
	warn(GROUP_NAME);
	if (false) throw new IOException("");
}

private void parseMaterialLibrary(String s) throws IOException
{
	System.out.println("WARNING: Mtllib declaration ignored: \""+s+"\"");
	if (false) throw new IOException("");
}

private void parseObject(String s) throws IOException
{
	if (hasObject)
		finishMesh();
	hasObject = true;
//	System.out.println("WARNING: Object declaration ignored!");
//	if (false) throw new IOException("");
}

private void parseUseMaterial(String s) throws IOException
{
	String materialName = removePrefix(s, "usemtl");
	currentMaterial = materials.get(materialName);
	if (currentMaterial == null)
	{
		System.out.println("Unknown material: \""+materialName+"\"");
//		warn(USE_MTL);
	}
}

private void parseVertex(String s) throws IOException
{
	Vector3 v = decodeVector3(s, "v");
	vertices.add(v);
}

private void parseTextureCoords(String s) throws IOException
{
	try
	{
		Vector3 v = decodeVector3(s, "vt");
		texcoords.add(v);
	}
	catch (IOException e)
	{
		Vector2 ov = decodeVector2(s, "vt");
		Vector3 v = new Vector3(ov.getX(), ov.getY(), 0);
		texcoords.add(v);
	}
}

private void parseVertexNormal(String s) throws IOException
{
	Vector3 v = decodeVector3(s, "vn");
	normals.add(v);
}

private void parseV(String s) throws IOException
{
	int i = s.indexOf(' ');
	String vname = s.substring(0, i);
	if (vname.equals("v"))
		parseVertex(s);
	else if (vname.equals("vt"))
		parseTextureCoords(s);
	else if (vname.equals("vn"))
		parseVertexNormal(s);
	else
		throw new IOException("Unexpected line: \""+s+"\"!");
}

@Override
protected void parse() throws IOException
{
//	progressListener.start("Parsing Wavefront OBJ");
	
	currentMaterial = null;
	String s;
	while ((s = readLine()) != null)
	{
		if (s.length() == 0) continue;
		switch (s.charAt(0))
		{
			case '#' : /*COMMENT*/ break;
			case 'f' : parseFace(s); break;
			case 'g' : parseGroupName(s); break;
			case 'm' : parseMaterialLibrary(s); break;
			case 'o' : parseObject(s); break;
			case 'u' : parseUseMaterial(s); break;
			case 'v' : parseV(s);  break;
			case 's' : break;
			default :
				throw new IOException("Unexpected line: \""+s+"\"!");
		}
	}
	finishMesh();
	
//	progressListener.finish();
}

}
