// arch-tag: c09bc157-a52e-42e7-99f7-5e17f62b0a15
package de.yvert.geometry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Helper class to allow a scene to triangulate itself.
 * <p>
 * TODO: Add triangulation settings (such as bezier patch resolution).
 * <p>
 * This is essentially just a collection of triangles. When a
 * {@link SceneObject} is asked to triangulate itself, it should add triangles
 * to the given triangulation.
 * 
 * @see SceneObject
 * @author Ulf Ochsenfahrt
 */
public class Triangulation implements Iterable<Triangle>
{

protected ArrayList<Triangle> data = new ArrayList<Triangle>();

public int size()
{ return data.size(); }

public Triangulation add(Triangle triangle)
{
	data.add(triangle);
	return this;
}

public Triangle[] toArray()
{
	return data.toArray(new Triangle[0]);
}

public Iterator<Triangle> iterator()
{ return data.iterator(); }

}
