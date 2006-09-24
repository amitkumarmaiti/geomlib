// arch-tag: 66ff6c12-90a0-4540-b53c-6e3cd7e64164
package de.yvert.algorithms;

import java.util.Hashtable;

import junit.framework.TestCase;
import de.yvert.algorithms.VertexNormalCalculator.Edge;
import de.yvert.algorithms.VertexNormalCalculator.EdgeInfo;

public class VertexNormalCalculatorTest extends TestCase
{

public void testSimple()
{
	float[][] vertices = new float[4][3];
	vertices[0][0] = 0;
	vertices[0][1] = 0;
	vertices[0][2] = 0;
	
	vertices[1][0] = 1;
	vertices[1][1] = 1;
	vertices[1][2] = 0;
	
	vertices[2][0] = 1;
	vertices[2][1] = -1;
	vertices[2][2] = 0;
	
	vertices[3][0] = 1;
	vertices[3][1] = 0;
	vertices[3][2] = 1;
	
	int[][] triangles = new int[2][3];
	triangles[0][0] = 0;
	triangles[0][1] = 1;
	triangles[0][2] = 2;
	
	triangles[1][0] = 1;
	triangles[1][1] = 3;
	triangles[1][2] = 2;
	
	VertexNormalCalculator calc = new VertexNormalCalculator(vertices, triangles, new float[vertices.length][3]);
	Hashtable<Edge,EdgeInfo> adjacencyList = calc.adjacentTriangles();
	assertEquals(5, adjacencyList.size());
	assertTrue(adjacencyList.containsKey(new Edge(1, 3)));
	assertTrue(adjacencyList.containsKey(new Edge(1, 2)));
	assertTrue(adjacencyList.containsKey(new Edge(2, 3)));
	assertTrue(adjacencyList.containsKey(new Edge(0, 2)));
	assertTrue(adjacencyList.containsKey(new Edge(0, 1)));
	
	int[][] adjacencyVTList = calc.adjacentVerticesTriangles(adjacencyList);
	assertEquals(4, adjacencyVTList.length);
	assertEquals(1, adjacencyVTList[0].length);
	assertEquals(2, adjacencyVTList[1].length);
	assertEquals(2, adjacencyVTList[2].length);
	assertEquals(1, adjacencyVTList[3].length);
	assertEquals(0, adjacencyVTList[0][0]);
	assertEquals(1, adjacencyVTList[3][0]);
	assertTrue(((adjacencyVTList[1][0] == 0) && (adjacencyVTList[1][1] == 1)) ||
			       ((adjacencyVTList[1][0] == 1) && (adjacencyVTList[1][1] == 0)));
	assertTrue(((adjacencyVTList[2][0] == 0) && (adjacencyVTList[2][1] == 1)) ||
			       ((adjacencyVTList[2][0] == 1) && (adjacencyVTList[2][1] == 0)));
/*	for (int i = 0; i < adjacencyVTList.length; i++)
	{
		for (int j = 0; j < adjacencyVTList[i].length; j++)
			System.out.print(adjacencyVTList[i][j]+", ");
		System.out.println();
	}*/
	
	int[][] newAdjList = calc.eliminateDiscontinuities(adjacencyList, adjacencyVTList);
	
	assertEquals(6, newAdjList.length);
	assertEquals(1, newAdjList[0].length); assertEquals(0, newAdjList[0][0]);
	assertEquals(1, newAdjList[1].length); assertEquals(1, newAdjList[1][0]);
	assertEquals(1, newAdjList[2].length); assertEquals(0, newAdjList[2][0]);
	assertEquals(1, newAdjList[3].length); assertEquals(1, newAdjList[3][0]);
	assertEquals(1, newAdjList[4].length); assertEquals(0, newAdjList[4][0]);
	assertEquals(1, newAdjList[5].length); assertEquals(1, newAdjList[5][0]);
	
	int[][] newtris = calc.getTriangles();
	assertEquals(2, newtris.length);
	assertEquals(3, newtris[0].length);
	assertEquals(0, newtris[0][0]);
	assertEquals(2, newtris[0][1]);
	assertEquals(4, newtris[0][2]);
	assertEquals(3, newtris[1].length);
	assertEquals(1, newtris[1][0]);
	assertEquals(5, newtris[1][1]);
	assertEquals(3, newtris[1][2]);
	
	calc.normalList(newAdjList);
	
//	float[][] newverts = calc.getVertices();
//	float[][] newnormals = calc.getNormals();
	
/*	for (int i = 0; i < newverts.length; i++)
		System.out.println(newverts[i][0]+" "+newverts[i][1]+" "+newverts[i][2]);
	System.out.println();
	for (int i = 0; i < newtris.length; i++)
		System.out.println(newtris[i][0]+" "+newtris[i][1]+" "+newtris[i][2]);
	System.out.println();
	for (int i = 0; i < newnormals.length; i++)
		System.out.println(newnormals[i][0]+" "+newnormals[i][1]+" "+newnormals[i][2]);*/
}

}
