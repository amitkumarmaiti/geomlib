// arch-tag: 079f8c11-4acd-47cd-983b-498b70f88fc4
package de.yvert.cr.stdlib;

import de.yvert.geometry.Matrix4;
import de.yvert.geometry.Vector4;

public class ArrayHelper
{
	
public static int[] copy(int[] x, int[] y)
{
	for (int i = 0; i < x.length; i++)
		x[i] = y[i];
	return x;	
}

public static float[] copy(float[] x, float[] y)
{
	for (int i = 0; i < x.length; i++)
		x[i] = y[i];
	return x;
}

public static boolean[] copy(boolean[] x, boolean[] y)
{
	for (int i = 0; i < x.length; i++)
		x[i] = y[i];
	return x;
}

public static Vector4[] copy(Vector4[] x, Vector4[] y)
{
	for (int i = 0; i < x.length; i++)
		x[i].set(y[i]);
	return x;
}

public static Matrix4[] copy(Matrix4[] x, Matrix4[] y)
{
	for (int i = 0; i < x.length; i++)
		x[i].set(y[i]);
	return x;
}

}
