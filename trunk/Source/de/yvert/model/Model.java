package de.yvert.model;

import java.util.ArrayList;
import java.util.Iterator;

import de.yvert.geometry.*;

public class Model
{

private String name;
private ArrayList<SceneObject> objects = new ArrayList<SceneObject>();

public Model(String name)
{ this.name = name; }

public String getName()
{ return name; }

public int size()
{ return objects.size(); }

public Iterator<SceneObject> iterator()
{ return objects.iterator(); }

public void add(SceneObject object)
{ objects.add(object); }

public TriangleMesh[] toTriangleMeshArray()
{
	ArrayList<TriangleMesh> result = new ArrayList<TriangleMesh>();
	Triangulation tri = new Triangulation();
	for (int i = 0; i < objects.size(); i++)
	{
		SceneObject object = objects.get(i);
		if (object instanceof TriangleMesh)
			result.add((TriangleMesh) object);
		else
			object.triangulate(tri);
	}
	
	if (tri.size() > 0)
	{
		for (Triangle t : tri)
			result.add(new TriangleMesh(t));
	}
	
	return result.toArray(new TriangleMesh[0]);
}

}
