// arch-tag: 5ee5df17-db7e-40c7-94e2-8a50bae5e2cc
package de.yvert.algorithms.triangulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

import de.yvert.algorithms.DaTreeSet;
import de.yvert.algorithms.Finder;
import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;

/**
 * Triangulates a simple polygon.
 * <p>
 * The algorithm is taken from:
 * <pre>
 * Computational Geometry: Algorithms and Applications
 * Second Edition
 * M. de Berg and M. van Kreveld and M. Overmars and O. Schwarzkopf
 * Springer-Verlag
 * ISBN: 3-540-65620-0
 * </pre>
 * 
 * @author Ulf Ochsenfahrt
 */
public class PolygonTriangulator2D
{

HalfEdge statice;

public PolygonTriangulator2D()
{/*OK*/}

static final boolean above(Vertex v1, Vertex v2)
{
	Vector2 p1 = v1.value, p2 = v2.value;
	if (p1.getY() < p2.getY()) return false;
	if (p1.getY() > p2.getY()) return true;
	if (p1.getX() < p2.getX()) return true;
	if (p1.getX() > p2.getX()) return false;
	if (v1.orig < v2.orig) return true;
	if (v1.orig > v2.orig) return false;
	throw new RuntimeException("Identical: "+p1+" "+p2);
}

/**
 * Determines if the given points are in clockwise order.
 */
static int isClockwise(Vector2 a, Vector2 b, Vector2 c)
{
	double det = b.getX()*c.getY()+a.getX()*b.getY()+a.getY()*c.getX();
	det -= b.getY()*c.getX()+a.getX()*c.getY()+a.getY()*b.getX();
	if (det < 0) return 1;
	if (det > 0) return -1;
	return 0;
}

static int isClockwise(Vertex a, Vertex b, Vertex c)
{ return isClockwise(a.value, b.value, c.value); }

/**
 * Determines if the given points form a right turn at the second point.
 * This is equivalent to them being in clockwise order.
 * If the points are collinear, this method returns false.
 */
static boolean isRightTurn(Vector2 a, Vector2 b, Vector2 c)
{ return isClockwise(a, b, c) > 0; }

static boolean isRightTurn(Vertex a, Vertex b, Vertex c)
{ return isRightTurn(a.value, b.value, c.value); }

/**
 * Comparator used to sort the vertices in top-down order. Vertices with
 * identical y coordinate are sorted left-right.
 */
static final Comparator<Vertex> TOP_DOWN_COMPARATOR = new Comparator<Vertex>()
	{
		public int compare(Vertex v1, Vertex v2)
		{
			if (v1 == v2) return 0;
			if (above(v1, v2)) return -1;
			return 1;
		}
	};

static final Comparator<Edge> LEFT_RIGHT_COMPARATOR = new Comparator<Edge>()
	{
		public int compare(Edge e1, Edge e2)
		{
			if (e1 == e2) return 0;
			
			int e1a = isClockwise(e1.start, e1.end, e2.start);
			int e1b = isClockwise(e1.start, e1.end, e2.end);
			if (e1a*e1b == 1) return e1a;
			if ((e1a != 0) && (e1b == 0)) return e1a;
			if ((e1a == 0) && (e1b != 0)) return e1b;
			
			int e2a = isClockwise(e2.start, e2.end, e1.start);
			int e2b = isClockwise(e2.start, e2.end, e1.end);
			if (e2a*e2b == 1) return -e2a;
			if ((e2a != 0) && (e2b == 0)) return -e2a;
			if ((e2a == 0) && (e2b != 0)) return -e2b;
			
			if ((e1a == 0) && (e1b == 0) && (e2a == 0) && (e2b == 0))
			{
				if (above(e1.start, e2.start) && 
						(above(e1.end, e2.start) || e1.end.value.same(e2.start.value)))
					return -1;
				if (above(e2.start, e1.start) &&
						(above(e2.end, e1.start) || e2.end.value.same(e1.start.value)))
					return 1;
				throw new RuntimeException("All 0: "+e1+" -> "+e2);
			}
			else if ((e1a != e1b) && (e2a != e2b))
			{
				throw new RuntimeException("All 1: "+e1+" -> "+e2);
			}
			
			throw new RuntimeException(e1+" -> "+e2+" "+e1a+" "+e1b+" "+e2a+" "+e2b);
		}
	};

static final Finder<Vertex,Edge> LEFT_RIGHT_FINDER = new Finder<Vertex,Edge>()
	{
		public int compare(Vertex n, Edge h)
		{ return -isClockwise(h.start, h.end, n); }
	};

static VertexState label(Vertex a, Vertex b, Vertex c)
{
	if (above(a, b))
	{
		if (above(b, c))
			return VertexState.REGULAR_LEFT;
		else
		{
			if (isRightTurn(a, b, c))
				return VertexState.MERGE;
			else
				return VertexState.END;
		}
	}
	else
	{
		if (above(b, c))
		{
			if (isRightTurn(a, b, c))
				return VertexState.SPLIT;
			else
				return VertexState.START;
		}
		else
			return VertexState.REGULAR_RIGHT;
	}
}

/**
 * Adds two halfedges between the origin of a and the origin of b.
 * 
 * ^           +  b       ^       e2  +  b
 *  \          |           \         ^|
 *   \         |            \       / |
 *    \        |    =>       \     /  |
 *     \       |              \   /   |
 *      \      |               \ v    |
 *    a  +     v             a  + e1  v
 * 
 * e1 is the new edge a -> b (same origin as a)
 * e2 is the new edge b -> a (same origin as b)
 * 
 * Modifies a, b, the predecessor of a and the predecessor of b apropriately.
 */
static void addEdge(HalfEdge a, HalfEdge b, TriangulationListener listener)
{
	// create two new halfedges
	HalfEdge e1 = new HalfEdge();
	HalfEdge e2 = new HalfEdge();
	
	// initialize e1 (a -> b)
	e1.origin = a.origin;
	e1.twin = e2;
	e1.next = b;
	e1.prev = a.prev;
	
	// initialize e2 (b -> a)
	e2.origin = b.origin;
	e2.twin = e1;
	e2.next = a;
	e2.prev = b.prev;
	
	// correct a and pred(a)
	a.prev.next = e1;
	a.prev = e2;
	
	// correct b and pred(b)
	b.prev.next = e2;
	b.prev = e1;
	
	if (listener != null)
		listener.addEdge(a.origin.orig, b.origin.orig);
}

private static void addTriangle(Vertex a, Vertex b, Vertex c, TriangulationListener listener)
{
	listener.addTriangle(a.orig, b.orig, c.orig);
}

private void triangulateMonotone(HalfEdge e, TriangulationListener listener)
{
//	System.out.println("MONOTONE");
//	if (true) return;
	HalfEdge f = e.next;
	HalfEdge top = e;
	while (f != e)
	{
		if (above(f.origin, top.origin))
			top = f;
		f = f.next;
	}
	
	ArrayList<HalfEdge> vertices = new ArrayList<HalfEdge>();
	vertices.add(top);
	
	HalfEdge left = top.next;
	HalfEdge right = top.prev;
	while (left != right)
	{
		if (above(left.origin, right.origin))
		{
			left.origin.isLeft = true;
			vertices.add(left);
			left = left.next;
		}
		else
		{
			right.origin.isLeft = false;
			vertices.add(right);
			right = right.prev;
		}
	}
	vertices.add(right);
	
//	for (int i = 0; i < vertices.size(); i++)
//		System.out.println(vertices.get(i).origin.orig);
	
	Stack<HalfEdge> stack = new Stack<HalfEdge>();
	stack.push(vertices.get(0));
	stack.push(vertices.get(1));
	for (int j = 2; j < vertices.size()-1; j++)
	{
		HalfEdge uj = vertices.get(j);
		if (uj.origin.isLeft != stack.peek().origin.isLeft)
		{
			HalfEdge ujm1 = stack.peek();
			
			while (stack.size() > 1)
			{
				HalfEdge newTemp = stack.pop();
				addEdge(uj, newTemp, listener);
				
				HalfEdge temp = stack.peek();
				if (newTemp.origin.isLeft)
					addTriangle(uj.origin, temp.origin, newTemp.origin, listener);
				else
					addTriangle(uj.origin, newTemp.origin, temp.origin, listener);
			}
			
			stack.pop();
			ujm1 = ujm1.prev.twin;
			stack.push(ujm1);
			stack.push(uj);
		}
		else
		{
			HalfEdge temp = stack.pop();
			int factor = uj.origin.isLeft ? -1 : 1;
			while (!stack.empty() && (factor*isClockwise(uj.origin, temp.origin, stack.peek().origin) < 0))
			{
				HalfEdge newTemp = stack.pop();
				addEdge(uj, newTemp, listener);
				if (temp.origin.isLeft)
					addTriangle(newTemp.origin, temp.origin, uj.origin, listener);
				else
					addTriangle(temp.origin, newTemp.origin, uj.origin, listener);
				uj = uj.prev.twin;
				temp = newTemp;
			}
			stack.push(temp);
			stack.push(uj);
		}
	}
	
	// TODO: Correct?
	if (stack.size() > 2)
	{
		HalfEdge uj = vertices.get(vertices.size()-1);
		HalfEdge temp = stack.pop();
		while (stack.size() > 1)
		{
			HalfEdge newTemp = stack.pop();
			addEdge(uj, newTemp, listener);
			if (temp.origin.isLeft)
				addTriangle(newTemp.origin, temp.origin, uj.origin, listener);
			else
				addTriangle(temp.origin, newTemp.origin, uj.origin, listener);
			temp = newTemp;
		}
		
		HalfEdge newTemp = stack.pop();
		if (temp.origin.isLeft)
			addTriangle(newTemp.origin, temp.origin, uj.origin, listener);
		else
			addTriangle(temp.origin, newTemp.origin, uj.origin, listener);
	}
	else
	{
		HalfEdge uj = vertices.get(vertices.size()-1);
		HalfEdge temp = stack.pop();
		HalfEdge newTemp = stack.pop();
		if (temp.origin.isLeft)
			addTriangle(newTemp.origin, temp.origin, uj.origin, listener);
		else
			addTriangle(temp.origin, newTemp.origin, uj.origin, listener);
	}
//	System.out.println("FINISH");
}

private void calculate(TriangulationListener listener, Vertex[] vertices,
		Vertex[] sortedVertices, HalfEdge[] halfEdges, Edge[] edges)
{
	statice = halfEdges[0];
	
	// execute the algorithm
	DaTreeSet<Edge> tree = new DaTreeSet<Edge>(Edge.class, LEFT_RIGHT_COMPARATOR);
	for (int j = 0; j < sortedVertices.length; j++)
	{
		Vertex vi = sortedVertices[j];
		Edge ei = vi.incidentEdge;
		Edge eim1 = vi.previousEdge;
		
//		System.out.println(vi+" "+vi.state);
		
		switch (vi.state)
		{
			/* A START vertex v_i looks like this:
			 * 
			 *    outside
			 * 
			 *       + v_i
			 * e_i  / ^
			 *     /   \  e_{i-1}
			 *    v     \
			 *           +  v_{i-1}
			 *    inside
			 * 
			 * For e_i, we need to store the helper vertex, as well as the helper halfedge,
			 * that is the halfedge starting at the helper vertex that this edge corresponds to.
			 * 
			 * e_i must be added to the tree.
			 */
			case START :
				{
					ei.helper = vi;
					ei.helperEdge = ei.partner;
					tree.add(ei);
				}
				break;
			
			/* An END vertex v_i looks like this:
			 * 
			 * v_{i-1}  +  inside               v_{i-1}  +  h_{i-1}
			 *           \         ^                      \    +    ^
			 *            \       /        ?               \   ^   /
			 *    e_{i-1}  \     /  e_i    =>      e_{i-1}  \  |  /  e_i
			 *              \   /                            \ | /
			 *               v /                              vv/
			 *     outside    +  v_i                           +  v_i
			 * 
			 * If the helper of e_{i-1} is a MERGE vertex, then we need to create a new edge between
			 * the helper halfedge of e_{i-1} and e_i. In this case, we close a monotone polygon and
			 * therefore have to triangulate it.
			 * 
			 * In any case, we close a monotone polygon and therefore have to triangulate it (we may
			 * therefore close two monoton polygons).
			 * 
			 * In any case, the old edge e_{i-1} must now be removed from the tree.
			 */
			case END :
				{
					if (eim1.helper.state == VertexState.MERGE)
					{
						addEdge(ei.partner, eim1.helperEdge, listener);
						triangulateMonotone(eim1.partner, listener);
					}
					
					triangulateMonotone(ei.partner, listener);
					
					if (!tree.remove(eim1)) throw new RuntimeException("MUST BE IN THE TREE!");
				}
				break;
			
			/* A SPLIT vertex v_i looks like this:
			 * 
			 *                                         <---+ h_j
			 *                                             ^
			 *           inside                            |
			 *                                             v
			 *              +  v_i                         +  v_i
			 *             ^ \                            ^ \
			 *   e_{i-1}  /   \  e_i   !        e_{i-1}  /   \  e_i
			 *           /     \       =>               /     \
			 * v_{i-1}  +       v             v_{i-1}  +       v
			 *           outside
			 * 
			 * We must find the left neighbour e_j of v_i and create a new edge between the helper 
			 * of that neighbour and e_i. The new helper of e_j is now v_i. The new helper halfedge
			 * of e_j is the newly created halfedge (v_i -> h_j).
			 * 
			 * e_i must be added to the tree.
			 */
			case SPLIT :
				{
					Edge ej = tree.findLeftNeighbour(vi, LEFT_RIGHT_FINDER);
					addEdge(ei.partner, ej.helperEdge, listener);
					ej.helper = vi;
					ej.helperEdge = ej.helperEdge.prev;
					
					ei.helper = vi;
					ei.helperEdge = ei.partner;
					tree.add(ei);
				}
				break;
			
			/* A MERGE vertex v_i looks like this:
			 * 
			 *     outside  +  v_{i-1}                    outside  +  v_{i-1}
			 *   ^         /                            ^         /
			 *    \       /                ?    inside   \       /           inside
			 *     \     /  e_{i-1}        =>             \     /  e_{i-1}
			 * e_i  \   /                             e_i  \   /
			 *       \ v                                    \ v
			 *   v_i  +  inside              h_j  +<-------->+<-------->+ h_{i-1}
			 *                                              v_i
			 * 
			 * If the helper of e_{i-1} is a MERGE vertex, then we need to create a new edge between
			 * the helper halfedge of e_{i-1} and e_{i-1}. In this case, we close a monotone polygon 
			 * and therefore have to triangulate it.
			 * 
			 * We must further find the left neighbour e_j of v_i. If the helper of e_j is a MERGE
			 * vertex, then we need to create a new edge between the helper halfedge of e_j and e_i.
			 * The new helper of e_j is now v_i. The new helper halfedge of e_j is the newly created 
			 * halfedge (v_i -> h_j).
			 * In this case, we close a monotone polygon and therefore have to triangulate it.
			 * 
			 * In any case, we must remove e_{i-1} from the tree.
			 */
			case MERGE :
				{
					if (eim1.helper.state == VertexState.MERGE)
					{
						addEdge(ei.partner, eim1.helperEdge, listener);
						triangulateMonotone(eim1.partner, listener);
					}
					if (!tree.remove(eim1)) throw new RuntimeException("MUST BE IN THE TREE!");
					
					Edge ej = tree.findLeftNeighbour(vi, LEFT_RIGHT_FINDER);
					if (ej.helper.state == VertexState.MERGE)
					{
						addEdge(ei.partner, ej.helperEdge, listener);
						ej.helper = vi;
						ej.helperEdge = ej.helperEdge.prev;
						
						triangulateMonotone(ei.partner, listener);
					}
					else
					{
						ej.helper = vi;
						ej.helperEdge = ei.partner;
					}
				}
				break;
			
			/* A REGULAR_LEFT vertex v_i looks like this:
			 * 
			 *  v_{i-1}  +  inside              v_{i-1}  +       +  h_{i-1}
			 *            \                               \     ^
			 *    e_{i-1}  \                      e_{i-1}  \   /
			 *              v            ?                  v v
			 * outside  v_i  +           =>             v_i  +
			 *              /                               /
			 *        e_i  v                          e_i  v
			 * 
			 * If the helper of e_{i-1} is a MERGE vertex, then we need to create a new edge between
			 * the helper halfedge of e_{i-1} and e_{i-1}. In this case, we close a monotone polygon 
			 * and therefore have to triangulate it.
			 * 
			 * In any case, e_{i-1} must be removed from the tree and e_i must be added to the tree.
			 */
			case REGULAR_LEFT :
				{
					if (eim1.helper.state == VertexState.MERGE)
					{
						addEdge(ei.partner, eim1.helperEdge, listener);
						triangulateMonotone(eim1.partner, listener);
					}
					
					if (!tree.remove(eim1)) throw new RuntimeException("MUST BE IN THE TREE!");
					
					ei.helper = vi;
					ei.helperEdge = ei.partner;
					tree.add(ei);
				}
				break;
			
			/* A REGULAR_RIGHT vertex looks like this:
			 * 
			 *                             h_j  +
			 *          ^                        ^     ^
			 * inside  /  e_i                     \   /  e_i
			 *        /                 ?          v /
			 *       +  v_i             =>          +  v_i
			 *       ^                              ^
			 *       |  e_{i-1}                     |  e_{i-1}
			 *       |                              |
			 *       +  v_{i-1}                     +  v_{i-1}
			 * 
			 * We must find the left neighbour e_j of v_i. If the helper of e_j is a MERGE vertex,
			 * then we need to create a new edge between the helper halfedge of e_j and e_i. The 
			 * new helper of e_j is now v_i. The new helper halfedge of e_j is the newly created 
			 * halfedge (v_i -> h_j). In this case, we close a monotone polygon and therefore have
			 * to triangulate it.
			 * 
			 * Otherwise, v_i becomes the new helper of e_j.
			 */
			case REGULAR_RIGHT :
				{
					Edge ej = tree.findLeftNeighbour(vi, LEFT_RIGHT_FINDER);
					if (ej.helper.state == VertexState.MERGE)
					{
						addEdge(ei.partner, ej.helperEdge, listener);
						ej.helper = vi;
						ej.helperEdge = ej.helperEdge.prev;
						
						triangulateMonotone(ei.partner, listener);
					}
					else
					{
						ej.helper = vi;
						ej.helperEdge = ei.partner;
					}
				}
				break;
			
			default :
				throw new RuntimeException("Internal error!");
		}
	}
}

private void calculate(Vertex[] vertices, TriangulationListener listener)
{
	int count = vertices.length;
	
	// sort vertices in top-down order
	Vertex[] sortedVertices = vertices.clone();
	Arrays.sort(sortedVertices, TOP_DOWN_COMPARATOR);
	
	if (sortedVertices[0].state == VertexState.SPLIT)
	{
		listener.reversing();
		for (int i = 0; i < vertices.length/2; i++)
		{
			Vertex help = vertices[i];
			vertices[i] = vertices[count-1-i];
			vertices[count-1-i] = help;
		}
		for (int i = 0; i < count; i++)
			vertices[i].state = vertices[i].state.reverse();
	}
	
	if (sortedVertices[0].state != VertexState.START)
		throw new RuntimeException("Internal error!");
	
	// create halfedges
	HalfEdge[] halfEdges = new HalfEdge[count];
	for (int i = 0; i < count; i++)
	{
		halfEdges[i] = new HalfEdge();
		halfEdges[i].origin = vertices[i];
		halfEdges[i].twin = new HalfEdge();
		halfEdges[i].twin.twin = halfEdges[i];
	}
	for (int i = 0; i < count; i++)
	{
		halfEdges[i].next = halfEdges[(i+1) % count];
		halfEdges[i].prev = halfEdges[(i-1+count) % count];
		halfEdges[i].twin.next = halfEdges[i].prev.twin;
		halfEdges[i].twin.prev = halfEdges[i].next.twin;
		halfEdges[i].twin.origin = halfEdges[i].next.origin;
	}
	
	// create edges
	Edge[] edges = new Edge[count];
	for (int i = 0; i < count; i++)
	{
		edges[i] = new Edge();
		edges[i].start = vertices[i];
		edges[i].end = vertices[(i+1) % count];
		edges[i].partner = halfEdges[i];
		vertices[i].incidentEdge = edges[i];
	}
	for (int i = 0; i < count; i++)
		vertices[i].previousEdge = edges[(i-1+count) % count];
	
	calculate(listener, vertices, sortedVertices, halfEdges, edges);
}

public void calculate(Contour[] contours, TriangulationListener listener)
{
	int[] count = new int[contours.length];
	int sum = 0;
	for (int i = 0; i < count.length; i++)
	{
		count[i] = contours[i].length();
		sum += count[i];
	}
	
	// create vertices
	Vertex[] vertices = new Vertex[sum];
	int offset = 0;
	for (int j = 0; j < count.length; j++)
	{
		for (int i = 0; i < count[j]; i++)
			vertices[offset+i] = new Vertex(offset+i, contours[j].get(i));
		offset += count[j];
	}
	
	offset = 0;
	for (int j = 0; j < count.length; j++)
	{
		vertices[offset+0].state = label(vertices[offset+count[j]-1], vertices[offset+0], vertices[offset+1]);
		for (int i = 1; i < count[j]-1; i++)
			vertices[offset+i].state = label(vertices[offset+i-1], vertices[offset+i], vertices[offset+i+1]);
		vertices[offset+count[j]-1].state = label(vertices[offset+count[j]-2], vertices[offset+count[j]-1], vertices[offset+0]);
		offset += count[j];
	}
	
	// sort vertices in top-down order
	Vertex[] sortedVertices = vertices.clone();
	Arrays.sort(sortedVertices, TOP_DOWN_COMPARATOR);
	
	if (sortedVertices[0].state == VertexState.SPLIT)
	{
		listener.reversing();
		for (int i = 0; i < vertices.length/2; i++)
		{
			Vertex help = vertices[i];
			vertices[i] = vertices[sum-1-i];
			vertices[sum-1-i] = help;
		}
		for (int i = 0; i < sum; i++)
			vertices[i].state = vertices[i].state.reverse();
		
		for (int i = 0; i < count.length/2; i++)
		{
			Contour help = contours[i];
			contours[i] = contours[count.length-1-i];
			contours[count.length-1-i] = help;
		}
		for (int i = 0; i < count.length/2; i++)
		{
			int help = count[i];
			count[i] = count[count.length-1-i];
			count[count.length-1-i] = help;
		}
	}
	
	if (sortedVertices[0].state != VertexState.START)
		throw new RuntimeException("Internal error: "+sortedVertices[0].state);
	
	// create halfedges
	HalfEdge[] halfEdges = new HalfEdge[sum];
	offset = 0;
	for (int j = 0; j < count.length; j++)
	{
		for (int i = 0; i < count[j]; i++)
		{
			halfEdges[offset+i] = new HalfEdge();
			halfEdges[offset+i].origin = vertices[offset+i];
			halfEdges[offset+i].twin = new HalfEdge();
			halfEdges[offset+i].twin.twin = halfEdges[offset+i];
		}
		for (int i = 0; i < count[j]; i++)
		{
			halfEdges[offset+i].next = halfEdges[offset + (i+1) % count[j]];
			halfEdges[offset+i].prev = halfEdges[offset + (i-1+count[j]) % count[j]];
			halfEdges[offset+i].twin.next = halfEdges[offset+i].prev.twin;
			halfEdges[offset+i].twin.prev = halfEdges[offset+i].next.twin;
			halfEdges[offset+i].twin.origin = halfEdges[offset+i].next.origin;
		}
		offset += count[j];
	}
	
	// create edges
	Edge[] edges = new Edge[sum];
	offset = 0;
	for (int j = 0; j < count.length; j++)
	{
		for (int i = 0; i < count[j]; i++)
		{
			edges[offset+i] = new Edge();
			edges[offset+i].start = vertices[offset+i];
			edges[offset+i].end = vertices[offset+(i+1) % count[j]];
			edges[offset+i].partner = halfEdges[offset+i];
			vertices[offset+i].incidentEdge = edges[offset+i];
		}
		for (int i = 0; i < count[j]; i++)
			vertices[offset+i].previousEdge = edges[offset+(i-1+count[j]) % count[j]];
		offset += count[j];
	}
	
	calculate(listener, vertices, sortedVertices, halfEdges, edges);
}

public void calculate(Vector2[] points, TriangulationListener listener)
{
	int count = points.length;
	
	Vertex[] vertices = new Vertex[count];
	for (int i = 0; i < count; i++)
		vertices[i] = new Vertex(i, points[i]);
	
	vertices[0].state = label(vertices[count-1], vertices[0], vertices[1]);
	for (int i = 1; i < count-1; i++)
		vertices[i].state = label(vertices[i-1], vertices[i], vertices[i+1]);
	vertices[count-1].state = label(vertices[count-2], vertices[count-1], vertices[0]);
	
	calculate(vertices, listener);
}

/**
 * Flattens the polygon to 2D and calls the 2D-triangulator.
 * 
 */
public void calculate(Vector3[] points, TriangulationListener listener)
{
	Vector3 min = new Vector3(points[0]);
	Vector3 max = new Vector3(points[0]);
	for (int i = 1; i < points.length; i++)
	{
		min.componentMinAndSet(points[i]);
		max.componentMaxAndSet(points[i]);
	}
	
	int dir = 0;
	max.subAndSet(min);
	if (max.getY() < max.getX()) dir = 1;
	if ((max.getZ() < max.getX()) && (max.getZ() < max.getY())) dir = 2;
	
	Vector2[] data = new Vector2[points.length];
	for (int i = 0; i < data.length; i++)
	{
		switch (dir)
		{
			case 0 : data[i] = new Vector2(points[i].getY(), points[i].getZ()); break;
			case 1 : data[i] = new Vector2(points[i].getX(), points[i].getZ()); break;
			case 2 : data[i] = new Vector2(points[i].getX(), points[i].getY()); break;
			default :
				throw new RuntimeException("Internal error!");
		}
	}
	
	calculate(data, listener);
	
/*	try
	{
		calculate(data, listener);
	}
	catch (RuntimeException e)
	{
		for (int i = 0; i < data.length; i++)
			System.out.println(i+": "+data[i]);
		throw e;
	}*/
}

}
