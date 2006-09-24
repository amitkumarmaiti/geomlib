// arch-tag: f511400c-bc90-46cd-9c25-eee86ddd4c82
package de.yvert.cr;

/** This class is used for setting parameters of Cr programs. */
public class CrUniform 
{
	
public final String name;
public final Object typeAndValue;
	
public CrUniform(String name, Object typeAndValue)
{
	this.name = name;
	this.typeAndValue = typeAndValue;
}

@Override
public String toString()
{ return name+"="+typeAndValue; }

}
