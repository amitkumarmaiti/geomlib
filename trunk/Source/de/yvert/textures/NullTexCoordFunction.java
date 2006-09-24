// arch-tag: 916db14d-48de-448e-8b86-3e28ad38506f
package de.yvert.textures;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.Vector4;

public class NullTexCoordFunction extends TexCoordFunction
{

@Override
public void genTexCoords(IntersectionResult result, Vector4 uvstcoords)
{ uvstcoords.set(0, 0, 0, 0); }

}
