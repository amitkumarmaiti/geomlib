// arch-tag: 2e1e087e-f7f6-4d03-a200-097fc3adfa4d
package de.yvert.geometry;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A collection of {@link SceneItem}s, useful for raytracing.
 * <p>
 * 
 * @see SceneObject
 * @see SceneItem
 * @author Ulf Ochsenfahrt
 */
public class SceneItemCollection implements Iterable<SceneItem>
{

protected ArrayList<SceneItem> data = new ArrayList<SceneItem>();

public int size()
{ return data.size(); }

public void add(SceneItem item)
{ data.add(item); }

public void add(Triangulation tri)
{ data.addAll(tri.data); }

public SceneItem[] toArray()
{ return data.toArray(new SceneItem[data.size()]); }

public Iterator<SceneItem> iterator()
{ return data.iterator(); }

}
