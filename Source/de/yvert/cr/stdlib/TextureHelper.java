// arch-tag: 1dab3bb4-80c7-460c-8180-078f56c828fd
package de.yvert.cr.stdlib;

import de.yvert.geometry.Vector4;
import de.yvert.textures.Color;
import de.yvert.textures.Texture;

public class TextureHelper 
{
	
public static Vector4 queryTexture(Texture texture, Vector4 coords)
{
	Color col = new Color(Color.BLACK);
	if (texture != null) texture.getColor(coords, col);
	return new Vector4(col.getR(), col.getG(), col.getB(), 0);
}

public static Vector4 queryTexture(Texture texture, float coord)
{ 
	return queryTexture(texture, new Vector4(coord,0,0,0)); 
}

}
