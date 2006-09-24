// arch-tag: bd438346-3b08-4aaa-9881-e6a2a5d9206e
package de.yvert.cr.stdlib;

import java.util.Random;

import de.yvert.geometry.Vector2;
import de.yvert.geometry.Vector3;

public class NoiseFunction
{

byte[] p = new byte[256];
float[] g1 = new float[256];
Vector2[] g2 = new Vector2[256];
Vector3[] g3 = new Vector3[256];

public NoiseFunction()
{
	Random rand = new Random();
	for (int i = 0; i < p.length; i++)
		p[i] = (byte) i;
	for (int i = p.length-1; i >= 0; i--)
	{
		int j = rand.nextInt(i+1);
		byte h = p[i];
		p[i] = p[j];
		p[j] = h;
	}
	
	for (int i = 0; i < g3.length; i++)
	{
		Vector3 v = new Vector3(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
		g3[i] = v.normalizeAndSet();
	}
	
	for (int i = 0; i < g2.length; i++)
	{
		Vector2 v = new Vector2(rand.nextGaussian(), rand.nextGaussian());
		g2[i] = v.normalizeAndSet();
	}
	
	for (int i = 0; i < g1.length; i++)
		g1[i] = rand.nextFloat();
}

public double noise(Vector3 in, double factor)
{
	double realx = in.getX()*factor, realy = in.getY()*factor, realz = in.getZ()*factor;
	int x = (int) Math.floor(realx);
	int y = (int) Math.floor(realy);
	int z = (int) Math.floor(realz);
	
	double xf = realx-x;
	double yf = realy-y;
	double zf = realz-z;
	
	double tx = xf*xf*(3-2*xf);
	double ty = yf*yf*(3-2*yf);
	double tz = zf*zf*(3-2*zf);
	
	int b0 = p[x & 0xff];
	int b1 = p[(x+1) & 0xff];
	
	int b00 = p[(b0 + y) & 0xff];
	int b01 = p[(b0 + y+1) & 0xff];
	int b10 = p[(b1 + y) & 0xff];
	int b11 = p[(b1 + y+1) & 0xff];
	
	double u, v;
	u = g3[(b00 + z) & 0xff].multiply(xf, yf, zf);
	v = g3[(b10 + z) & 0xff].multiply(xf-1, yf, zf);
	double a = u+tx*(v-u);
	
	u = g3[(b01 + z) & 0xff].multiply(xf, yf-1, zf);
	v = g3[(b11 + z) & 0xff].multiply(xf-1, yf-1, zf);
	double b = u+tx*(v-u);
	
	u = g3[(b00 + z+1) & 0xff].multiply(xf, yf, zf-1);
	v = g3[(b10 + z+1) & 0xff].multiply(xf-1, yf, zf-1);
	double c = u+tx*(v-u);
	
	u = g3[(b01 + z+1) & 0xff].multiply(xf, yf-1, zf-1);
	v = g3[(b11 + z+1) & 0xff].multiply(xf-1, yf-1, zf-1);
	double d = u+tx*(v-u);
	
	double e = a+ty*(b-a);
	double f = c+ty*(d-c);
	
	return e+tz*(f-e);
}

}
