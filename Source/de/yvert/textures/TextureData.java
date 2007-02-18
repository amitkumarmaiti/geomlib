package de.yvert.textures;

/**
 * Container class that bundles textures and their 
 * corresponding texture coordinate generation functions.
 * 
 * @author Stefan Goldman
 */
public class TextureData 
{

private Texture[] textures;
private TexCoordFunction[] texFunctions;

// Convenience constructor
public TextureData(Texture tex, TexCoordFunction func)
{
	this(new Texture[] {tex}, new TexCoordFunction[] {func});
}

//Convenience constructor
public TextureData(Texture tex1, TexCoordFunction func1, Texture tex2, TexCoordFunction func2)
{
	this(new Texture[] {tex1, tex2}, new TexCoordFunction[] {func1, func2});
}

// Performs certain sanity checks. If your object (used for Material and NormalMap of 
// SceneItem) does not have textures, then don't use this constructor but leave
// the corresponding fields in SceneItem blank (ie. null).
public TextureData(Texture[] textures, TexCoordFunction[] texFunctions)
{
	// Sanity checks
	if (textures == null) throw new NullPointerException();
	if (texFunctions == null) throw new NullPointerException();
	if (textures.length == 0) throw new IllegalArgumentException();
	if (texFunctions.length == 0) throw new IllegalArgumentException();
	if (textures.length != texFunctions.length) throw new IllegalArgumentException(); 

	this.textures = textures;
	this.texFunctions = texFunctions;
}

public Texture[] getTextures()
{ return textures; }

public TexCoordFunction[] getTexCoordFunctions()
{ return texFunctions; }

// FIXME: Do we need separate set-Functions?

}
