// arch-tag: 0857d37b-4fdb-427b-8bb2-2127e90599ce
package de.yvert.algorithms.roots;

import java.util.ArrayList;

public class Roots
{
	private ArrayList<ComplexNumber> entries = new ArrayList<ComplexNumber>(); 
	
	public Roots()
	{/*OK*/}
	
	public int size()
	{ return entries.size(); }
	
	public void add(ComplexNumber c)
	{
		entries.add(c);
	}
	
	public void add(double re, double im)
	{
		entries.add(new ComplexNumber(re,im));
	}
		
	public void add(double re)
	{
		entries.add(new ComplexNumber(re,0));
	}
	
	public ComplexNumber get(int i)
	{
		return entries.get(i);
	}
	
	public double getRe(int i)
	{
		return entries.get(i).re;
	}
	
	public double getIm(int i)
	{
		return entries.get(i).im;
	}
}
