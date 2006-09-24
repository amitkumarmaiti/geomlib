// arch-tag: 12ee9c89-7213-4b63-8c68-46855506c657
package de.yvert.cr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Quick and dirty dynamic class loader for Cr programs.
 * Delegates everything that is not in the <code>compiled</code> package.
 * 
 * @author Ulf Ochsenfahrt
 */
public class CrClassLoader extends ClassLoader
{

public CrClassLoader()
{/*OK*/}

@Override
public URL getResource(String name)
{ return ClassLoader.getSystemResource(name); }

@Override
public InputStream getResourceAsStream(String name)
{ return ClassLoader.getSystemResourceAsStream(name); } 

@Override
public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
{
	Class c = findLoadedClass(name);
	if (c != null)
		return c;
	
	if (name.startsWith("compiled."))
	{
		if (c == null)
		{
			byte[] data = lookupClassData(name);
			if (data == null) throw new ClassNotFoundException();
			c = defineClass(name, data, 0, data.length);
		}
		if (resolve) resolveClass(c);
		return c;
	}
	
	return findSystemClass(name);
}

private byte[] lookupClassData(String className) throws ClassNotFoundException
{
	String path = "Runtime";
	String fileName = className.replace('.', '/')+".class";
	byte[] data = loadFileData(path, fileName);
	if (data != null) return data;
	throw new ClassNotFoundException(className);
}

private byte[] getClassData(File f)
{
	try
	{
		FileInputStream stream = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
		byte[] b = new byte[1000];
		int n;
		while ((n = stream.read(b)) != -1) 
			out.write(b, 0, n);
		stream.close();
		out.close();
		return out.toByteArray();
	}
	catch (IOException e)
	{/*do naught*/}
	return null;
}

private byte[] loadFileData(String path, String fileName)
{
	File file = new File(path, fileName);
	if (file.exists())
		return getClassData(file);
	return null;
}

}
