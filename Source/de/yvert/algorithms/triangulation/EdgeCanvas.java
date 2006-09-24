// arch-tag: 16c15c18-90f1-451b-812a-ebe8e15774af
package de.yvert.algorithms.triangulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;

import javax.swing.JPanel;

class EdgeCanvas extends JPanel
{

private static final long serialVersionUID = 1L;

PolygonTriangulator2D triangulator;
HalfEdge ae, be, ce;

public EdgeCanvas(PolygonTriangulator2D triangulator)
{
	this.triangulator = triangulator;
}

private Point get(Vertex v)
{ return new Point((int) (80*v.value.getX())+100, (int) (-80*v.value.getY())+300); }

private void paintSingle(Graphics g, HalfEdge e)
{
	Point a = get(e.origin);
	Point b = get(e.twin.origin);
	g.drawLine(a.x, a.y, b.x, b.y);
	double vx = b.x-a.x;
	double vy = b.y-a.y;
	double len = Math.sqrt(vx*vx+vy*vy);
	vx /= len;
	vy /= len;
	g.drawLine(b.x+(int) (10*vy-30*vx), b.y-(int) (10*vx+30*vy), b.x-(int) (10*vx), b.y-(int) (10*vy));
}

HashSet<HalfEdge> visited = new HashSet<HalfEdge>();

private void paint(Graphics g, HalfEdge e)
{
	if (visited.contains(e)) return;
	paintSingle(g, e);
	visited.add(e);
	paint(g, e.next);
	paint(g, e.prev);
	paint(g, e.twin);
}

@Override
public void paintComponent(Graphics g)
{
	if (triangulator.statice == null) return;
  super.paintComponent(g);
  g.clearRect(0, 0, 800, 600);
  g.setColor(Color.LIGHT_GRAY);
  visited.clear();
  paint(g, triangulator.statice);
  
  g.setColor(Color.BLACK);
  paintSingle(g, ae); ae = ae.next;
  g.setColor(Color.RED);
  paintSingle(g, be); be = be.next;
  g.setColor(Color.GREEN);
  paintSingle(g, ce); ce = ce.next;
}

public void reset()
{
	ae = triangulator.statice;
	be = triangulator.statice.twin;
	ce = triangulator.statice.next.twin;
}

}