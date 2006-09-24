// arch-tag: 9738c7ec-4dfb-439a-b724-7828b1d8448c
package de.yvert.algorithms.triangulation;

class HalfEdge
{

Vertex origin;
HalfEdge twin;
HalfEdge next;
HalfEdge prev;

@Override
public String toString()
{ return origin.orig+" -> "+twin.origin.orig; }

}