// arch-tag: 60b13cab-d5d7-48e2-b7e0-680f828d6997
package de.yvert.cr;


/** This class is used for sanity checks of Cr programs. */
public class CrTextureInfo
{

public enum SamplerTypeEnum
{ SAMPLER1D, SAMPLER2D, SAMPLER3D, SAMPLERCUBE }

public enum CoordinateTypeEnum
{ FLOAT, FLOAT2, FLOAT3 }

public enum SamplerBindingEnum
{ TEXTURE_0, TEXTURE_1, TEXTURE_2, TEXTURE_3, TEXTURE_4, TEXTURE_5, TEXTURE_6, TEXTURE_7 }

public enum CoordinateBindingEnum 
{ TEXCOORD_0, TEXCOORD_1, TEXCOORD_2, TEXCOORD_3, TEXCOORD_4, TEXCOORD_5, TEXCOORD_6, TEXCOORD_7 }

private PairList<SamplerTypeEnum,SamplerBindingEnum> samplerBindings = new PairList<SamplerTypeEnum,SamplerBindingEnum>();	
private PairList<CoordinateTypeEnum,CoordinateBindingEnum> coordinateBindings = new PairList<CoordinateTypeEnum,CoordinateBindingEnum>();	

public CrTextureInfo()
{/*OK*/}

public void addSamplerBinding(SamplerTypeEnum samplerType, SamplerBindingEnum binding)
{ samplerBindings.add(samplerType,binding); }

public void addCoordinateBinding(CoordinateTypeEnum coordType,CoordinateBindingEnum binding)
{ coordinateBindings.add(coordType, binding); }

public PairList<SamplerTypeEnum,SamplerBindingEnum> getAllSamplerBindings()
{ return samplerBindings; }

public PairList<CoordinateTypeEnum,CoordinateBindingEnum> getAllCoordinateBindings()
{ return coordinateBindings; }

}
