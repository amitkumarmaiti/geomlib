// arch-tag: a5db53bf-77e5-4e01-b9a6-e4b7ecaee6b0
package de.yvert.geometry;

/**
 * An OpenGL-like matrix stack for Matrix4 objects.
 * 
 * @author Ulf Ochsenfahrt
 */
public final class Matrix4Stack
{

private Matrix4[] stack = new Matrix4[] { new Matrix4().reset() };
private int stackTop = 0;

private Matrix4 tempMatrix = new Matrix4();

public Matrix4Stack()
{/*OK*/}


public Matrix4Stack reset()
{
	stackTop = 0;
	identity();
	return this;
}

public Matrix4Stack multiply(Matrix4 m)
{
	stack[stackTop].multiplyAndSet(m);
	return this;
}

public Matrix4Stack set(Matrix4 m)
{
	stack[stackTop].set(m);
	return this;
}

public Matrix4 get()
{ return stack[stackTop]; }

public Matrix4 top()
{ return stack[stackTop]; }

public Matrix4Stack identity()
{
	stack[stackTop].reset();
	return this;
}

public Matrix4Stack translate(double tx, double ty, double tz)
{
	tempMatrix.reset();
	tempMatrix.setEntry(0, 3, tx).setEntry(1, 3, ty).setEntry(2, 3, tz);
	return multiply(tempMatrix);
}

public Matrix4Stack scale(double sx, double sy, double sz)
{
	tempMatrix.reset();
	tempMatrix.setEntry(0, 0, sx).setEntry(1, 1, sy).setEntry(2, 2, sz);
	return multiply(tempMatrix);
}

public Matrix4Stack scale(Vector3 s)
{ return scale(s.getX(), s.getY(), s.getZ()); }

public Matrix4Stack translate(Vector3 v, double scale)
{ return translate(scale*v.getX(), scale*v.getY(), scale*v.getZ()); }

public Matrix4Stack translate(Vector3 v)
{ return translate(v, 1); }

public Matrix4Stack rotate(Vector3 v, double angle)
{
	Vector3 temp = v.normalize();
	Quaternion q = new Quaternion(temp, angle);
	tempMatrix.set(q);
	return multiply(tempMatrix);
}

public Matrix4Stack rotate(Quaternion q)
{
	tempMatrix.set(q);
	return multiply(tempMatrix);
}

public Matrix4Stack rotateX(double angle)
{ return rotate(Vector3.X, angle); }

public Matrix4Stack rotateY(double angle)
{ return rotate(Vector3.Y, angle); }

public Matrix4Stack rotateZ(double angle)
{ return rotate(Vector3.Z, angle); }


public void push()
{
	stackTop++;
	if (stack.length <= stackTop)
	{
		Matrix4[] temp = new Matrix4[2*stack.length+1];
		for (int i = 0; i < stack.length; i++)
			temp[i] = stack[i];
		stack = temp;
	}
	stack[stackTop] = new Matrix4(stack[stackTop-1]);
}

public void pop()
{ stackTop--; }

}
