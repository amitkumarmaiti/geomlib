// arch-tag: cff2fd71-d676-489b-b04c-4c3344ef4743
package de.yvert.textures;

import de.yvert.cr.profiles.IntersectionResult;
import de.yvert.geometry.Vector4;

public abstract class TexCoordFunction
{

public abstract void genTexCoords(IntersectionResult result, Vector4 uvstcoords);

}
