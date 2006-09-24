// arch-tag: e785a258-91c0-45f6-b006-a7b9bde52ffd
package de.yvert.algorithms.triangulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import de.yvert.geometry.Vector2;

class Main
{

public static void main(String[] args)
{
	ArrayList<Contour> contours = new ArrayList<Contour>();
	ArrayList<Vector2> list = new ArrayList<Vector2>();
	
	list.clear();
	list.add(new Vector2(0, 0));
	list.add(new Vector2(4, 0));
	list.add(new Vector2(2, 3));
	contours.add(new Contour(list.toArray(new Vector2[0])));
	
	list.clear();
	list.add(new Vector2(1, 0.5));
	list.add(new Vector2(2, 2));
	list.add(new Vector2(3, 0.5));
	contours.add(new Contour(list.toArray(new Vector2[0])));
	
	PolygonTriangulator2D triangulator = new PolygonTriangulator2D();
	
	JFrame frame = new JFrame("JavaDraw!");
	final EdgeCanvas canvas = new EdgeCanvas(triangulator);
	canvas.setPreferredSize(new Dimension(800, 600));
	frame.setLayout(new BorderLayout(4,4));
	frame.add(canvas, BorderLayout.CENTER);
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	frame.setLocation(100, 100);
	frame.pack();
	frame.setVisible(true);
	
	Contour[] data = contours.toArray(new Contour[0]);
	triangulator.calculate(data, new TriangulationAdapter()
		{
			@Override
			public void addEdge(int a, int b)
			{
//				if ((a == 15) && (b == 44)) throw new RuntimeException("Argh!");
//				System.out.println("Connecting: "+a+" to "+b);
				canvas.reset();
				for (int i = 0; i < 2; i++)
				{
					canvas.repaint();
					try
					{ Thread.sleep(500); }
					catch (InterruptedException e)
					{ e.printStackTrace(); }
				}
			}
		});
}

public static void main2(String[] args)
{
/*	ArrayList<Vector2> list = new ArrayList<Vector2>();
	list.add(new Vector2(401.0, -195.0));
	list.add(new Vector2(265.0, -243.0));
	list.add(new Vector2(577.0, -389.0));
	list.add(new Vector2(663.0, -65.0));
	list.add(new Vector2(500.0, -101.0));
	list.add(new Vector2(543.0, -147.0));
	list.add(new Vector2(517.0, -271.0));
	list.add(new Vector2(550.0, -199.0));
	list.add(new Vector2(505.0, -315.0));
	list.add(new Vector2(383.0, -247.0));
	list.add(new Vector2(464.0, -143.0));
	list.add(new Vector2(431.0, -127.0));
	list.add(new Vector2(369.0, -147.0));
	list.add(new Vector2(403.0, -145.0));*/
	
	ArrayList<Vector2> list = new ArrayList<Vector2>();
	list.add(new Vector2(326.0, -134.0));
	list.add(new Vector2(124.0, -444.0));
	list.add(new Vector2(530.0, -450.0));
	list.add(new Vector2(386.0, -380.0));
	list.add(new Vector2(276.0, -384.0));
	list.add(new Vector2(336.0, -308.0));
	list.add(new Vector2(386.0, -380.0));
	list.add(new Vector2(530.0, -450.0));
	
	
	Vector2[] data = list.toArray(new Vector2[0]);
	
	PolygonTriangulator2D triangulator = new PolygonTriangulator2D();
	
	JFrame frame = new JFrame("JavaDraw!");
	final EdgeCanvas canvas = new EdgeCanvas(triangulator);
	canvas.setPreferredSize(new Dimension(800, 600));
	frame.setLayout(new BorderLayout(4,4));
	frame.add(canvas, BorderLayout.CENTER);
	
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	frame.setLocation(100, 100);
	frame.pack();
	frame.setVisible(true);
	
	triangulator.calculate(data, new TriangulationAdapter()
		{
			@Override
			public void addEdge(int a, int b)
			{
//				if ((a == 15) && (b == 44)) throw new RuntimeException("Argh!");
//				System.out.println("Connecting: "+a+" to "+b);
				canvas.reset();
				for (int i = 0; i < 2; i++)
				{
					canvas.repaint();
					try
					{ Thread.sleep(500); }
					catch (InterruptedException e)
					{ e.printStackTrace(); }
				}
			}
		});
}

}
