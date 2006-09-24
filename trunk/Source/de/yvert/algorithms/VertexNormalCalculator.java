// arch-tag: 235bc73f-7754-495d-9190-095d81cc0c77
package de.yvert.algorithms;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Christian Hundt
 * @author Ulf Ochsenfahrt
 */
public final class VertexNormalCalculator
{

public static double DISCONTINUITY_THRESHOLD = 0.707; //0.1736; //0.707;

protected float[][] vertices = null;
protected float[][] texcoords = null;
protected int[][]   triangles = null;
protected float[][] normals = null;
protected int       maxNodes = 0; //to be assigned later

//constructor
public VertexNormalCalculator()
{/*OK*/}

//constructor
public VertexNormalCalculator(float[][] vertices, int[][] triangles, float[][] texcoords)
{
	setData(vertices, triangles, texcoords);
}

//input
public final void setData(float[][] vertices, int[][] triangles, float[][] texcoords)
{
	maxNodes = vertices.length;

	this.vertices = vertices.clone();
	this.triangles = triangles.clone();
	this.texcoords = texcoords.clone();
}

//start calculation
public void doCalculation()
{
	Hashtable<Edge,EdgeInfo> adjacencyList = adjacentTriangles();
	int[][] adjacencyVTList = adjacentVerticesTriangles(adjacencyList);
	adjacencyVTList = eliminateDiscontinuities(adjacencyList, adjacencyVTList);
	normalList(adjacencyVTList);
}

//return results
public float[][] getVertices()  { return vertices;  }
public int[][] getTriangles()   { return triangles; }
public float[][] getNormals()   { return normals;   }
public float[][] getTexCoords() { return texcoords; }


//class needed to store edge information
protected static class Edge
{
	public final int v1;
	public final int v2;
	
	public Edge(int v1, int v2)
	{
		if (v1 < v2)
		{
			this.v1 = v1;
			this.v2 = v2;
		}
		else
		{
			this.v1 = v2;
			this.v2 = v1;
		}
	}
	
	@Override
	public int hashCode()
	{ return v1*10000+v2; }
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Edge)) return false;
		if ((((Edge)o).v1 == v1) && (((Edge)o).v2 == v2)) return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return v1+"-"+v2;
	}
}

protected class EdgeInfo
{
	final Edge e;
	public int t1;
	public int t2 = -1;
	public boolean discontinuity = false;
	public EdgeInfo(Edge e, int t1)
	{
		this.e = e;
		this.t1 = t1;
	}
	@Override
	public String toString()
	{
		return e+" for "+t1+" and "+t2+" "+discontinuity;
	}
}

//returns a hashtable of adjacent triangles
protected Hashtable<Edge,EdgeInfo> adjacentTriangles()
{
	//there are at most 3 times triangles edges ... however, if we assume the net to be connected,
	//each triangle adds at most 2 new edges except the first which adds 3
	//but since hash map does rehashing at 75% at and we want to minimize collision,
	//we chose the following size
	Hashtable<Edge,EdgeInfo> edges = new Hashtable<Edge,EdgeInfo>(triangles.length<<2);
	Edge edge1, edge2, edge3 = null;
	
	//add all edges to the hashtable
	for (int i = 0; i < triangles.length; i++)
	{
		edge1 = new Edge(triangles[i][0], triangles[i][1]);
		edge2 = new Edge(triangles[i][0], triangles[i][2]);
		edge3 = new Edge(triangles[i][1], triangles[i][2]);
		
		if (edges.containsKey(edge1))
			edges.get(edge1).t2 = i;
		else
			edges.put(edge1, new EdgeInfo(edge1, i));
		
		if (edges.containsKey(edge2))
			edges.get(edge2).t2 = i;
		else
			edges.put(edge2, new EdgeInfo(edge2, i));
		
		if (edges.containsKey(edge3))
			edges.get(edge3).t2 = i;
		else
			edges.put(edge3, new EdgeInfo(edge3, i));
	}
	
	//for all edges ...
	for (Edge e : edges.keySet())
	{
		EdgeInfo info = edges.get(e);
		if (info.t2 > -1)
		{
			//check whether they are a discontinuity ...
			double[] normal1 = triangleNormal(info.t1);
			double[] normal2 = triangleNormal(info.t2);
			
			double cos = dot(normal1, normal2);
			
			//and if so, mark it 
			if (cos < DISCONTINUITY_THRESHOLD) info.discontinuity = true;
		}
	}
	
/*	for (EdgeInfo e : edges.values())
	{
		System.out.println(e);
	}*/
	
	return edges;
}

//returns a list which associates the vertices and its adjacent triangles
protected int[][] adjacentVerticesTriangles(Hashtable<Edge,EdgeInfo> adjacencyList)
{
	int[][] adjacencyVTList = new int[vertices.length][];
	int[] cardinalityMem = new int[vertices.length];
	for (int i = 0; i < cardinalityMem.length; i++) cardinalityMem[i] = 0;

	//count number of triangles per vertex
	for (int i = 0; i < triangles.length; i++)
	{
		cardinalityMem[triangles[i][0]] ++; 
		cardinalityMem[triangles[i][1]] ++; 
		cardinalityMem[triangles[i][2]] ++;
	}

	//init array for each vertex
	for (int i = 0; i < cardinalityMem.length; i++)
	{
		adjacencyVTList[i] = new int[cardinalityMem[i]];
		cardinalityMem[i] = 0;
	}

	//fill in adjacency list
	for (int i = 0; i < triangles.length; i++)
	{
		adjacencyVTList[triangles[i][0]][cardinalityMem[triangles[i][0]]++] = i;
		adjacencyVTList[triangles[i][1]][cardinalityMem[triangles[i][1]]++] = i;
		adjacencyVTList[triangles[i][2]][cardinalityMem[triangles[i][2]]++] = i;
	}

	//sort the adjacent faces for each vertex clockwise or counter-clockwise
	//(ok ... this is quadratic complexity ... however the number of adjacent faces will be relatively small)
	for (int i = 0; i < adjacencyVTList.length; i++)
	{
		int start = 0;
		int choice = 0;
		EdgeInfo testEdge = null;
		
		//if border goes through vertex, find it
		for (int m = 0; m < adjacencyVTList[i].length; m++)
		{
			for (int j = 0; j < 3; j++)
			{
				Edge dummyEdge = new Edge(i, triangles[adjacencyVTList[i][m]][j]);
				testEdge = adjacencyList.get(dummyEdge);
				if ((testEdge == null) || (testEdge.t2 < 0)) start = m;
			}
		}

		//if border triangle is not first in list, then switch
		if (start > 0)
		{
			int dummy = adjacencyVTList[i][0];
			adjacencyVTList[i][0] = adjacencyVTList[i][start];
			adjacencyVTList[i][start] = dummy;
		}

		//determine first choice
		if (testEdge != null)
		{
			if (triangles[adjacencyVTList[i][0]][0] == i)
			{
				if ((testEdge.e.v1 == triangles[adjacencyVTList[i][0]][1])
				|| (testEdge.e.v2 == triangles[adjacencyVTList[i][0]][1])) choice = 2;
				else choice = 1;
			}
			else if (triangles[adjacencyVTList[i][0]][1] == i)
			{
				if ((testEdge.e.v1 == triangles[adjacencyVTList[i][0]][0])
				|| (testEdge.e.v2 == triangles[adjacencyVTList[i][0]][0])) choice = 2;
				else choice = 0;
			}
			else
			{
				if ((testEdge.e.v1 == triangles[adjacencyVTList[i][0]][0])
				|| (testEdge.e.v2 == triangles[adjacencyVTList[i][0]][0])) choice = 1;
				else choice = 0;
			}
		}
		else if (triangles[adjacencyVTList[i][0]][0] == i) choice = 1;

		//sort triangles
		for (int m = 0; m < adjacencyVTList[i].length; m++)
		for (int n = m+1; n < adjacencyVTList[i].length; n++)
		{
			boolean found = false;

			if (triangles[adjacencyVTList[i][n]][0] == triangles[adjacencyVTList[i][m]][choice])
			{
				if (triangles[adjacencyVTList[i][n]][1] == i) choice = 2; else choice = 1;
				found = true;
			}
			else if (triangles[adjacencyVTList[i][n]][1] == triangles[adjacencyVTList[i][m]][choice])
			{
				if (triangles[adjacencyVTList[i][n]][0] == i) choice = 2; else choice = 0;
				found = true;
			}
			else if (triangles[adjacencyVTList[i][n]][2] == triangles[adjacencyVTList[i][m]][choice])
			{
				if (triangles[adjacencyVTList[i][n]][0] == i) choice = 2; else choice = 0;
				found = true;
			}

			if (found)
			{
				int dummy = adjacencyVTList[i][m+1];
				adjacencyVTList[i][m+1] = adjacencyVTList[i][n];
				adjacencyVTList[i][n] = dummy;
				break;
			}
		}
	}

	return adjacencyVTList;
}

//returns a list which associates the vertices and its adjacent triangles but
//with discontinuities eliminated
//attention: the triangle list is updated => the adjacencyList becomes invalid
protected int[][] eliminateDiscontinuities(Hashtable<Edge,EdgeInfo> adjacencyList, int[][] adjacencyVTList)
{
//	Edge dummyEdge = new Edge(0,0);
	
	LinkedList<int[]> vertexList = new LinkedList<int[]>();
	LinkedList<Integer> indexList = new LinkedList<Integer>();
	
	//for each vertex
	for (int k = 0; k < adjacencyVTList.length; k++)
	{
		int start = 0;

		//if there is a boundary edge running through the vertex then we have to start there,
		//else we must search for the first discontinuity as a start point
		//and if there is even no discontinuity, then it doesnt matter where we start
		boolean noBoundary = true;
		for (int j = 0; j < 3; j++)
		{
			Edge dummyEdge = new Edge(k, triangles[adjacencyVTList[k][0]][j]);
			EdgeInfo testEdge = adjacencyList.get(dummyEdge);
			if (testEdge != null)
			{
				if (testEdge.t2 == -1) noBoundary = false;
			}
		}
		
		//find a start point from where we can collect the new vertex triangle neighbourhoods
		//remember: if a discontinuity runs through an old vertex then actually there must be two vertices,
		//          one for the neighbourhood on each side of the discontinuity
		if (noBoundary)
		for (int i = 0; i < adjacencyVTList[k].length; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				Edge dummyEdge = new Edge(k, triangles[adjacencyVTList[k][i]][j]);
				EdgeInfo testEdge = adjacencyList.get(dummyEdge);
				if ((testEdge != null) && (testEdge.discontinuity))
				{
					if ((testEdge.t1 == adjacencyVTList[k][i])
						&& (testEdge.t2 == adjacencyVTList[k][i+1])) start = i+1;
					else if ((testEdge.t2 == adjacencyVTList[k][i])
						&& (testEdge.t1 == adjacencyVTList[k][i+1])) start = i+1;
					else start = i;
					
					//$ANALYSIS-IGNORE codereview.java.rules.loop.RuleLoopAssignLoopVariable
					i=adjacencyVTList[k].length;
					
					break;
				}
			}
		}

		//collect neighbourhoods and insert one new vertex for each
		int current = start;
		int len = adjacencyVTList[k].length;
		for (int i = 0; i < len; i++)
		{
			Edge dummyEdge = null;
			for (int j = 0; j < 3; j++)
			{
				dummyEdge = new Edge(k, triangles[adjacencyVTList[k][current%len]][j]);
				EdgeInfo testEdge = adjacencyList.get(dummyEdge);
				if ((testEdge != null) && (testEdge.discontinuity))
				{
					if (((testEdge.t1 == adjacencyVTList[k][current%len])
						&& (testEdge.t2 == adjacencyVTList[k][(current+1)%len]))
					|| ((testEdge.t2 == adjacencyVTList[k][current%len])
						&& (testEdge.t1 == adjacencyVTList[k][(current+1)%len])))
					{
						break;
					}
				}
				dummyEdge = null;
			}
			
			if ((dummyEdge != null) || (i == len-1))
			{
				int[] vertex = new int[current - start + 1];
				int n = 0;
				for (int m = start; m <= current; m++)
					vertex[n++] = adjacencyVTList[k][m%len];
				vertexList.add(vertex);
				indexList.add(new Integer(k));
				start = current + 1;
			}
			
			current++;
		}
	}

	//create a new list of adjacencies and update the triangles and vertices
	int[][] newAdjacencyVTList = new int[vertexList.size()][];
	float[][] newVertices = new float[vertexList.size()][3];
	float[][] newTexcoords = new float[vertexList.size()][2];
	int[][] newTriangles = new int[triangles.length][3];
	
	ListIterator it1 = vertexList.listIterator(0);
	ListIterator it2 = indexList.listIterator(0);

	int currentIndex = 0;
	while (it1.hasNext())
	{		
		int[] vertex = (int[])it1.next();
		int index = ((Integer)it2.next()).intValue();
		newAdjacencyVTList[currentIndex] = vertex;
		newVertices[currentIndex][0] = vertices[index][0];
		newVertices[currentIndex][1] = vertices[index][1];
		newVertices[currentIndex][2] = vertices[index][2];
		if (texcoords != null)
			newTexcoords[currentIndex] = texcoords[index];
		
		//update triangle indices and vertices
		for (int i = 0; i < vertex.length; i++)
		{
			if (triangles[vertex[i]][0] == index)
				newTriangles[vertex[i]][0] = currentIndex;
			else if (triangles[vertex[i]][1] == index)
				newTriangles[vertex[i]][1] = currentIndex;
			else
				newTriangles[vertex[i]][2] = currentIndex;
		}
		
		currentIndex++;
	}
	
	vertices = newVertices;
	triangles = newTriangles;
	texcoords = texcoords != null ? newTexcoords : texcoords;
	
	return newAdjacencyVTList;
}

//returns a list with one normal per vertex
protected void normalList(int[][] adjacencyVTList)
{
	normals = new float[adjacencyVTList.length][3];

	for (int i = 0; i < adjacencyVTList.length; i++)
	{
		double[] normal = {0.0, 0.0, 0.0};
		
		for (int j = 0; j < adjacencyVTList[i].length; j++)
		{
			double[] nextNormal = triangleUnNormal(adjacencyVTList[i][j]);
			normal[0] += nextNormal[0]; normal[1] += nextNormal[1]; normal[2] += nextNormal[2];
		}
		
		normalize(normal);
		
		normals[i][0] = (float)normal[0];
		normals[i][1] = (float)normal[1];
		normals[i][2] = (float)normal[2]; 
	}
}

protected double[] triangleUnNormal(int triangle)
{
	double[] v1 = new double[3];
	double[] v2 = new double[3];
	
	v1[0] = (double)vertices[triangles[triangle][1]][0] - (double)vertices[triangles[triangle][0]][0];
	v1[1] = (double)vertices[triangles[triangle][1]][1] - (double)vertices[triangles[triangle][0]][1];
	v1[2] = (double)vertices[triangles[triangle][1]][2] - (double)vertices[triangles[triangle][0]][2];
	
	v2[0] = (double)vertices[triangles[triangle][2]][0] - (double)vertices[triangles[triangle][0]][0];
	v2[1] = (double)vertices[triangles[triangle][2]][1] - (double)vertices[triangles[triangle][0]][1];
	v2[2] = (double)vertices[triangles[triangle][2]][2] - (double)vertices[triangles[triangle][0]][2];
	
	double[] normal = cross(v1, v2);
	return normal;
}

protected double[] triangleNormal(int triangle)
{
	double[] normal = triangleUnNormal(triangle);
	normalize(normal);
	return normal;
}

protected double dot(double[] v1, double[] v2)
{
	return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
}

protected double[] cross(double[] v1, double[] v2)
{
	double[] v = new double[3];

	v[0] = v1[1]*v2[2] - v1[2]*v2[1];
	v[1] = v1[2]*v2[0] - v1[0]*v2[2];
	v[2] = v1[0]*v2[1] - v1[1]*v2[0];

	return v;
}

protected void normalize(double[] v)
{
	double length = Math.sqrt(dot(v, v));
	if (length > 0.0) {v[0] /= length; v[1] /= length; v[2] /= length;}
}

}
