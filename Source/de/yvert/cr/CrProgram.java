// arch-tag: 9dae5114-946e-4d45-b2ad-6c760deffb02
package de.yvert.cr;


/** 
 * Code created by SLC implements this interface to allow parameter setting/retrieval 
 * as well as texture units/coordinate binding sanity checks.
 */
public interface CrProgram
{

public String getName();
public CrUniform[] getUniforms();
public void setUniform(String name, Object value) throws IllegalArgumentException;
public CrTextureInfo getTextureInfo();

}
