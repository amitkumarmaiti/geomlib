package de.yvert.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.yvert.cr.profiles.Material;
import de.yvert.geometry.*;

public abstract class TextModelReader
{

protected static final String INT_STR = "-?[0-9]+";
protected static final String NUM_STR = "-?[0-9.]+(?:[eE][+-]?[0-9]+)?";

protected static final Pattern POLYGON_PATTERN =
	Pattern.compile("p (\\d+)");

protected static final Pattern PREFIX_PATTERN =
	Pattern.compile("(\\w+) (.*)");

protected static final Pattern DOUBLE_1_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+NUM_STR+")");

protected static final Pattern DOUBLE_2_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+NUM_STR+") ("+NUM_STR+")");

protected static final Pattern DOUBLE_3_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+")");

protected static final Pattern DOUBLE_4_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+")");

protected static final Pattern DOUBLE_8_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+") ("+NUM_STR+")");

protected static final Pattern INT_2_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+INT_STR+") ("+INT_STR+")");

protected static final Pattern INT_3_PATTERN =
	Pattern.compile("(?:(\\w+) )?("+INT_STR+") ("+INT_STR+") ("+INT_STR+")");

protected String name;
//protected CountInputStream countStream;
protected BufferedReader in;
protected int totalSize;
protected Model model;
protected Material currentMaterial;

public TextModelReader()
{
	// Ok
}

protected String readLine() throws IOException
{
//	progressListener.update(countStream.getCount(), totalSize);
	return in.readLine();
}

protected void add(SceneItem item)
{
	item.setMaterial(currentMaterial);
	model.add(item);
}

protected String removePrefix(String s, String prefix) throws IOException
{
	Matcher m = PREFIX_PATTERN.matcher(s);
	if (m.matches())
	{
		if (!(prefix.equals(m.group(1))))
			throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		return m.group(2);
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected double decodeDouble(String s, String prefix) throws IOException
{
	Matcher m = DOUBLE_1_PATTERN.matcher(s);
	if (m.matches())
	{
		if (prefix == null)
		{
			if (m.group(1) != null)
				throw new IOException("No prefix expected in line \""+s+"\"!");
		}
		else
		{
			if (!(prefix.equals(m.group(1))))
				throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		}
		return Double.parseDouble(m.group(2));
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected Vector2 decodeVector2(String s, String prefix) throws IOException
{
	Matcher m = DOUBLE_2_PATTERN.matcher(s);
	if (m.matches())
	{
		if (prefix == null)
		{
			if (m.group(1) != null)
				throw new IOException("No prefix expected in line \""+s+"\"!");
		}
		else
		{
			if (!(prefix.equals(m.group(1))))
				throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		}
		double x = Double.parseDouble(m.group(2));
		double y = Double.parseDouble(m.group(3));
		return new Vector2(x, y);
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected Vector3 decodeVector3(String s, String prefix) throws IOException
{
	Matcher m = DOUBLE_3_PATTERN.matcher(s);
	if (m.matches())
	{
		if (prefix == null)
		{
			if (m.group(1) != null)
				throw new IOException("No prefix expected in line \""+s+"\"!");
		}
		else
		{
			if (!(prefix.equals(m.group(1))))
				throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		}
		double x = Double.parseDouble(m.group(2));
		double y = Double.parseDouble(m.group(3));
		double z = Double.parseDouble(m.group(4));
		return new Vector3(x, y, z);
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected Vector4 decodeVector4(String s, String prefix) throws IOException
{
	Matcher m = DOUBLE_4_PATTERN.matcher(s);
	if (m.matches())
	{
		if (prefix == null)
		{
			if (m.group(1) != null)
				throw new IOException("No prefix expected in line \""+s+"\"!");
		}
		else
		{
			if (!(prefix.equals(m.group(1))))
				throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		}
		double x = Double.parseDouble(m.group(2));
		double y = Double.parseDouble(m.group(3));
		double z = Double.parseDouble(m.group(4));
		double w = Double.parseDouble(m.group(5));
		return new Vector4(x, y, z, w);
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected int[] decodeInt3(String s, String prefix) throws IOException
{
	Matcher m = INT_3_PATTERN.matcher(s);
	if (m.matches())
	{
		if (prefix == null)
		{
			if (m.group(1) != null)
				throw new IOException("No prefix expected in line \""+s+"\"!");
		}
		else
		{
			if (!(prefix.equals(m.group(1))))
				throw new IOException("Expected \""+prefix+"\" in line \""+s+"\"!");
		}
		int[] result = new int[3];
		result[0] = Integer.parseInt(m.group(2));
		result[1] = Integer.parseInt(m.group(3));
		result[2] = Integer.parseInt(m.group(4));
		return result;
	}
	else
		throw new IOException("No Match: \""+s+"\"");
}

protected abstract void parse() throws IOException;

public Model read(String sname, InputStream inStream, int size) throws IOException
{
	this.name = sname;
//	this.progressListener = listener;
//	this.countStream = new CountInputStream(inStream);
	this.totalSize = size;
	this.in = new BufferedReader(new InputStreamReader(inStream));
	this.model = new Model(name);
	parse();
	return model;
}

}
