package de.yvert.algorithms.triangulation;

import de.yvert.geometry.Vector2;

class Vertex
{

int orig;
VertexState state;
boolean isLeft;
Vector2 value;
Edge incidentEdge;
Edge previousEdge;

public Vertex()
{/*OK*/}

public Vertex(int orig, Vector2 value)
{
	this.orig = orig;
	this.value = value;
}

@Override
public String toString()
{ return Integer.toString(orig); }

}