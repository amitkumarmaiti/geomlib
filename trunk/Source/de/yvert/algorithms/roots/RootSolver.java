// arch-tag: 15d81dfc-a38f-48f4-8b1d-878bc70f605c
package de.yvert.algorithms.roots;

public class RootSolver
{
	private static final double EPS = 1e-6;
	
	protected RootSolver()
	{/*OK*/}
	
	public static Roots solve(Polynom pol)
	{
		switch(pol.getDegree())
		{
			case 1: return solve1(pol);
			case 2: return solve2(pol);
			case 3: return solve3(pol);
			case 4: return solve4(pol);
			default: throw new IllegalArgumentException();
		}
	}
	
	protected static Roots solve1(Polynom pol)
	{
		Roots roots = new Roots();
		roots.add(-pol.getC(0)/pol.getC(1));
		return roots;
	}

	protected static Roots solve2(Polynom pol)
	{
		Roots roots = new Roots();
		double c0 = pol.getC(0);
		double c1 = pol.getC(1);
		double c2 = pol.getC(2);
		
		ComplexNumber t = new ComplexNumber(c1*c1 - 4*c2*c0).sqrt()[0];
		roots.add(new ComplexNumber(-c1).add(t).mul(1/(2*c2)));
		roots.add(new ComplexNumber(-c1).sub(t).mul(1/(2*c2)));

		return roots;
	}	

	protected static Roots solve3(Polynom pol)
	{
		Roots roots = new Roots();
		double c0 = pol.getC(0);
		double c1 = pol.getC(1);
		double c2 = pol.getC(2);
		double c3 = pol.getC(3);

		// Normalize polynom
		c2 /= c3; c1 /= c3; c0 /= c3;

		// Coefficients of substitute polynom
		double p = c1 - 1/3.0*c2*c2;
		double q = 2/27.0*c2*c2*c2 - 1/3.0*c1*c2 + c0;
		double d = 1/27.0*p*p*p + 1/4.0*q*q;

		// Substitute (and actual) roots
		if (d >= 0) 
		{
			// 1 real and 2 complex roots
			double h1 = -q/2.0 + Math.sqrt(d);
			double h2 = -q/2.0 - Math.sqrt(d);

			double u,v;
			if (h1 >= 0) u = Math.pow(h1, 1/3.0); else u = -Math.pow(-h1, 1/3.0);
			if (h2 >= 0) v = Math.pow(h2, 1/3.0); else v = -Math.pow(-h2, 1/3.0);

			double re = -(u+v)/2.0;
			double im = Math.sqrt(3)*(u-v)/2.0;

			roots.add((u+v) - c2/3.0);
			roots.add(new ComplexNumber(re, im).sub(c2/3.0));
			roots.add(new ComplexNumber(re,-im).sub(c2/3.0));
		}
		else
		{
			// 3 real roots
			double delta = Math.sqrt(-p*p*p/27.0);
			double phi = Math.acos(-q/(2*delta));
			double y1 = 2*Math.pow(delta, 1/3.0)*Math.cos(phi/3.0);
			double y2 = 2*Math.pow(delta, 1/3.0)*Math.cos(phi/3.0 + 2*Math.PI/3.0);
			double y3 = 2*Math.pow(delta, 1/3.0)*Math.cos(phi/3.0 + 4*Math.PI/3.0);
			
			roots.add(y1 - c2/3.0);
			roots.add(y2 - c2/3.0);
			roots.add(y3 - c2/3.0);
		}

		return roots;		
	}	

	protected static Roots solve4(Polynom pol)
	{
		// Normalize
		double n = pol.getC(4);
		double a = pol.getC(3)/n;
		double b = pol.getC(2)/n;
		double c = pol.getC(1)/n;
		double d = pol.getC(0)/n;
		
		// Substitute, calculate resolvent and its roots
		double p = b - 3*a*a/8;
		double q = a*a*a/8 - a*b/2 + c;
		double r = -(3*a*a*a*a - 16*a*a*b + 64*a*c - 256*d)/256;
		double[] co = new double[] {q*q, p*p-4*r, -2*p, 1};
		Polynom res = new Polynom(co);
		Roots resRoots = solve3(res);
				
		ComplexNumber[] z1 = resRoots.get(0).mul(-1).sqrt();
		ComplexNumber[] z2 = resRoots.get(1).mul(-1).sqrt();
		ComplexNumber[] z3 = resRoots.get(2).mul(-1).sqrt();
		
		Roots roots = new Roots();
		for (int i = 0; i < 2; i++)
		for (int j = 0; j < 2; j++)
		for (int k = 0; k < 2; k++)
		{
			ComplexNumber chk = z1[i].mul(z2[j]).mul(z3[k]);
			if (Math.abs(chk.im) > EPS) continue;
			if (Math.abs(chk.re+q) > EPS) continue;
			
			ComplexNumber y1 = z1[i].add(z2[j]).add(z3[k]).mul(.5);
			ComplexNumber y2 = z1[i].sub(z2[j]).sub(z3[k]).mul(.5);
			ComplexNumber y3 = z2[j].sub(z1[i]).sub(z3[k]).mul(.5);
			ComplexNumber y4 = z3[k].sub(z1[i]).sub(z2[j]).mul(.5);
			
			roots.add(y1.sub(a/4));
			roots.add(y2.sub(a/4));
			roots.add(y3.sub(a/4));
			roots.add(y4.sub(a/4));
			return roots;
		}

		// Sould never happen
		return roots;
	}
}
