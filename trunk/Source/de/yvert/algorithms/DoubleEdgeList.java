// arch-tag: 4580f414-18d3-4bf3-9b2c-b8058ebe24db
package de.yvert.algorithms;

import java.util.HashMap;

import de.yvert.geometry.Vector2;

/**
 * Implements a double-linked edge list.
 * 
 * @author Ulf Ochsenfahrt
 */
public class DoubleEdgeList
{

public static final String COORDINATES_KEY = "coords";

	public class Item
	{
		HashMap<String,Object> attributes = new HashMap<String,Object>();
		
		public Object getAttribute(String key)
		{ return attributes.get(key); }
		
		public Object setAttribute(String key, Object o)
		{ return attributes.put(key, o); }
	}

	public class Vertex extends Item
	{
		HalfEdge incidentEdge;
		
		public HalfEdge incidentEdge()
		{ return incidentEdge; }
		
/*		public HalfEdge makeVertexEdge(Face leftFace, Face rightFace)
		{
			HalfEdge topLeft = incidentEdge;
			while (topLeft.leftFace != leftFace)
				topLeft = topLeft.twin.next;
			
			HalfEdge bottomRight = incidentEdge;
			while (bottomRight.leftFace != rightFace)
				bottomRight = bottomRight.twin.next;
			
			HalfEdge result = new HalfEdge();
			HalfEdge twin = new HalfEdge();
			
			
			
			return result;
		}*/
	}
	
	public class HalfEdge extends Item
	{
		Vertex origin;
		HalfEdge twin;
		HalfEdge next, prev;
		Face leftFace;
		
		// linked list of all halfedges
		HalfEdge llnext, llprev;
		// linked list of inner components
		HalfEdge icnext, icprev;
		
		public Vertex origin()
		{ return origin; }
		
		public Vertex destination()
		{ return twin.origin; }
		
		public HalfEdge twin()
		{ return twin; }
		
		public HalfEdge next()
		{ return next; }
		
		public HalfEdge prev()
		{ return prev; }
		
		public Face leftFace()
		{ return leftFace; }
		
		public Face rightFace()
		{ return twin.leftFace; }
		
		public HalfEdge makeFaceEdge(HalfEdge destination)
		{
			HalfEdge e1 = new HalfEdge();
			HalfEdge e2 = new HalfEdge();
			
			e1.origin = this.origin;
			e1.twin = e2;
			e1.next = destination;
			e1.prev = prev;
			
			e2.origin = destination.origin;
			e2.twin = e1;
			e2.next = this;
			e2.prev = destination.prev;
			
			this.prev.next = e1;
			this.prev = e2;
			
			destination.prev.next = e2;
			destination.prev = e1;
			return e1;
		}
		
/*		private void deleteHalfEdge()
		{
			// if prev.twin == this then
			//   the vertex is gone after the deleteEdge operation
			if (origin.incidentEdge == this)
				origin.incidentEdge = prev.twin;
			
			// remove this HalfEdge from the ring around leftFace
			next.prev = prev;
			prev.next = next;
			
			// remove this HalfEdge from the ring of all HalfEdges
			llnext.llprev = llprev;
			llprev.llnext = llnext;
			if (leftFace.outerComponent == this)
				leftFace.outerComponent = next;
			
			// if this HalfEdge is part of a component ring
			if (icnext != null)
			{
				next.icnext = icnext;
				next.icprev = icprev;
				if (icnext != this)
				{
					icnext.icprev = next;
					icprev.icnext = next;
				}
				if (leftFace.innerComponents == this)
					leftFace.innerComponents = icnext;
			}
		}
		
		// Merges the origin and destination vertices
		// by deleting this HalfEdge and its twin
		// Maintains the Euler condition V-E+F = 2 (if applicable)
		// the destination vertex is gone after the operation
		public void killVertexEdge()
		{
			deleteHalfEdge();
			twin.deleteHalfEdge();
		}*/
	}
	
	public class Face extends Item
	{
		HalfEdge outerComponent;
		HalfEdge innerComponents;
		
		public HalfEdge outerComponent()
		{ return outerComponent; }
		
		public HalfEdge innerComponents()
		{ return innerComponents; }
		
/*		public HalfEdge makeFaceEdge(Vertex origin, Vertex destination)
		{
			HalfEdge result = new HalfEdge();
			HalfEdge twin = new HalfEdge();
			
			Face newFace = new Face();
			
			result.origin = origin;
			twin.origin = destination;
			
			result.twin = twin;
			twin.twin = twin;
			
			result.leftFace = this;
			twin.leftFace = newFace;
			
			HalfEdge edge = origin.incidentEdge;
			while (edge.leftFace != this)
				edge = edge.twin.next;
			
			twin.next = edge;
			result.prev = edge.prev;
			
			edge.prev.next = result;
			edge.prev = twin;
			
			edge = destination.incidentEdge;
			while (edge.leftFace != this)
				edge = edge.twin.next;
			
			
			
			return result;
		}*/
		
/*		public void removeInnerComponent(HalfEdge edge)
		{
			if (edge.leftFace != this)
				throw new RuntimeException("Edge is not an inner component of this face!");
			HalfEdge find = edge;
			while (find.icnext == null)
			{
				find = find.next;
				if (find == edge)
					throw new RuntimeException("Sanity check failed: edge is an inner component but not in list!");
			}
			find.icnext.icprev = find.icprev;
			find.icprev.icnext = find.icnext;
			if (innerComponents == edge)
				innerComponents = innerComponents.icnext;
		}*/
		
		public void addInnerComponent(HalfEdge edge)
		{
			if (innerComponents == null)
			{
				edge.icnext = edge;
				edge.icprev = edge;
				innerComponents = edge;
			}
			else
			{
				edge.icnext = innerComponents.icnext;
				edge.icprev = innerComponents;
				edge.icprev.icnext = edge;
				edge.icnext.icprev = edge;
			}
		}
	}

private Face area;

private Vertex someVertex;
private HalfEdge someHalfEdge;

/**
 * Creates an empty doubly-linked edge list, i.e. it only contains a single 
 * unbounded face.
 */
public DoubleEdgeList()
{
	area = new Face();
	area.outerComponent = null;
	area.innerComponents = null;
	someVertex = null;
	someHalfEdge = null;
}

public Vertex getSomeVertex()
{ return someVertex; }

public HalfEdge getSomeHalfEdge()
{ return someHalfEdge; }

public DoubleEdgeList(Vector2[] points)
{
	Vertex[] vertices = new Vertex[points.length];
	for (int i = 0; i < points.length; i++)
	{
		Vertex vi = new Vertex();
		vi.setAttribute(COORDINATES_KEY, points[i]);
	}
	
	area = new Face();
	Face polygon = new Face();
	
	HalfEdge[] edges = new HalfEdge[2*points.length];
	for (int i = 0; i < points.length; i++)
	{
		HalfEdge edge1 = new HalfEdge();
		HalfEdge edge2 = new HalfEdge();
		edge1.origin = vertices[i];
		edge2.origin = vertices[(i+1) % points.length];
		
		edge1.twin = edge2;
		edge2.twin = edge1;
		
		edge1.leftFace = area;
		edge2.leftFace = polygon;
		
		edges[2*i+0] = edge1;
		edges[2*i+1] = edge2;
	}
	
	for (int i = 0; i < edges.length; i++)
	{
		edges[i].llnext = edges[(i+1) % edges.length];
		edges[i].llprev = edges[(i-1+edges.length) % edges.length];
	}
	
	for (int i = 0; i < points.length; i++)
		vertices[i].incidentEdge = edges[i];
	
	for (int i = 0; i < points.length; i++)
	{
		edges[2*i+0].prev = edges[2*((i-1+points.length) % points.length)+0];
		edges[2*i+0].next = edges[2*((i+1) % points.length)+0];
		
		edges[2*i+1].prev = edges[2*((i-1+points.length) % points.length)+1];
		edges[2*i+1].next = edges[2*((i+1) % points.length)+1];
	}
	
	area.outerComponent = null;
	area.innerComponents = null;
	area.addInnerComponent(edges[0]);
	
	someVertex = vertices[0];
	someHalfEdge = edges[0];
	
	polygon.outerComponent = edges[1];
	polygon.innerComponents = null;
}

public Face area()
{ return area; }

}
