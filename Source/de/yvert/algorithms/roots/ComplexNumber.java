// arch-tag: f87927c5-7370-4ca6-96d2-c890042cc3d8
package de.yvert.algorithms.roots;

public class ComplexNumber
{
	private static final double EPS  = 1e-6;
	
	public double re,im;
	
	public ComplexNumber(double re, double im)
	{
		this.re = re;
		this.im = im;
	}
	
	public ComplexNumber(double re)
	{
		this.re = re;
		this.im = 0;	
	}
	
	public boolean hasIm()
	{ return (Math.abs(im) > EPS); }
	
	public ComplexNumber add(ComplexNumber other)
	{
		return new ComplexNumber(re+other.re, im+other.im);
	}
	
	public ComplexNumber sub(ComplexNumber other)
	{
		return new ComplexNumber(re-other.re, im-other.im);
	}
	
	public ComplexNumber mul(ComplexNumber other)
	{
		double r = re*other.re - im*other.im;
		double i = re*other.im + im*other.re;
		return new ComplexNumber(r,i);
	}
	
	public ComplexNumber div(ComplexNumber other)
	{
		double r =  other.re/(other.re*other.re+other.im*other.im);
		double i = -other.im/(other.re*other.re+other.im*other.im);
		return mul(new ComplexNumber(r,i));
	}
	
	public ComplexNumber pow(int exp)
	{
		double rad = Math.sqrt(re*re+im*im);
		
		double phi;
		if (rad != 0) phi = Math.acos(re/rad);
		else phi = 0;
		if (im < 0) phi = 2*Math.PI-phi;
		
		double r = Math.pow(rad, exp)*Math.cos(exp*phi);
		double i = Math.pow(rad, exp)*Math.sin(exp*phi);
		
		return new ComplexNumber(r,i);
	}
	
	public ComplexNumber[] nrt(int n)
	{
		double rad = Math.sqrt(re*re+im*im);
		double r = Math.pow(rad, 1./n);
		
		double tau;
		if (rad != 0) tau = Math.acos(re/rad);
		else tau = 0;		
		if (im < 0) tau = 2*Math.PI-tau;

		ComplexNumber[] res = new ComplexNumber[n];
		for (int k = 0; k < n; k++)
		{
			double phi = (tau + 2*Math.PI*k)/n;
			res[k] = new ComplexNumber(r*Math.cos(phi), r*Math.sin(phi));
		}
		
		return res;
	}
	
	public ComplexNumber[] sqrt()
	{
		return nrt(2);
	}
	
	public ComplexNumber add(double d)
	{
		return add(new ComplexNumber(d));
	}
	
	public ComplexNumber sub(double d)
	{
		return sub(new ComplexNumber(d));
	}
	
	public ComplexNumber mul(double d)
	{
		return mul(new ComplexNumber(d));
	}
	
	public ComplexNumber div(double d)
	{
		return  div(new ComplexNumber(d));
	}
	
	@Override
	public String toString()
	{
		String s = "";
		
		if (re >= 0) s += " " + re;
		else s+= re;
		
		if (im >= 0) s+= " + " + im + "*i";
		else s+= " - " + Math.abs(im) + "*i";
		
		return s;
	}
}
