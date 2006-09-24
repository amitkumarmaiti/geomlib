// arch-tag: 57a97d3a-88ae-4c59-9f3b-bb5db3af4f90
package de.yvert.algorithms.roots;

public class Polynom
{

	private double EPS = 1e-6;
	private double[] c;
	
	public Polynom(double[] c)
	{
		int i;
		for (i = c.length-1; (i > 0) && (Math.abs(c[i]) <= EPS); i--)
		{
			// do nothing, this loop finds the first value that is smaller than EPS
		}
		
		this.c = new double[i+1];
		for (int j = 0; j <= i; j++) this.c[j] = c[j];
	}
	
	public double eval(double x)
	{
		double sum = 0, tmp = 1;
		for (int i = 0; i < c.length; i++)
		{
			sum += tmp*c[i];
			tmp *= x;
		}
		return sum;
	}
	
	public int getDegree()
	{
		return c.length-1;
	}	
	
	public double getC(int i)
	{
		return c[i];
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for (int i = c.length-1; i >= 0; i--)
		{
			if (Math.abs(c[i]) <= EPS) continue;
			if ((c[i] >= 0) && (i != c.length-1)) s += "+" + c[i] + "x^" + i;
			else s += c[i] + "x^" + i;
		}
		return s;
	}

}
