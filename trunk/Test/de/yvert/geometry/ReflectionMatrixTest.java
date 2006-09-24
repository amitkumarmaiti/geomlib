// arch-tag: 21e9a5ee-ef62-4e96-b4b8-3f380b0cfa2e
package de.yvert.geometry;

import junit.framework.TestCase;

public class ReflectionMatrixTest extends TestCase
{

Vector4 plane;
Matrix4 refMatrix;

@Override
public void setUp()
{
	plane = new Vector4(1, 1, 0, 0).normalizeAndSet();
	plane.setV3(-Math.sqrt(2));
	
	refMatrix = Matrix4.createReflectionMatrix(new Matrix4(), plane);
}

public void testTest1()
{
//	System.out.println(refMatrix);
//	Vector4 v;
	new Vector4(0, 0, 0, 1).multiplyAndSet(refMatrix);// System.out.println(v);
	new Vector4(0, -1, 0, 1).multiplyAndSet(refMatrix);// System.out.println(v);
}

}
