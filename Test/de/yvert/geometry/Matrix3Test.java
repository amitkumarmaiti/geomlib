// arch-tag: 2bd8d9ff-2146-43c8-8ac3-67295a33cf32
package de.yvert.geometry;

import junit.framework.TestCase;

public class Matrix3Test extends TestCase
{

private void mytest(Quaternion q)
{
	Matrix3 m = new Matrix3(q);
	Quaternion p = m.getQuaternion();
	Quaternion kp = new Quaternion(p);
	p.subAndSet(q);
	double diff = Math.abs(p.getX())+Math.abs(p.getY())+Math.abs(p.getZ())+Math.abs(p.getW());
	if (diff > 0.01)
		throw new RuntimeException("Test failed: "+kp+" != "+q);
}

public void testX1()
{ mytest(new Quaternion(1, 0, 0, 0)); }

public void testX2()
{ mytest(new Quaternion(-1, 0, 0, 0)); }

public void testY1()
{ mytest(new Quaternion(0, 1, 0, 0)); }

public void testY2()
{ mytest(new Quaternion(0, -1, 0, 0)); }

public void testZ1()
{ mytest(new Quaternion(0, 0, 1, 0)); }

public void testZ2()
{ mytest(new Quaternion(0, 0, -1, 0)); }

public void testW1()
{ mytest(new Quaternion(0, 0, 0, 1)); }

public void testW2()
{ mytest(new Quaternion(0, 0, 0, -1)); }

}
