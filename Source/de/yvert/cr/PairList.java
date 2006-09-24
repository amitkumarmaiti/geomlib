// arch-tag: 4a4d6c21-9843-445e-b87b-de46d364baa2
package de.yvert.cr;

import java.util.ArrayList;

/** This class should go into some sort of utils package. */
public class PairList<O1,O2>
{
	
	public static class ObjectPair<O1,O2>
	{
		public final O1 o1;
		public final O2 o2;
		public ObjectPair(O1 o1, O2 o2)
		{
			this.o1 = o1;
			this.o2 = o2;
		}
	}

private ArrayList<ObjectPair<O1,O2>> entries = new ArrayList<ObjectPair<O1,O2>>();
	
public PairList()
{/*OK*/}
	
public void add(O1 o1, O2 o2)
{ entries.add(new ObjectPair<O1,O2>(o1,o2)); }

public ObjectPair<O1,O2> get(int index)
{ return entries.get(index); }

public O1 getLeft(int index)
{ return get(index).o1; }
	
public O2 getRight(int index)
{ return get(index).o2; }

}
