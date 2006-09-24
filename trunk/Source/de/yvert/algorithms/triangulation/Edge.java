// arch-tag: 0c75d864-e528-4b99-a04a-b8b50f105963
package de.yvert.algorithms.triangulation;

class Edge
{

Vertex start, end;
Vertex helper;
HalfEdge helperEdge;
HalfEdge partner;

public Edge(Vertex start, Vertex end)
{
	this.start = start;
	this.end = end;
}

public Edge()
{/*OK*/}

@Override
public String toString()
{ return start.orig+" - "+end.orig; }

public void flip()
{
	Vertex help = start;
	start = end;
	end = help;
}

}