// arch-tag: e76ac155-f6d2-4359-887a-7be3d6e414a1
package de.yvert.textures;

import java.util.Random;
import java.util.regex.*;

/**
 * Stores a color as rgb float values.
 * <p>
 * TODO: Include alpha as well?
 * 
 * @author Stefan Goldmann
 * @author Ulf Ochsenfahrt
 */
public final class Color
{

private static final String DOUBLE_PATTERN =
	"(?:\\+|\\-)?(?:NaN|Infinity|\\d+(?:\\.\\d*)?(?:(?:e|E)(?:\\+|\\-)?\\d+)?)";
private static final Pattern SERIAL_PATTERN = Pattern.compile(
	"\\(("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+"),("+DOUBLE_PATTERN+")\\)");


public static final Color BLACK = new Color(0, 0, 0);
public static final Color WHITE = new Color(1, 1, 1);
public static final Color RED   = new Color(1, 0, 0);

static Random rand = new Random();

static Color randomColor()
{
	return new Color(3.0f*rand.nextFloat()/4.0f+0.25f, 3.0f*rand.nextFloat()/4.0f+0.25f, 3.0f*rand.nextFloat()/4.0f+0.25f);
}

public final float[] rgb;

public Color()
{
	rgb = new float[3];
	rgb[0] = rgb[1] = rgb[2] = 0; 
}

public Color(float r, float g, float b)
{
	this();
	set(r, g, b);
}

public Color(double r, double g, double b)
{
	this();
	set(r, g, b);
}

public Color(Color c)
{
	this();
	set(c);
}


public float getR()
{ return rgb[0]; }

public float getG()
{ return rgb[1]; }

public float getB()
{ return rgb[2]; }

public void set(float r, float g, float b)
{ rgb[0] = r; rgb[1] = g; rgb[2] = b; }

public void set(double r, double g, double b)
{ set((float) r, (float) g, (float) b); }

public void set(Color c)
{ System.arraycopy(c.rgb, 0, rgb, 0, 3); }

public void set(double d, Color c)
{ set(c.getR()*d, c.getG()*d, c.getB()*d); }

public void setToZero()
{ rgb[0] = rgb[1] = rgb[2] = 0; }

public void setToOne()
{ rgb[0] = rgb[1] = rgb[2] = 1; }

public void add(double r, double g, double b)
{
	rgb[0] += r;
	rgb[1] += g;
	rgb[2] += b;
}

public void add(Color color)
{
	rgb[0] += color.rgb[0];
	rgb[1] += color.rgb[1];
	rgb[2] += color.rgb[2];
}

public void add(double factor, Color color)
{
	rgb[0] += factor*color.rgb[0];
	rgb[1] += factor*color.rgb[1];
	rgb[2] += factor*color.rgb[2];
}

public void mul(Color color)
{
	rgb[0] *= color.rgb[0];
	rgb[1] *= color.rgb[1];
	rgb[2] *= color.rgb[2];
}

public void mul(float scale)
{
	rgb[0] *= scale;
	rgb[1] *= scale;
	rgb[2] *= scale;
}

public void mul(double scale)
{ mul((float) scale); }

public void mul(float s1, float s2, float s3)
{
	rgb[0] *= s1;
	rgb[1] *= s2;
	rgb[2] *= s3;
}

public void light(Color color)
{
	rgb[0] = (rgb[0]+color.rgb[0])/2;
	rgb[1] = (rgb[1]+color.rgb[1])/2;
	rgb[2] = (rgb[2]+color.rgb[2])/2;
}

public void ambient(float r, float g, float b)
{
	rgb[0] = rgb[0]*(1-r)+r;
	rgb[1] = rgb[1]*(1-g)+g;
	rgb[2] = rgb[2]*(1-b)+b;
}

@Override
public String toString()
{ return "Color: (r,g,b) = "+rgb[0]+","+rgb[1]+","+rgb[2]; }

public String serialize()
{ return "("+rgb[0]+","+rgb[1]+","+rgb[2]+")"; }

public void decode(String value)
{
	Matcher m = SERIAL_PATTERN.matcher(value);
	if (m.matches())
	{
		rgb[0] = Float.parseFloat(m.group(1));
		rgb[1] = Float.parseFloat(m.group(2));
		rgb[2] = Float.parseFloat(m.group(3));
	}
	else
		throw new NumberFormatException("Invalid Color \""+value+"\"!");
}

}
