// arch-tag: 18dd429d-658b-4dc6-a2f6-c699569e93cf
package de.yvert.geometry;

/**
 * A {@link SceneObject} describes a piece of geometry. As we are only able to
 * render triangles right now, a {@link SceneObject} must be able to 
 * triangulate itself.
 * 
 * @see Triangulation
 * @author Ulf Ochsenfahrt
 */
public abstract class SceneObject
{

/** A global id counter */
private static int idCounter = 0;

/**
 * <code>Scene.getSceneItemCollection</code> uses this to return the same sequence of ids each time.
 */
public final static void resetIDCounter()
{ idCounter = 0; }

/**
 * A (more or less, see <code>resetIDCounter</code>) unique id for that object.
 */
public final int id = idCounter++;

/**
 * Triangulates this {@link SceneObject} and adds the resulting triangles to
 * the given {@link Triangulation}.
 */
public abstract void triangulate(Triangulation tri);

}
